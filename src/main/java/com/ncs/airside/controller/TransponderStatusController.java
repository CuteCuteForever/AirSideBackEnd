/*
package com.ncs.airside.controller;

import com.ncs.airside.service.AsyncService;
import com.syc.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransponderStatusController {

    private static Logger logger = LoggerFactory.getLogger(TransponderStatusController.class);

    @Autowired
    private TR transponderStatusRepo;

    @Autowired
    private AsyncService service;

    @GetMapping("/transponderStatuses")
    public List<TransponderStatus> retrieveAllTransponderStatuses(){
        return transponderStatusRepo.findAll();
    }

    */
/*@GetMapping("/scanTransponderCardReader")
    public ResponseEntity<Object> createTransponderStatus(){

        String epc="";
        epc = scanGetEPC();

        if (epc==null){
            return ResponseEntity.ok().body("Please initiate RFID Card Reader Port");
        }

        Optional<TransponderStatus> transponderStatusOptional = transponderStatusRepo.findByEPCAndRowRecordStatus(epc , "VALID");

        if (transponderStatusOptional.isPresent()){
            TransponderStatus transponderStatus = transponderStatusOptional.get();
            transponderStatus.setRowRecordStatus("INVALID");
            transponderStatusRepo.save(transponderStatus);
        }

        TransponderStatus transponderStatus = new TransponderStatus();
        transponderStatus.setRowRecordStatus("VALID");
        transponderStatus.setTimestamp(LocalDateTime.now());
        transponderStatusRepo.save(transponderStatus);
        return ResponseEntity.ok().body("Scan tag Added to database successfully");
    }
*//*


    public String scanGetEPC(){
        byte[] arrBuffer = new byte[40960];
        int[] iNum = new int[2];
        int[] iTotalLen = new int[2];
        byte bRet = 0;

        bRet = Function.RFID.instance.CFHid_GetTagBuf(arrBuffer, iTotalLen, iNum);

        int iTagNumber = 0;
        iTagNumber = iNum[0];
        if (iTagNumber == 0)
            return null
        ;
        int iIndex = 0;
        int iLength = 0;
        byte bPackLength = 0;
        int iIDLen = 0;
        int i = 0;

        for (iIndex = 0; iIndex < iTagNumber; iIndex++) {
            bPackLength = arrBuffer[iLength];
            String str2 = "";
            String str1 = "";
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

            return str2;
        }

        return null;
    }








}
*/
