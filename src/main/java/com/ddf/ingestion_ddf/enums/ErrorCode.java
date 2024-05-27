package com.ddf.ingestion_ddf.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    
    REQUEST_ID_NOT_FOUND("error.1001"),
    REQUEST_NOTE_ID_NOT_FOUND("error.1002"),
    INVALID_REQUEST_BODY_ARGUMENTS("error.1003");
    
    final String errorCode;
    
    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
}
