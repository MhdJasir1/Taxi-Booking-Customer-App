package com.taita.springboot.taxibookingcustomerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<?> accessDeniedException(AccessDeniedException accessDeniedException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accessDeniedException.getMessage());
    }
}
