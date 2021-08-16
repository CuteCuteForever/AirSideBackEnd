package com.ncs.airside.controller;

import com.ncs.airside.model.database.RT_EPC_ACTIVE;
import com.ncs.airside.model.database.RT_EPC_PASSIVE;
import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import com.ncs.airside.model.view.V_ACTIVE_STATUS;
import com.ncs.airside.repository.*;
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
import java.util.*;

import static com.ncs.airside.controller.AntennaController.PortHandle;
import static com.ncs.airside.controller.AntennaController.comAddr;

@RestController
public class AntennaActiveController {


    @Autowired
    private RT_EPC_ACTIVE_REPO rt_epc_active_repo;

    @Autowired
    private V_ACTIVE_STATUS_REPO v_active_status_repo;

    @Autowired
    public Device reader;

    Timer timerActive;

    private Logger logger = LoggerFactory.getLogger(AntennaActiveController.class);

    boolean isAntennaOneA;
    boolean isAntennaTwoA;
    boolean isAntennaThreeA;
    boolean isAntennaFourA;

    @GetMapping("/activeScan/{isAntennaOne}/{isAntennaTwo}/{isAntennaThree}/{isAntennaFour}")
    public void activeScan (@PathVariable boolean isAntennaOne , @PathVariable boolean isAntennaTwo , @PathVariable boolean isAntennaThree , @PathVariable boolean isAntennaFour) {

        rt_epc_active_repo.deleteAll();

        this.isAntennaOneA = isAntennaOne;
        this.isAntennaTwoA = isAntennaTwo;
        this.isAntennaThreeA = isAntennaThree;
        this.isAntennaFourA = isAntennaFour;

        timerActive = new Timer();
        timerActive.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAntennaOneA) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x01, PortHandle[0]);
                    activeScanProcess(1);
                }
                if (isAntennaTwoA) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x02, PortHandle[0]);
                    activeScanProcess(2);
                }
                if (isAntennaThreeA) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x04, PortHandle[0]);
                    activeScanProcess(3);
                }
                if (isAntennaFourA) {
                    reader.SetAntennaMultiplexing(comAddr, (byte) 0x08, PortHandle[0]);
                    activeScanProcess(4);
                }
            }
        }, 500, 500);
    }

    @GetMapping("/stopActiveScan")
    public void stopActiveScan () {
        this.timerActive.cancel();
    }

    int counter = 0;
    public void activeScanProcess(int antennaNumber){

        logger.info("Scanning Active ..."+counter++);

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
                 Optional<RT_EPC_ACTIVE> epcActiveOptional = rt_epc_active_repo.findByEPCAndAntennaNumberAndRowRecordStatus(EPCMemData, antennaNumber,"valid");

                if (!epcActiveOptional.isPresent() && !EPCMemData.equals("")) {

                    RT_EPC_ACTIVE rt_epc_active = new RT_EPC_ACTIVE();
                    rt_epc_active.setEPC(EPCMemData);
                    rt_epc_active.setAntennaNumber(antennaNumber);
                    rt_epc_active.setTimestamp(LocalDateTime.now());
                    rt_epc_active.setRowRecordStatus("valid");
                    rt_epc_active_repo.save(rt_epc_active);

                }
            }
        }
    }

    @GetMapping("/viewActiveScan")
    public ResponseEntity viewActiveScan () {

        List<V_ACTIVE_STATUS> vActiveStatusList = v_active_status_repo.findAll();

        List<V_ACTIVE_STATUS> newActiveList = new ArrayList<>();
        V_ACTIVE_STATUS newVActiveStatus = new V_ACTIVE_STATUS();

        HashMap<String , V_ACTIVE_STATUS> newMap = new HashMap();

        vActiveStatusList.forEach( item -> {

            if (newMap.get(item.getEPC()) == null){
                newMap.put(item.getEPC() , item);
            } else {
                V_ACTIVE_STATUS obj = newMap.get(item.getEPC());
                String antennaNumber = item.getAntennaNumber();
                obj.setAntennaNumber(obj.getAntennaNumber()+","+antennaNumber);
            }
        });

        for (Map.Entry<String,V_ACTIVE_STATUS> entry : newMap.entrySet()){
            newActiveList.add(entry.getValue());
        }

        return ResponseEntity.ok().body(newActiveList);
    }

}
