package com.ncs.airside.controller;


import com.ncs.airside.helper.Alarm;
import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoundController {

    private static Logger logger = LoggerFactory.getLogger(SoundController.class);

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private Alarm alarm;

    @GetMapping("/offAlertSound")
    public ResponseEntity offAlertSound(){
        alarm.offAlarm();

        return ResponseEntity.ok().body(new MessageResponse("Switched Off Alert Sound Successfully"));
    }
}
