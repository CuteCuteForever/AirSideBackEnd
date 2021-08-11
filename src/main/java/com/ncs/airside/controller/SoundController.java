package com.ncs.airside.controller;


import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoundController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/offAlertSound")
    public ResponseEntity offAlertSound(){

        asyncService.isAlarmTriggerAtomic.set(false);

        return ResponseEntity.ok().body(new MessageResponse("Switched Off Alert Sound Successfully"));
    }
}
