package com.ip.server.My.first.Springboot.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
public class SampleExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail>
        handleGenericException(RuntimeException ex) {
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
}
