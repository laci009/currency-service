package com.tlaci.currencyservice.rest.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleConflict(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
    }

    @ExceptionHandler(value = { ExchangeRateNotFoundException.class })
    protected ResponseEntity<Object> handleConflict(ExchangeRateNotFoundException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, createNotFoundMessage(ex), ex));
    }

    private String createNotFoundMessage(ExchangeRateNotFoundException ex) {
        StringBuilder notFoundMessage = new StringBuilder();
        notFoundMessage.append("Exchange rate not found with parameters");
        if (ex.getFrom() != null) {
            notFoundMessage.append(", from: ").append(ex.getFrom());
        }
        if (ex.getTo() != null) {
            notFoundMessage.append(", to: ").append(ex.getTo());
        }
        if (ex.getType() != null) {
            notFoundMessage.append(", type: ").append(ex.getType());
        }
        return notFoundMessage.toString();
    }

}