package com.ddf.ingestion_ddf.exceptions;

import com.ddf.ingestion_ddf.enums.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiException extends RuntimeException {
    
    private HttpStatus status;
    
    private LocalDateTime timestamp;
    
    private String message;
    
    private ErrorCode errorCode;
    
    private ApiException() {
        super();
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiException(HttpStatus status) {
        this();
        this.status = status;
    }
    
    public ApiException(HttpStatus status, ErrorCode errorCode) {
        this(status);
        this.errorCode = errorCode;
    }
    
}
