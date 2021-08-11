package com.ncs.airside.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class SoundAlertException extends RuntimeException{

    public SoundAlertException(String message){
        super(message);
    }

}