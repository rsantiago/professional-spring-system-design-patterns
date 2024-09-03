package com.homeit.rental.property.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class SampleExceptionHandlerUnitTest {
    private final SampleExceptionHandler handler
        = new SampleExceptionHandler();

    @Test
    public void testHandler() {
        ResponseEntity<ProblemDetail> mySampleException
            = handler.handleGenericException(
                new RuntimeException("my sample exception"));
        ProblemDetail problemDetail = mySampleException.getBody();
        assert problemDetail != null;
        String detail = problemDetail.getDetail();
        Assertions.assertEquals(
    "An unexpected error occurred: my sample exception"
        , detail);
    }
}
