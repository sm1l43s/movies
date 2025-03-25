package com.moviescloud.common.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviescloud.common.exceptions.AccessDeniedException;
import com.moviescloud.common.exceptions.AppException;
import com.moviescloud.common.exceptions.ResourceNotFoundException;
import com.moviescloud.common.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppException> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppException(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<AppException> handleRestTemplateNotFound(HttpClientErrorException.NotFound e) {
        String responseBody = e.getResponseBodyAsString();
        String defaultMessage = "Resource not found";

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            if (jsonNode.has("message")) {
                defaultMessage = jsonNode.get("message").asText();
            }
        } catch (IOException ex) {
            log.warn("Failed to parse error response", ex);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AppException(HttpStatus.NOT_FOUND.value(), defaultMessage));
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<AppException> catchUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppException(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppException> catchAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppException(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
