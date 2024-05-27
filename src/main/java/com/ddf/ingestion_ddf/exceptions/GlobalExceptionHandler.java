package com.ddf.ingestion_ddf.exceptions;

import com.ddf.ingestion_ddf.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String getMessageFromErrorCode(ErrorCode e) {
        if (e == null) {
            return null;
        }
        return messageSource.getMessage(e.getErrorCode(), null, Locale.getDefault());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        ErrorResponse.ErrorResponseBuilder builder = new ErrorResponse.ErrorResponseBuilder();
        builder.timestamp(e.getTimestamp());
        builder.error(getMessageFromErrorCode(e.getErrorCode()));
        builder.message(e.getMessage());
        builder.status(e.getStatus());
        ErrorResponse errorResponse = builder.build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ArrayList<String> errorMessages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(item -> errorMessages.add(item.getDefaultMessage()));
        ErrorResponse.ErrorResponseBuilder builder = new ErrorResponse.ErrorResponseBuilder();
        builder.timestamp(LocalDateTime.now());
        builder.status(BAD_REQUEST);
        builder.error(getMessageFromErrorCode(ErrorCode.INVALID_REQUEST_BODY_ARGUMENTS));
        builder.message(errorMessages.toString());
        return new ResponseEntity<>(builder.build(), BAD_REQUEST);
    }

}
