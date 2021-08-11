package com.ncs.airside.controller;

import com.ncs.airside.Exception.SoundAlertException;
import com.ncs.airside.helper.SoundUtils;
import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_EPC_ALERT;
import com.ncs.airside.model.database.RT_TRANSPONDER_BORROW;
import com.ncs.airside.model.rfid.RfidResponse;
import com.ncs.airside.repository.RT_EPC_ALERT_REPO;
import com.ncs.airside.repository.RT_TRANSPONDER_BORROWER_REPO;
import com.ncs.airside.service.AsyncService;
import com.rfid.uhf.Device;
import com.syc.function.Function;
import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class AntennaController {

    @Autowired
    public Device reader;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RT_TRANSPONDER_BORROWER_REPO rt_transponder_borrower_repo;

    @Autowired
    private  RT_EPC_ALERT_REPO rt_epc_alert_repo ;

    @Value("${airside.rfid.antennapower}")
    private String antennaRfPower;

    private Logger logger = LoggerFactory.getLogger(AntennaController.class);

    static boolean isLoadAntennaLibrary = false;

    /*
    Target reader address.
     In the case when reader address is unknown, the broadcasting address 0xFF can be used to initialise the serial connection.
     Once the function is called successfully, the actual address of the reader will be returned as the value of this parameter.
    */
    byte[] comAddr = new byte[1];
    /*
    Reader connected handle will be returned as the value of this parameters.
    The value of handle will be used in all the future API operation.
    */
    int[] PortHandle = new int[1];

    @GetMapping("/asyncantennastartscan") //First Step
    public void asyncAntennaCall() throws InterruptedException {
        asyncService.AntennaStartScan();
    }

    @GetMapping("/setportnumber/{portNumber}")
    public ResponseEntity setPortNumber(@PathVariable int portNumber) {
        return ResponseEntity.ok().body("Successfully Set portNumber " + portNumber);
    }



    @GetMapping("/getrfpower")
    public ResponseEntity getRfPower() {

        byte[]versionInfo=new byte[2];
        byte[]readerType=new byte[1];
        byte[]trType=new byte[1];
        byte[]dmaxfre=new byte[1];
        byte[]dminfre=new byte[1];
        byte[]powerdBm=new byte[1];
        byte[]InventoryScanTime=new byte[1];
        byte[]Ant=new byte[1];
        byte[]BeepEn=new byte[1];
        byte[]OutputRep=new byte[1];
        byte[]CheckAnt=new byte[1];
        int result = reader.GetReaderInformation(this.comAddr, versionInfo, readerType, trType, dmaxfre, dminfre, powerdBm, InventoryScanTime,
                Ant, BeepEn, OutputRep, CheckAnt, this.PortHandle[0]);

        if (result ==  0) {
            logger.info("Successfully obtain RF power");
            return ResponseEntity.ok().body(new MessageResponse(String.valueOf(powerdBm[0])));
        } else {
            logger.info("Unable to get RF power  ");
            return ResponseEntity.badRequest().body("Unable to get RF Power");
        }
    }


    @GetMapping("/getbeepstatus")
    public ResponseEntity getBeepStatus() {

        byte[]versionInfo=new byte[2];
        byte[]readerType=new byte[1];
        byte[]trType=new byte[1];
        byte[]dmaxfre=new byte[1];
        byte[]dminfre=new byte[1];
        byte[]powerdBm=new byte[1];
        byte[]InventoryScanTime=new byte[1];
        byte[]Ant=new byte[1];
        byte[]BeepEn=new byte[1];
        byte[]OutputRep=new byte[1];
        byte[]CheckAnt=new byte[1];
        int result = reader.GetReaderInformation(this.comAddr, versionInfo, readerType, trType, dmaxfre, dminfre, powerdBm, InventoryScanTime,
                Ant, BeepEn, OutputRep, CheckAnt, this.PortHandle[0]);

        if (result ==  0) {
            logger.info("Successfully get beep status"+BeepEn[0]);
            return ResponseEntity.ok().body(new MessageResponse(String.valueOf(BeepEn[0])));
        } else {
            logger.info("Unable to get Beep status");
            return ResponseEntity.badRequest().body("Unable to get Beep Status");
        }
    }

    @GetMapping("/getversioninfo")
    public ResponseEntity getVersionInfo() {

        byte[]versionInfo=new byte[2];
        byte[]readerType=new byte[1];
        byte[]trType=new byte[1];
        byte[]dmaxfre=new byte[1];
        byte[]dminfre=new byte[1];
        byte[]powerdBm=new byte[1];
        byte[]InventoryScanTime=new byte[1];
        byte[]Ant=new byte[1];
        byte[]BeepEn=new byte[1];
        byte[]OutputRep=new byte[1];
        byte[]CheckAnt=new byte[1];
        int result = reader.GetReaderInformation(this.comAddr, versionInfo, readerType, trType, dmaxfre, dminfre, powerdBm, InventoryScanTime,
                Ant, BeepEn, OutputRep, CheckAnt, this.PortHandle[0]);

        String versionInfoStr = getHexString(versionInfo) ;

        if (result ==  0) {
            logger.info(versionInfoStr);
            return ResponseEntity.ok().body(new MessageResponse(versionInfoStr));
        } else {
            logger.info("Unable to get version info status");
            return ResponseEntity.badRequest().body("Unable to get version info");
        }
    }


    @GetMapping("/getserialnumber")
    public ResponseEntity getSerialNumber() {

        byte[] serialNoArray = new byte[4];

        int result = reader.GetSeriaNo(this.comAddr, serialNoArray , this.PortHandle[0]);

        String serialNumberStr = getHexString(serialNoArray);

        if (result ==  0) {
            logger.info("Successfully get Serial No. " + serialNumberStr);
            return ResponseEntity.ok().body(new MessageResponse(serialNumberStr));
        } else {
            logger.info("Unable to get Serial No. " + serialNumberStr);
            return ResponseEntity.badRequest().body("Unable to get Serial No. Please try again");
        }
    }

    @GetMapping("/getdrm")
    public ResponseEntity getDRM() {

        byte[] drmArray = new byte[1];

        int result = reader.GetDRM(this.comAddr, drmArray , this.PortHandle[0]);

        String drmStr = getHexString(drmArray);

        if (result ==  0) {
            logger.info("Successfully get Serial No. " + drmStr);
            return ResponseEntity.ok().body(new MessageResponse(drmStr));
        } else {
            logger.info("Unable to get Serial No. " + drmStr);
            return ResponseEntity.badRequest().body("Unable to get DRM. Please try again");
        }
    }

    @GetMapping("/setdrm/{value}")
    public void setDRM(@PathVariable int value) {

        /*
        0 - disable
        1 - enable
         */
        boolean isContinue = true;

        while (isContinue){

            if (reader.SetDRM(this.comAddr, (byte) value , this.PortHandle[0]) != 0) {
                logger.error("Fail to update DRM "+value);
            } else {
                logger.info("Successfully update DRM to "+value);
                isContinue = false;
            }
        }
    }

    @GetMapping("/setrfpower/{value}") //only can call when not scanning continously
    public void setRfPower(@PathVariable int value) {

      /*
        1 byte, reader RF power.
        The range is 0 ~ 30 and the unit is dBm.
        */
        boolean isContinue = true;

        while (isContinue){

            if (reader.SetRfPower(this.comAddr, (byte) value, this.PortHandle[0]) != 0) {
                logger.error("Fail to update RF Power "+value);
            } else {
                logger.info("Successfully update RF Power to "+value);
                isContinue = false;
            }
        }
    }

    @GetMapping("/setbeepstatus/{value}")
    public void setBeepStatus(@PathVariable int value) {

        boolean isContinue = true;

        while (isContinue){

            if (reader.SetBeepNotification(this.comAddr, (byte) value,this.PortHandle[0]) != 0) {
                logger.error("Fail to update beep status "+value);
            } else {
                logger.info("Successfully update Beep status to "+value);
                isContinue = false;
            }
        }
    }


    @GetMapping("/getreaderinformation")
    public ResponseEntity getReaderInformation() {

        byte[]versionInfo=new byte[2];
        byte[]readerType=new byte[1];
        byte[]trType=new byte[1];
        byte[]dmaxfre=new byte[1];
        byte[]dminfre=new byte[1];
        byte[]powerdBm=new byte[1];
        byte[]InventoryScanTime=new byte[1];
        byte[]Ant=new byte[1];
        byte[]BeepEn=new byte[1];
        byte[]OutputRep=new byte[1];
        byte[]CheckAnt=new byte[1];
        int result = reader.GetReaderInformation(this.comAddr, versionInfo, readerType, trType, dmaxfre, dminfre, powerdBm, InventoryScanTime,
                Ant, BeepEn, OutputRep, CheckAnt, this.PortHandle[0]);

        if (result ==  0) {
            logger.info("Successfully ");
            return ResponseEntity.ok().body("Successfully  ");
        } else {
            logger.info("Unable to get  ");
            return ResponseEntity.badRequest().body("Unable to get ");
        }
    }

    @GetMapping("/getreadertemperature")
    public ResponseEntity getReaderTemperature() {

        byte[] plusMinus=new byte[1];
        byte[] temperature=new byte[1];

        int result = reader.GetReaderTemperature(this.comAddr, plusMinus ,temperature,this.PortHandle[0]);

        if (result ==  0) {
            logger.info("Successfully obtain temperature");
            return ResponseEntity.ok().body(new MessageResponse(String.valueOf(temperature[0])));
        } else {
            logger.info("Unable to get Antenna temperature ");
            return ResponseEntity.badRequest().body("Unable to get temperature");
        }
    }

    @GetMapping("/loadantennalibrary")
    public ResponseEntity loadAntennaLibrary () {
        System.loadLibrary("com_rfid_uhf_Device");
        return ResponseEntity.ok().body(new MessageResponse("Successfully load library"));
    }

    @GetMapping("/openantenna/{portNumber}")
    public ResponseEntity openAntenna (@PathVariable int portNumber) {

        if (!isLoadAntennaLibrary) {
            System.loadLibrary("com_rfid_uhf_Device"); //read from resources folder
            logger.info("Library com_rfid_uhf_Device loaded");
            isLoadAntennaLibrary = true;
        } else {
            logger.info("Library com_rfid_uhf_Device Not Loaded as already loaded once.");
        }

        logger.info("The portNumber is " + portNumber);

        byte baud = 5;//57600bps

        this.comAddr[0]=(byte)255;

        int resultComPort = reader.OpenComPort(portNumber, this.comAddr, baud, this.PortHandle);

        if (resultComPort == 0) {
            logger.info("Successfully initialized Antenna with comPort "+portNumber);
            return ResponseEntity.ok().body(new MessageResponse("Successfully initialized Antenna with comPort "+portNumber));
        }else {
            logger.error("Unable to initialize Antenna with comPort "+portNumber +" .Please try again");
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to initialized Antenna. Please try again."));
        }
    }

    private volatile AtomicBoolean isContinuousScan = new AtomicBoolean(true);


    @GetMapping("/antennastartcontinouosscan")
    public void antennaStartContinuousScan () {

        isContinuousScan.set(true);

        while (this.isContinuousScan.get()) {

            int counter = 0;

            /*
                1 byte.
                        bit7: Statistic data packet flag;
                0 – after inventory，DO NOT deliver statistic data packet of inventory process;
                1 – after inventory，deliver statistic data packet of inventory process.
                        bit5: FastID inventory indicator.
                        This function is used to rapidly obtain tag EPC and TID, only supported by some tags from the Impinj Monza series;
                0 – disable;
                1 – enable.
                        bit4 ~ bit0: the original Q-value of EPC tag inventory.
                NOTE:
                1. The setting of Q-value should follow the rule: 2Q ≈ total amount of tags within the effective field. The range of Q-value is 0 ~ 15, if other value is delivered in this field, reader will return a parameter error status in the response frame.
                2. Inventory with statistic data packet is SLOWER than inventory without statistic data packet.
            */
            byte QValue = 4;
            /*
                1 byte, the Session-value of EPC tag inventory.
                0x00 – apply S0 as Session value;
                0x01 – apply S1 as Session value;
                0x02 – apply S2 as Session value;
                0x03 – apply S3 as Session value.
                0xff – apply reader smart configuration (only valid in EPC inventory).
                Inventory for single tag or small amount of tag, S0 is the recommended setting.
             */
            byte Session = 0;
            /*
                1 byte, mask area indication.
                0x01 – EPC memory;
                0x02 – TID memory;
                0x03 – User memory.
             */
            byte MaskMem = 2;
            /*
                2 bytes, entry bit address of the mask.
                The valid range of MaskAdr is 0 ~ 16383.
              */
            byte[] MaskAdr = new byte[2];
            /* 1 byte, bit length of mask (unit: bits).*/
            byte MaskLen = 0;
            /*
                N bytes, mask data.
                N = MaskLen/8. If MaskLen is not a multiple of 8 integer, N= int[MaskLen/8]+1.
                Non-specified lower significant figures should be filled up with 0.
            */
            byte[] MaskData = new byte[256];
            /*
                1 byte, mask flag.
                0 – disable mask;
                1 – enable mask.
            */
            byte MaskFlag = 0;
            /*
                1 byte, entry address of TID memory inventory.
            */
            byte AdrTID = 0;
            /*
                1 byte, data length for TID inventory operation, the valid range of LenTID is 0 ~ 15.
            */
            byte LenTID = 6;
            /*
                1 byte, inventory purpose indicator.
                0 – EPC inventory;
                1 – TID inventory.
            */
            byte TIDFlag = 1;
            /*
                1 byte, the Target value of EPC tag inventory.
                0x00 – apply Target A;
                0x01 – apply Target B.
            */
            byte Target = 0;
            /*
                1 byte, antenna selection for the current inventory.
                0x80 – antenna 1;   0x81 – antenna 2;
                0x82 – antenna 3;   0x83 – antenna 4.
                InAnt is 0x08 for single port reader.
            */
            byte InAnt = (byte) 0x80;
            /*
                1 byte, the maximum operation time for inventory.
                The valid range of Scantime is 0 ~ 255,corresponding to (0 ~ 255)*100ms.
                 For Scantime = 0, operation time is not limited.
            */
            byte Scantime = 10;
            /*
                1 byte, express inventory indicator
                0 – disable express inventory, Target, InAnt and Scantime are NOT essential, can be set to default value 0;
                1 – enable express inventory, Target, InAnt and Scantime are need to be defined.
            */
            byte FastFlag = 0;
            /*
                The inquired tag data, the data block follows the format stated below:
                EPC/TID length + EPC/TID No. + RSSI.
                Data of multiple tags is formed by several identical data blocks in sequence.
            */
            byte[] pEPCList = new byte[20000];
            /*
                4 antenna ports reader:
                Every bit in Ant represents one corresponding antenna. For example, 0x04 is 0000 0100 in binary. This indicates Antenna 3 had inquired this specific tag.
                12 antenna ports reader
                Ant value 0 ~ 11 corresponding to Antenna 1 to Antenna 12. For example, Ant = 0 represents Antenna 1 had inquired this specific tag.
            */
            byte[] Ant = new byte[1];
            /*
                The total length of the received data stored in pEPCList.
            */
            int[] Totallen = new int[1];
            /*
                The total amount of tag inquired during the current inventory.
            */
            int[] CardNum = new int[1];

            int result = reader.Inventory_G2(this.comAddr, QValue, Session, MaskMem, MaskAdr, MaskLen, MaskData, MaskFlag,
                    AdrTID, LenTID, TIDFlag, Target, InAnt, Scantime, FastFlag, pEPCList, Ant, Totallen,
                    CardNum, PortHandle[0]);

            String EPCMemData = "";

            if(CardNum[0]>0)
            {
                System.out.println();
                System.out.println();
                int m=0;
                for(int index=0;index<CardNum[0];index++)
                {
                    int epclen = pEPCList[m++]&255;
                    String EPCstr="";
                    byte[]epc = new byte[epclen];
                    for(int n=0;n<epclen;n++)
                    {
                        byte bbt = pEPCList[m++];
                        epc[n] = bbt;
                        String hex= Integer.toHexString(bbt& 255);
                        if(hex.length()==1)
                        {
                            hex="0"+hex;
                        }
                        EPCstr+=hex;
                    }
                    int rssi = pEPCList[m++];
                    //¸ù¾ÝTIDºÅÐ´Êý¾Ý
                    byte ENum=(byte)255;//ÑÚÂë
                    byte Mem=1;//¶ÁEPC
                    byte WordPtr=2;//´ÓµÚ2×Ö¿ªÊ¼
                    byte Num=6;//¶Á6¸ö×Ö
                    byte[]Password=new byte[4];
                    MaskMem=2;//TIDÑÚÂë
                    MaskAdr[0]=0;
                    MaskAdr[1]=0;
                    MaskLen=96;
                    int p=0;
                    System.arraycopy(epc,0,MaskData,0,96/8);
                    byte[]Data=new byte[Num*2];
                    int[]Errorcode=new int[1];
                    byte WNum=7;
                    byte[]Wdt=new byte[WNum*2];
                    Wdt[0]=0x30;
                    Wdt[1]=0x00;
                    Wdt[2]=(byte)0xE2;
                    Wdt[3]=0x00;
                    Wdt[4]=0x12;
                    Wdt[5]=0x34;
                    Wdt[6]=0x56;
                    Wdt[7]=0x78;
                    Wdt[8]=0x12;
                    Wdt[9]=0x34;
                    Wdt[10]=0x56;
                    Wdt[11]=0x78;
                    Wdt[12]=0x12;
                    Wdt[13]=0x34;
                    WordPtr=1;
                    WordPtr=2;

                    result = reader.ReadData_G2(comAddr,epc,ENum,Mem,WordPtr,Num,Password,
                            MaskMem,MaskAdr, MaskLen,MaskData,Data,Errorcode,PortHandle[0]);

                    if(result==0)
                    {
                        String Memdata="";
                        for( p=0;p<Num*2;p++)
                        {
                            byte bbt = Data[p];
                            String hex= Integer.toHexString(bbt& 255);
                            if(hex.length()==1)
                            {
                                hex="0"+hex;
                            }
                            Memdata+=hex;
                        }

                        EPCMemData = Memdata.toUpperCase() ;
                    }

                    Optional<RT_TRANSPONDER_BORROW> transponderBorrowerOptional = rt_transponder_borrower_repo.findByEPCAndRowRecordStatus(EPCMemData, "VALID");
                    Optional<RT_EPC_ALERT> epcAlertOptional = rt_epc_alert_repo.findByEpcAndRowRecordStatus(EPCMemData , "VALID");

                    if (!transponderBorrowerOptional.isPresent() && !epcAlertOptional.isPresent() && !EPCMemData.equals("")){

                        RT_EPC_ALERT epc_alert = new RT_EPC_ALERT();
                        epc_alert.setEpc(EPCMemData);
                        epc_alert.setTimestamp(LocalDateTime.now());
                        epc_alert.setRowRecordStatus("VALID");
                        rt_epc_alert_repo.save(epc_alert) ;
                        logger.info("Inserted to EPC Alert table "+EPCMemData);

                        if (!asyncService.isAlarmTriggerAtomic.get()) {
                            asyncService.isAlarmTriggerAtomic.set(true);
                            asyncService.toggleSoundAlert();
                        }
                    }
                }
            }
        } //end of while loop
    }

    @GetMapping("/antennastopcontinuousscan")
    public void stopContinuousScan () {
        System.out.println("SETTING IT OFF");
        this.isContinuousScan.set(false);
    }

    @GetMapping("/closeantenna")
    public ResponseEntity closePort () {
        int result = reader.CloseSpecComPort(this.PortHandle[0]);

        if (result == 0){
            return ResponseEntity.ok().body(new MessageResponse("Close antenna ComPort Successfully"));
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to close ComPort. Please try again"));
        }

    }

    String getHexString(byte[] temp){
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < temp.length ; i++){
            sb.append(Integer.toHexString(temp[i] & 255));
        }
        return sb.toString() ;
    }
}
