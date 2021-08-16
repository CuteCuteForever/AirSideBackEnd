package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_TRANSPONDER;
import com.ncs.airside.repository.RT_TRANSPONDER_REPO;
import com.ncs.airside.service.AsyncService;
import com.syc.function.Function.RFID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.ncs.airside.controller.AntennaController.PortHandle;
import static com.ncs.airside.controller.AntennaController.comAddr;

@RestController
public class RFIDController {

    private Logger logger = LoggerFactory.getLogger(RFIDController.class);

    @Autowired
    private RT_TRANSPONDER_REPO rt_transponder_repo;

    @Autowired
    private AsyncService service;

    @Value("${airside.rfid.rfpower}")
    private String rfidRfPower;

    @Autowired
    private AsyncService asyncService;


    @GetMapping("/rfidopen")
    public ResponseEntity rficOpen() {

        if (RFID.instance.CFHid_GetUsbCount() > 0) {
            if (RFID.instance.CFHid_OpenDevice(0)) {

                byte[] arrBuffer = new byte[48];
                byte addr = (byte) 0xff;
                if (RFID.instance.CFHid_GetDeviceSystemInfo(addr, arrBuffer))  //Get DeviceInfo
                {
                    String str = "", str1 = "";
                    str = String.format("SoftVer:%d.%d\r\n", arrBuffer[0] >> 4, arrBuffer[0] & 0x0F);
                    logger.info(str);
                    str = String.format("HardVer:%d.%d\r\n", arrBuffer[1] >> 4, arrBuffer[1] & 0x0F);
                    logger.info(str);
                    str = "SN:";
                    for (int i = 0; i < 7; i++) {
                        str1 = String.format("%02X", arrBuffer[2 + i]);
                        str = str + str1;
                    }
                    str = str + "\r\n";
                    logger.info(str);

                    RFID.instance.CFHid_ClearTagBuf();
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.set("Soft Version", String.valueOf(arrBuffer[0] & 0x0F));
                    responseHeaders.set("Hard Version", String.valueOf(arrBuffer[1] & 0x0F));
                    responseHeaders.set("SN", str1);

                    logger.info("Successfully initialized Card Reader");
                }
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("Unable to connect to Card Reader in USB. Please re-connect again"));
            }
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to detect Card Reader in USB. Please re-plug it again"));
        }
        return ResponseEntity.ok().body(new MessageResponse("Successfully initialized Card Reader"));
    }

    @GetMapping("/rfidclose")
    public ResponseEntity rfidClose() {

        RFID.instance.CFHid_CloseDevice();
        logger.info("Close RFID Card Reader.");

        return ResponseEntity.ok().body(new MessageResponse("Close RFID Card Reader successfully"));
    }
    Timer rfidTimer ;
    private String rfidEPCValue;
    @GetMapping("/rfidScanTransponderStatus")
    public ResponseEntity rfidScanTransponderStatus()  {

        RFID.instance.CFHid_ClearTagBuf();

        ExecutorService executor = Executors.newCachedThreadPool();

        Callable<String> task = new Callable<String>() {
            public String call() throws Exception{
                return scanTagProcess();
            }
        };

        Future<String> future = executor.submit(task);
        try {
            String epc = future.get(5, TimeUnit.SECONDS);

            Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndServiceAvailabilityAndRowRecordStatus(epc , "Not Spare","valid");

            if (transponderOptional.isPresent()){
                return ResponseEntity.ok().body(transponderOptional.get());
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("EPC not registered with any Not Spare transponder. Please register EPC wih transponder."));
            }
        } catch (TimeoutException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse("No EPC Tag detected. Please try again."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error occured. Please try again."));
        } finally {
            future.cancel(true); // may or may not desire this
        }
    }

    @GetMapping("/rfidScanNewTransponder")
    public ResponseEntity rfidScanNewTransponder()  {

        RFID.instance.CFHid_ClearTagBuf();

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<String> task = new Callable<String>() {
            public String call() throws Exception{
                return scanTagProcess();
            }
        };
        Future<String> future = executor.submit(task);
        try {
            String result = future.get(5, TimeUnit.SECONDS);
            return ResponseEntity.ok().body(new MessageResponse(result));
        } catch (TimeoutException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse("No EPC Tag detected. Please try again."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error occured. Please try again."));
        } finally {
            future.cancel(true); // may or may not desire this
        }
    }


    @GetMapping("/AsyncRfidScanTagMultiple/{numberOfTimes}")
    public void rfidScanTagMultiple(@PathVariable int numberOfTimes) throws InterruptedException {
        service.RfidScanMultiple(numberOfTimes);
    }


    @GetMapping("/rfidReadDeviceOneParam/{index}")
    public ResponseEntity rfidReadDeviceOneParam(@PathVariable int index) throws Exception{

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            public Object call() throws Exception{
                return rfidReadDeviceOneParamProcess(index);
            }
        };
        Future<Object> future = executor.submit(task);
        try {

            Object result = future.get(3, TimeUnit.SECONDS);
            return ResponseEntity.ok().body(new MessageResponse(String.valueOf(result)));

        } catch (TimeoutException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error occured. Please try again."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error occured. Please try again."));
        } finally {
            future.cancel(true); // may or may not desire this
        }
    }

    public String rfidReadDeviceOneParamProcess(int index) {

        boolean isContinue = true;

        String value ="" ;
        while (isContinue){

            byte bParamAddr = 0;
            byte[] bValue = new byte[2];

	            /*  01: Transport
	                02: WorkMode
	                03: DeviceAddr
	                04: FilterTime
	                05: RFPower
	                06: BeepEnable
	                07: UartBaudRate*/
            bParamAddr = (byte) index;

            if (RFID.instance.CFHid_ReadDeviceOneParam((byte) 0xFF, bParamAddr, bValue)) {
                isContinue = false;
                value = String.valueOf(bValue[0]);
            }
        }
        return value;
    }


    boolean isContinueUpdateDeviceOneParam = true;
    @GetMapping("/rfidUpdateDeviceOneParam/{index}/{value}")
    public ResponseEntity rfidUpdateDeviceOneParam(@PathVariable int index , @PathVariable int value) {

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            public Object call() throws Exception{
                return rfidUpdateDeviceOneParamProcess(index, value);
            }
        };
        Future<Object> future = executor.submit(task);
        try {
            future.get(5, TimeUnit.SECONDS);
            return ResponseEntity.ok().body(new MessageResponse("Successfully updated parameters."));
        } catch (TimeoutException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error occurred. Please try again."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error occurred. Please try again."));
        } finally {
            future.cancel(true); // may or may not desire this
        }
    }

    public String rfidUpdateDeviceOneParamProcess(int index , int value) {

        isContinueUpdateDeviceOneParam = true;

        while (isContinueUpdateDeviceOneParam){
            byte bParamAddr = 0;
            byte[] bValue = new byte[2];

	            /*  01: Transport
	                02: WorkMode
	                03: DeviceAddr
	                04: FilterTime
	                05: RFPower
	                06: BeepEnable
	                07: UartBaudRate*/
            bParamAddr = (byte) index;
            bValue[0] = (byte) value;

            if (RFID.instance.CFHid_SetDeviceOneParam((byte) 0xFF, bParamAddr, bValue[0]) ) {
                isContinueUpdateDeviceOneParam = false;
            }
        }
        return "Dummy success";
    }

    @GetMapping("/rfidsetrfpower/{rfPower}")
    public ResponseEntity rfidSetRFPower(@PathVariable int rfPower) {

        byte bParamAddr = 0;
        byte[] bValue = new byte[2];

        bParamAddr = (byte) 0x05;
        bValue[0] = (byte) rfPower;

        if (RFID.instance.CFHid_SetDeviceOneParam((byte) 0xFF, bParamAddr, bValue[0]) == false) {
            logger.error("Failed");
            return ResponseEntity.badRequest().body("CFHid_ReadDeviceOneParam return false");
        }
        logger.info("Success");
        return ResponseEntity.ok().body("Successfully set RF Power to " + rfPower);
    }

    @GetMapping("/rfidreadfreq")
    public ResponseEntity rfidReadFrequency() {

        byte[] pFreq = new byte[2];
        if (RFID.instance.CFHid_ReadFreq((byte) 0xFF, pFreq) == false) {
            logger.error("Failed");
            return ResponseEntity.badRequest().body("CFHid_ReadFreq return false");
        }
        //pFreq[0]   pFreq[1]
        //0x31        0x80     //US Freq
        //0x4E        0x00     //Europe
        //0x2C        0xA3     //China
        //0x29        0x9D     //Korea
        //0x2E        0x9F     //Australia
        //0x4E        0x00     //New Zealand
        //0x4E        0x00     //India
        //0x2C        0x81     //Singapore
        //0x2C        0xA3     //Hongkong
        //0x31        0xA7     //Taiwan
        //0x31        0x80     //Canada
        //0x31        0x80     //Mexico
        //0x31        0x99     //Brazil
        //0x1C        0x99     //Israel
        //0x24        0x9D     //South Africa
        //0x2C        0xA3     //Thailand
        //0x28        0xA1     //Malaysia
        //0x29        0x9D     //Japan

        logger.info("Success\r\n");
        if (pFreq[0] == 0x31 && pFreq[1] == 0x80)
            return ResponseEntity.ok().header("Frequency", "US").build();
        else if (pFreq[0] == 0x4E && pFreq[1] == 0x00)
            return ResponseEntity.ok().header("Frequency", "Europe").build();
        else if (pFreq[0] == 0x2C && pFreq[1] == 0xA3)
            return ResponseEntity.ok().header("Frequency", "China").build();
        else if (pFreq[0] == 0x29 && pFreq[1] == 0x9D)
            return ResponseEntity.ok().header("Frequency", "Korea").build();
        else if (pFreq[0] == 0x2E && pFreq[1] == 0x9F)
            return ResponseEntity.ok().header("Frequency", "Australia").build();
        else
            return ResponseEntity.ok().header("Frequency", "US").build();
    }

    @GetMapping("/rfidsetfreq/{freqIndex}")
    public ResponseEntity rfidSetFreq(@PathVariable int freqIndex) {

        byte[] pFreq = new byte[2];
        byte bSelect = 0;
        bSelect = (byte) freqIndex;
        //pFreq[0]   pFreq[1]
        //0x31        0x80     //US Freq
        //0x4E        0x00     //Europe
        //0x2C        0xA3     //China
        //0x29        0x9D     //Korea
        //0x2E        0x9F     //Australia
        //0x4E        0x00     //New Zealand
        //0x4E        0x00     //India
        //0x2C        0x81     //Singapore
        //0x2C        0xA3     //Hongkong
        //0x31        0xA7     //Taiwan
        //0x31        0x80     //Canada
        //0x31        0x80     //Mexico
        //0x31        0x99     //Brazil
        //0x1C        0x99     //Israel
        //0x24        0x9D     //South Africa
        //0x2C        0xA3     //Thailand
        //0x28        0xA1     //Malaysia
        //0x29        0x9D     //Japan
        if (bSelect == 0) {
            pFreq[0] = (byte) 0x31;
            pFreq[1] = (byte) 0x80;
        } else if (bSelect == 1) {
            pFreq[0] = (byte) 0x4E;
            pFreq[1] = (byte) 0x00;
        } else if (bSelect == 2) {
            pFreq[0] = (byte) 0x2C;
            pFreq[1] = (byte) 0xA3;
        } else if (bSelect == 3) {
            pFreq[0] = (byte) 0x29;
            pFreq[1] = (byte) 0x9D;
        } else if (bSelect == 4) {
            pFreq[0] = (byte) 0x2E;
            pFreq[1] = (byte) 0x9F;
        }

        if (RFID.instance.CFHid_SetFreq((byte) 0xFF, pFreq) == false) {
            logger.error("Failed");
            return ResponseEntity.badRequest().body("CFHid_SetFreq return false");
        }
        logger.info("Success");
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/rfidopensensor")
    public ResponseEntity rfidIsStartRead() {

        if (RFID.instance.CFHid_StartRead((byte) 0xFF) == false) {
            logger.error("Failed");
            return ResponseEntity.badRequest().body("CFHid_StartRead return false");
        }
        logger.info("Success");
        return ResponseEntity.ok().body("Start all RF reading successfully");
    }

    @GetMapping("/rfidclosesensor")
    public ResponseEntity rfidIsStopRead() {

        if (RFID.instance.CFHid_StopRead((byte) 0xFF) == false) {
            logger.error("Failed");
            return ResponseEntity.badRequest().body("CFHid_StopRead return false");
        }
        logger.info("Success");
        return ResponseEntity.ok().body("Stop all RF reading successfully");
    }


    private String scanTagProcess() {

        boolean isContinue = true ;
        String epc = "";
        while (isContinue) {

            byte[] arrBuffer = new byte[40960];
            int[] iNum = new int[2];
            int[] iTotalLen = new int[2];

            RFID.instance.CFHid_GetTagBuf(arrBuffer, iTotalLen, iNum);

            int iTagNumber = 0;
            iTagNumber = iNum[0];

            int iIndex = 0;
            int iLength = 0;
            byte bPackLength = 0;
            int iIDLen = 0;
            int i = 0;

            for (iIndex = 0; iIndex < iTagNumber; iIndex++) {
                bPackLength = arrBuffer[iLength];
                String str2 = "";
                String str1 = "";
                str1 = String.format("%02X", arrBuffer[1 + iLength + 0]);
                if ((arrBuffer[1 + iLength + 0] & 0x80) == 0x80)  // with TimeStamp , last 6 bytes is time
                {
                    iIDLen = bPackLength - 7;
                } else iIDLen = bPackLength - 1;


                String str3 = "";
                for (i = 2; i < iIDLen; i++) {
                    str1 = String.format("%02X", arrBuffer[1 + iLength + i]);
                    str3 = str3 + str1 + "";
                }
                str2 = str2 + str3;

                epc = str2;
                isContinue = false;
            }
        }
        logger.info("Found Tag : "+epc.toUpperCase());
        return epc.toUpperCase();
    }



}

/*


    @GetMapping(value = "/receiveRFIDMultipleResult", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getItemsStream(){

        Flux<String> newFlux = Flux
                .generate(sink -> {
                    String element = rfidMultipleScanBlockingQueue.peek();
                    if (element == null) {
                        sink.complete();
                    } else {
                        sink.next(element);
                        rfidMultipleScanBlockingQueue.poll();
                    }
                });

        return newFlux.delayElements(Duration.ofSeconds(1))
                .repeat()
                .log();
    }*/
