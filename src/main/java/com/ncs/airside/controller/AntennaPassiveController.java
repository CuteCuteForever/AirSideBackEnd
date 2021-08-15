package com.ncs.airside.controller;

import com.ncs.airside.helper.Alarm;
import com.ncs.airside.model.database.RT_EPC_PASSIVE;
import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import com.ncs.airside.repository.RT_EPC_PASSIVE_REPO;
import com.ncs.airside.repository.RT_TRANSPONDER_STATUS_REPO;
import com.ncs.airside.service.AsyncService;
import com.rfid.uhf.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ncs.airside.controller.AntennaController.PortHandle;
import static com.ncs.airside.controller.AntennaController.comAddr;

@RestController
public class AntennaPassiveController {

    @Autowired
    private Alarm alarm ;

    @Autowired
    private RT_EPC_PASSIVE_REPO rt_epc_passive_repo;

    @Autowired
    private RT_TRANSPONDER_STATUS_REPO rt_transponder_STATUS_repo;

    @Autowired
    public Device reader;

    private volatile AtomicBoolean isPassiveContinuousScan = new AtomicBoolean(true);

    private Logger logger = LoggerFactory.getLogger(AntennaPassiveController.class);

    @GetMapping("/antennaPassiveStartContinuousScan")
    public void antennaPassiveStartContinuousScan () {

        isPassiveContinuousScan.set(true);

        while (this.isPassiveContinuousScan.get()) {

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

            int result = reader.Inventory_G2( comAddr, QValue, Session, MaskMem, MaskAdr, MaskLen, MaskData, MaskFlag,
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
                            MaskMem,MaskAdr, MaskLen,MaskData,Data,Errorcode, PortHandle[0]);

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

                    Optional<RT_TRANSPONDER_STATUS> transponderStatusOptional_RENT_OUT = rt_transponder_STATUS_repo.findByEPCAndTransponderStatusAndRowRecordStatus(EPCMemData, "Rent Out" , "VALID");
                    Optional<RT_TRANSPONDER_STATUS> transponderStatusOptional_REPAIR = rt_transponder_STATUS_repo.findByEPCAndTransponderStatusAndRowRecordStatus(EPCMemData, "Repair" , "VALID");
                    Optional<RT_EPC_PASSIVE> epcAlertOptional = rt_epc_passive_repo.findByEPCAndRowRecordStatus(EPCMemData , "valid");

                    if (!transponderStatusOptional_RENT_OUT.isPresent() &&!transponderStatusOptional_REPAIR.isPresent()  && !epcAlertOptional.isPresent() && !EPCMemData.equals("")){

                        RT_EPC_PASSIVE rt_epc_passive = new RT_EPC_PASSIVE();
                        rt_epc_passive.setEPC(EPCMemData);
                        rt_epc_passive.setTimestamp(LocalDateTime.now());
                        rt_epc_passive.setRowRecordStatus("valid");
                        rt_epc_passive_repo.save(rt_epc_passive) ;
                        logger.info("Inserted to EPC Passive table "+rt_epc_passive.toString());

                       /* if (!asyncService.isAlarmTriggerAtomic.get()) {
                            asyncService.isAlarmTriggerAtomic.set(true);
                            asyncService.toggleSoundAlert();
                        }*/
                    }
                }
            }
        } //end of while loop
    }

    Timer timerPassive;
    boolean isAntennaOneP;
    boolean isAntennaTwoP;
    boolean isAntennaThreeP;
    boolean isAntennaFourP;
    @GetMapping("/passiveScan/{isAntennaOne}/{isAntennaTwo}/{isAntennaThree}/{isAntennaFour}")
    public void passiveScan (@PathVariable boolean isAntennaOne , @PathVariable boolean isAntennaTwo , @PathVariable boolean isAntennaThree , @PathVariable boolean isAntennaFour) {

        this.isAntennaOneP = isAntennaOne;
        this.isAntennaTwoP = isAntennaTwo;
        this.isAntennaThreeP = isAntennaThree;
        this.isAntennaFourP = isAntennaFour;

        timerPassive = new Timer();
        timerPassive.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAntennaOneP) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x01, PortHandle[0]);
                    passiveScanProcess(1);
                }
                if (isAntennaTwoP) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x02, PortHandle[0]);
                    passiveScanProcess(2);
                }
                if (isAntennaThreeP) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x04, PortHandle[0]);
                    passiveScanProcess(3);
                }
                if (isAntennaFourP) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x08, PortHandle[0]);
                    passiveScanProcess(4);
                }
            }
        }, 500, 500);
    }

    @GetMapping("/stopPassiveScan")
    public void stopPassiveScan () {
        this.timerPassive.cancel();
    }

    int counter = 0;
    public void passiveScanProcess(int antennaNumber){

        logger.info("Scanning Passive ..."+counter++);

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

        int result = reader.Inventory_G2(comAddr, QValue, Session, MaskMem, MaskAdr, MaskLen, MaskData, MaskFlag,
                AdrTID, LenTID, TIDFlag, Target, InAnt, Scantime, FastFlag, pEPCList, Ant, Totallen,
                CardNum, PortHandle[0]);

        String EPCMemData = "";

        if (CardNum[0] > 0) {
            System.out.println();
            System.out.println();
            int m = 0;
            for (int index = 0; index < CardNum[0]; index++) {
                int epclen = pEPCList[m++] & 255;
                String EPCstr = "";
                byte[] epc = new byte[epclen];
                for (int n = 0; n < epclen; n++) {
                    byte bbt = pEPCList[m++];
                    epc[n] = bbt;
                    String hex = Integer.toHexString(bbt & 255);
                    if (hex.length() == 1) {
                        hex = "0" + hex;
                    }
                    EPCstr += hex;
                }
                int rssi = pEPCList[m++];
                //¸ù¾ÝTIDºÅÐ´Êý¾Ý
                byte ENum = (byte) 255;//ÑÚÂë
                byte Mem = 1;//¶ÁEPC
                byte WordPtr = 2;//´ÓµÚ2×Ö¿ªÊ¼
                byte Num = 6;//¶Á6¸ö×Ö
                byte[] Password = new byte[4];
                MaskMem = 2;//TIDÑÚÂë
                MaskAdr[0] = 0;
                MaskAdr[1] = 0;
                MaskLen = 96;
                int p = 0;
                System.arraycopy(epc, 0, MaskData, 0, 96 / 8);
                byte[] Data = new byte[Num * 2];
                int[] Errorcode = new int[1];
                byte WNum = 7;
                byte[] Wdt = new byte[WNum * 2];
                Wdt[0] = 0x30;
                Wdt[1] = 0x00;
                Wdt[2] = (byte) 0xE2;
                Wdt[3] = 0x00;
                Wdt[4] = 0x12;
                Wdt[5] = 0x34;
                Wdt[6] = 0x56;
                Wdt[7] = 0x78;
                Wdt[8] = 0x12;
                Wdt[9] = 0x34;
                Wdt[10] = 0x56;
                Wdt[11] = 0x78;
                Wdt[12] = 0x12;
                Wdt[13] = 0x34;
                WordPtr = 1;
                WordPtr = 2;

                result = reader.ReadData_G2(comAddr, epc, ENum, Mem, WordPtr, Num, Password,
                        MaskMem, MaskAdr, MaskLen, MaskData, Data, Errorcode, PortHandle[0]);

                if (result == 0) {
                    String Memdata = "";
                    for (p = 0; p < Num * 2; p++) {
                        byte bbt = Data[p];
                        String hex = Integer.toHexString(bbt & 255);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        Memdata += hex;
                    }

                    EPCMemData = Memdata.toUpperCase();
                }

                logger.info(EPCMemData +" - "+antennaNumber);
                Optional<RT_TRANSPONDER_STATUS> transponderStatusOptional_RENT_OUT = rt_transponder_STATUS_repo.findByEPCAndTransponderStatusAndRowRecordStatus(EPCMemData, "Rent Out", "VALID");
                Optional<RT_TRANSPONDER_STATUS> transponderStatusOptional_REPAIR = rt_transponder_STATUS_repo.findByEPCAndTransponderStatusAndRowRecordStatus(EPCMemData, "Repair", "VALID");
                Optional<RT_EPC_PASSIVE> epcAlertOptional = rt_epc_passive_repo.findByEPCAndRowRecordStatus(EPCMemData, "valid");

                if (!transponderStatusOptional_RENT_OUT.isPresent() && !transponderStatusOptional_REPAIR.isPresent() && !epcAlertOptional.isPresent() && !EPCMemData.equals("")) {

                    RT_EPC_PASSIVE rt_epc_passive = new RT_EPC_PASSIVE();
                    rt_epc_passive.setEPC(EPCMemData);
                    rt_epc_passive.setAntennaNumber(antennaNumber);
                    rt_epc_passive.setTimestamp(LocalDateTime.now());
                    rt_epc_passive.setRowRecordStatus("valid");
                    rt_epc_passive_repo.save(rt_epc_passive);

                    if (!alarm.isAlarmTimerRunning) {
                        alarm.onAlarm();
                    }
                }
            }
        }
    }

    @GetMapping("/viewPassiveScan")
    public ResponseEntity<List<RT_EPC_PASSIVE>>  viewPassiveScan () {

        List<RT_EPC_PASSIVE> rt_epc_passiveList = rt_epc_passive_repo.findAll();

        return ResponseEntity.ok().body(rt_epc_passiveList);
    }





















}
