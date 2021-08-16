package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_EPC_ACTIVE;
import com.ncs.airside.model.database.RT_EPC_PASSIVE;
import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import com.ncs.airside.model.helper.READER_ERROR_CODE;
import com.ncs.airside.repository.RT_EPC_ACTIVE_REPO;
import com.ncs.airside.repository.RT_EPC_PASSIVE_REPO;
import com.ncs.airside.repository.RT_TRANSPONDER_STATUS_REPO;
import com.ncs.airside.service.AsyncService;
import com.rfid.uhf.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class AntennaController {

    @Autowired
    public Device reader;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RT_TRANSPONDER_STATUS_REPO rt_transponder_STATUS_repo;

    @Autowired
    private RT_EPC_ACTIVE_REPO rt_epc_active_repo;

    @Autowired
    private READER_ERROR_CODE error_code;

    @Value("${airside.rfid.antennapower}")
    private String antennaRfPower;

    private Logger logger = LoggerFactory.getLogger(AntennaController.class);

    static boolean isLoadAntennaLibrary = false;

    /*
    Target reader address.
     In the case when reader address is unknown, the broadcasting address 0xFF can be used to initialise the serial connection.
     Once the function is called successfully, the actual address of the reader will be returned as the value of this parameter.
    */
    public static byte[] comAddr = new byte[1];
    /*
    Reader connected handle will be returned as the value of this parameters.
    The value of handle will be used in all the future API operation.
    */
    public static int[] PortHandle = new int[1];

    @GetMapping("/AsyncAntennaPassiveStartscan") //First Step
    public void asyncPassiveAntennaCall() throws InterruptedException {
        asyncService.AntennaPassiveStartScan();
    }

    @GetMapping("/AsyncAntennaActiveStartscan") //First Step
    public void asyncActiveAntennaCall() throws InterruptedException {
        asyncService.AntennaActiveStartScan();
    }

    @GetMapping("/setportnumber/{portNumber}")
    public ResponseEntity setPortNumber(@PathVariable int portNumber) {
        return ResponseEntity.ok().body("Successfully Set portNumber " + portNumber);
    }

    @GetMapping("/getRfPower")
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

    @GetMapping("/getVersionInfo")
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


    @GetMapping("/getSerialNumber")
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

    @GetMapping("/getserialnumber/{antennaNumber}")
    public ResponseEntity getSerialNumber(@PathVariable int antennaNumber) {

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
            logger.error(error_code.getErrorCode(result));
            return ResponseEntity.badRequest().body("Unable to get Reader Information ");
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

    @GetMapping("/openAntenna/{portNumber}")
    public ResponseEntity connectAntenna (@PathVariable int portNumber) {

        if (!isLoadAntennaLibrary) {
            System.loadLibrary("com_rfid_uhf_Device"); //read from resources folder
            logger.info("Library com_rfid_uhf_Device loaded");
            isLoadAntennaLibrary = true;
        } else {
            logger.info("Library com_rfid_uhf_Device Not Loaded as already loaded once.");
        }

        byte baud = 5;//57600bps

        this.comAddr[0]=(byte)255;

        int resultComPort = reader.OpenComPort(portNumber, this.comAddr, baud, this.PortHandle);

        if (resultComPort == 0) {
            logger.info("Successfully initialized Antenna with comPort "+portNumber);
            return ResponseEntity.ok().body(new MessageResponse("Successfully initialized Antenna with comPort "+portNumber));
        } if (resultComPort == 48) {
            return ResponseEntity.badRequest().body(new MessageResponse("Inventory G2 Hub not detected. Please try again"));
        }else {
            logger.error(resultComPort+" - "+error_code.getErrorCode(resultComPort));
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to initialized Antenna. Please try again."));
        }
    }

    @GetMapping("/closeAntenna")
    public ResponseEntity closeAntenna () {

        int result = reader.CloseSpecComPort(this.PortHandle[0]);

        logger.info(String.valueOf(result));
        if (result == 0){
            return ResponseEntity.ok().body(new MessageResponse("Disconnect antenna successfully"));
        }if (result == 55){
            return ResponseEntity.ok().body(new MessageResponse("Antenna may have been disconnected. Please connect instead"));
        }else {
            logger.error(error_code.getErrorCode(result));
            return ResponseEntity.badRequest().body(new MessageResponse("Error occurred : "+error_code.getErrorCode(result)));
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
