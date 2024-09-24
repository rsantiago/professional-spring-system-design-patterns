package com.homeit.rental.property.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class SampleExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail>
        handleGenericException(RuntimeException ex) {
            log.error("exception: ", ex);
            ProblemDetail problemDetail =
                ProblemDetail.forStatus(
                    HttpStatus.INTERNAL_SERVER_ERROR);

            problemDetail.setTitle("Customized Internal Server Error");
            problemDetail.setDetail("An unexpected error occurred: "
                    + ex.getMessage());
            problemDetail.setInstance(
                URI.create("/api/v1/rental-properties/error"));
            problemDetail.setProperty("timestamp",
                LocalDateTime.now().toString());

            return new ResponseEntity<>(problemDetail,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("access denied: ", ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(
                        HttpStatus.UNAUTHORIZED);

        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail("An unexpected error occurred: "
                + ex.getMessage());
        problemDetail.setInstance(
                URI.create("/api/v1/rental-properties/error"));
        problemDetail.setProperty("timestamp",
                LocalDateTime.now().toString());

        return new ResponseEntity<>(problemDetail,
                HttpStatus.UNAUTHORIZED);

    }
}
