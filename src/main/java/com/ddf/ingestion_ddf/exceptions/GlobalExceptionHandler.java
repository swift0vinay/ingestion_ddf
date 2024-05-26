package com.ddf.ingestion_ddf.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @Autowired
    private MessageSource messageSource;
    
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        ErrorResponse.ErrorResponseBuilder builder = new ErrorResponse.ErrorResponseBuilder();
        builder.timestamp(e.getTimestamp());
        if (e.getErrorCode() != null) {
            builder.error(messageSource.getMessage(e.getErrorCode().getErrorCode(), null, Locale.getDefault()));
        }
        builder.message(e.getMessage());
        builder.status(e.getStatus());
        ErrorResponse errorResponse = builder.build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
    
}
