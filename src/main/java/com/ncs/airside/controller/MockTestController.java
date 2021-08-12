package com.ncs.airside.controller;

import com.ncs.airside.Exception.RFIDCardReaderNotFoundException;
import com.ncs.airside.repository.RT_TRANSPONDER_STATUS_REPO;
import com.ncs.airside.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MockTestController {

    @Value("${hostnameUrl}")
    private String hostnameUrl;

    @Autowired
    private RT_TRANSPONDER_STATUS_REPO rt_transponder_STATUS_repo;

    private static Logger logger = LoggerFactory.getLogger(MockTestController.class);

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/mockRFIDScannedTest")
    public ResponseEntity<String> scannedBorrowedTransponder(){

        ResponseEntity<String> responseEntityEPC =  new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/rfidscan", String.class);

        if (responseEntityEPC.getStatusCode() == HttpStatus.BAD_REQUEST){
            throw new RFIDCardReaderNotFoundException("RFID Card Reader not detected");
        }

       /* Optional<RT_TRANSPONDER_BORROWER> transponderBorrowerOptional = rt_transponder_borrower_repo.findByEpc(responseEntityEPC.getBody());

        if (!transponderBorrowerOptional.isPresent()){

            RT_TRANSPONDER_BORROWER rt_transponder_borrower = new RT_TRANSPONDER_BORROWER();
            rt_transponder_borrower.setVehicleID(444L);
            rt_transponder_borrower.setTimestamp( LocalDateTime.now());
            rt_transponder_borrower.setRowRecordStatus("VALID");

            rt_transponder_borrower_repo.save(rt_transponder_borrower) ;
        } else {
            throw new TransponderBorrowerEPCDuplicateException("Already found duplication in EPC :"+responseEntityEPC.getBody()+"\n"+transponderBorrowerOptional.get().toString());
        }*/

        return ResponseEntity.ok().body("Successfully scanned EPC - "+responseEntityEPC.getBody());
    }

    @GetMapping("/MockAsyncAntennaStartScan")
    public void mockAsyncAntennaCall() throws InterruptedException {
        asyncService.MockAntennaStartScan();
    }

    @GetMapping("/api/test/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

}
