package com.ncs.airside.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TransponderBorrowerEPCDuplicateException extends RuntimeException{

    public TransponderBorrowerEPCDuplicateException(String message){
        super(message);
    }

}