package com.taita.springboot.taxibookingcustomerapi.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String exception){
        super(exception);
    }
}
