package com.ncs.airside.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RFIDCardReaderNotFoundException extends RuntimeException{

    public RFIDCardReaderNotFoundException(String message){
        super(message);
    }

}