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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

        boolean isContinueRFIDScan = true;

        while (isContinueRFIDScan) {
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
                        logger.info("Success\r\n");

                        //set rf power to 30
                        ;
                        byte bParamAddr = 0;
                        byte[] bValue = new byte[2];
                        bParamAddr = (byte) 0x05;
                        bValue[0] = (byte) Integer.parseInt(rfidRfPower);
                        logger.info("RFID RfPower :" + rfidRfPower);

                        if (RFID.instance.CFHid_SetDeviceOneParam((byte) 0xFF, bParamAddr, bValue[0]) == false) {
                            logger.error("CFHid_ReadDeviceOneParam return false.. Looping");
                            //return ResponseEntity.badRequest().body(new MessageResponse("CFHid_ReadDeviceOneParam return false"));
                        }else {
                            isContinueRFIDScan = false;
                            logger.info("Successfully initialized Card Reader");
                        }
                        //return ResponseEntity.ok().body(new MessageResponse("Successfully initialized Card Reader"));
                    }
                    if (isContinueRFIDScan) {
                        logger.error("Unable to CFHid_GetDeviceSystemInfo.. Looping");
                    }
                    //return ResponseEntity.badRequest().body(new MessageResponse("Unable to initialized RFID Card Reader"));
                }
            }
            isContinueRFIDScan = false;
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to detect Card Reader in USB. Please re-connect again"));
        }
        return ResponseEntity.ok().body(new MessageResponse("Successfully initialized Card Reader"));
    }

    @GetMapping("/mockRfidOpen")
    public String mockRfidOpen() {

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
                    logger.info("Success\r\n");

                    //set rf power to 30
                    ;
                    byte bParamAddr = 0;
                    byte[] bValue = new byte[2];
                    bParamAddr = (byte) 0x05;
                    bValue[0] = (byte) Integer.parseInt(rfidRfPower);
                    logger.info("RFID RfPower :"+rfidRfPower);

                    if (RFID.instance.CFHid_SetDeviceOneParam((byte) 0xFF, bParamAddr, bValue[0]) == false) {
                        logger.error("Failed");
                        //return ResponseEntity.badRequest().body("CFHid_ReadDeviceOneParam return false");
                        return "CFHid_ReadDeviceOneParam return false";
                    }

                    //return new ResponseEntity<String>("Successfully initialized RFID Card Reader", responseHeaders, HttpStatus.OK);
                    return "Successfully initialized RFID Card Reader";

                } else logger.error("Failed");
                //return ResponseEntity.badRequest().body("Unable to initialized RFID Card Reader");
                return "Unable to initialized RFID Card Reader";
            }
        } else logger.error("Failed");
        //return ResponseEntity.badRequest().body("Please insert RFID Card Reader to USB");
        return "Please insert RFID Card Reader to USB";
    }



    @GetMapping("/rfidclose")
    public ResponseEntity rfidClose() {

        logger.error("isContinueReadDeviceOneParam    "+isContinueReadDeviceOneParam.get());

        isContinueReadDeviceOneParam.set(false);
        atomicIntegerCounter.set(0);
        RFID.instance.CFHid_CloseDevice();

        logger.info("Close RFID Card Reader.");

        return ResponseEntity.ok().body(new MessageResponse("Close RFID Card Reader successfully"));
    }



    @GetMapping("/rfidscan")
    public ResponseEntity rfidScan()  {

        try {

            String epc = "";
            boolean isContinueRFIDScan = true;
            while (isContinueRFIDScan) {

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
                    isContinueRFIDScan = false;
                }
            }

            //break out of while loop and sent back the transponder details with epc
            Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndRowRecordStatus(epc.toUpperCase() , "VALID");
            if (transponderOptional.isPresent()){
                return ResponseEntity.ok().body(transponderOptional.get());
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("EPC not registered with any transponder. Please register EPC wih transponder."));
            }

        } catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Exception error occurred. Please re-connect card reader again."));
        }
    }

    int counter =0;
    public static BlockingQueue<String> rfidMultipleScanBlockingQueue = new LinkedBlockingDeque<>();

    @GetMapping("/rfidscantagMuliple/{numberOfTimes}")
    public void rfidScanTag(@PathVariable int numberOfTimes) throws Exception{

        while(numberOfTimes != 0) {
            byte[] arrBuffer = new byte[40960];
            int[] iNum = new int[2];
            int[] iTotalLen = new int[2];
            byte bRet = 0;

            while (bRet == 0) {
                bRet = RFID.instance.CFHid_GetTagBuf(arrBuffer, iTotalLen, iNum);
            }
            logger.info(String.valueOf(bRet));

            int iTagLength = 0;
            int iTagNumber = 0;
            iTagLength = iTotalLen[0];
            iTagNumber = iNum[0];
            if (iTagNumber == 0) {
                continue;
            }
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
                str2 = str2 + "Type:" + str1 + " ";  //Tag Type

                str1 = String.format("%02X", arrBuffer[1 + iLength + 1]);
                str2 = str2 + "Ant:" + str1 + " Tag:";  //Ant

                String str3 = "";
                for (i = 2; i < iIDLen; i++) {
                    str1 = String.format("%02X", arrBuffer[1 + iLength + i]);
                    str3 = str3 + str1 + "";
                }
                str2 = str2 + str3;
                str1 = String.format("%02X", arrBuffer[1 + iLength + i]);
                str2 = str2 + "RSSI:" + str1 ;  //RSSI
                iLength = iLength + bPackLength + 1;
                logger.info(str2);
                numberOfTimes--;
                rfidMultipleScanBlockingQueue.put("Scanning " +str2 +" left:"+numberOfTimes);



            }
        }
        rfidMultipleScanBlockingQueue.put("Successfully completed scanning "+numberOfTimes+" times");
    }

    @GetMapping("/AsyncRfidScanTagMultiple/{numberOfTimes}")
    public void rfidScanTagMultiple(@PathVariable int numberOfTimes) throws InterruptedException {
        service.RfidScanMultiple(numberOfTimes);
    }

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
    }

    static volatile AtomicBoolean isContinueReadDeviceOneParam = new AtomicBoolean(true);
    static volatile AtomicInteger atomicIntegerCounter = new AtomicInteger(0);
    @GetMapping("/rfidreaddeviceoneparam/{index}")
    public ResponseEntity rfidReadDeviceOneParam(@PathVariable int index) throws Exception{

        String returnValue = "";

        isContinueReadDeviceOneParam.set(true);

        while (isContinueReadDeviceOneParam.get()){

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


            if (atomicIntegerCounter.get() == 500000){
                isContinueReadDeviceOneParam.set(false);
                return ResponseEntity.badRequest().body(new MessageResponse("An error occured. Please close and initialize Card Reader again."));
            }
            if (RFID.instance.CFHid_ReadDeviceOneParam((byte) 0xFF, bParamAddr, bValue) == false) {
                logger.error("Failed and loop at reading device one param "+this.atomicIntegerCounter);
                atomicIntegerCounter.getAndIncrement();
            } else {
                isContinueReadDeviceOneParam.set(false);
                logger.info(String.valueOf(bValue[0]));
                returnValue = String.valueOf(bValue[0]);
            }
        }
        return ResponseEntity.ok().body(new MessageResponse(returnValue));
    }


    boolean isContinueUpdateDeviceOneParam = true;
    @GetMapping("/rfidupdatedeviceoneparam/{index}/{value}")
    public void rfidUpdateDeviceOneParam(@PathVariable int index , @PathVariable int value) {

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

            if (RFID.instance.CFHid_SetDeviceOneParam((byte) 0xFF, bParamAddr, bValue[0]) == false) {
                logger.error("Failed");
            } else {
                logger.info("Success");
                isContinueUpdateDeviceOneParam = false;
            }
        }
    }

    @GetMapping("/rfidsetrfpower/{rfPower}")
    public ResponseEntity rfidSet(@PathVariable int rfPower) {

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
    public ResponseEntity rfidRead_1() {

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



}
