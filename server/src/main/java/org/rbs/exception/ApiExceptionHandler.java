package org.rbs.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleException(ApiRequestException e) {
        ApiException apiException = new ApiException(
                e.getLocalizedMessage(),
                e.getHttpStatus(),
                ZonedDateTime.now(ZoneId.of("UTC"))
        );
        return new ResponseEntity<>(apiException, e.getHttpStatus());
    }
}