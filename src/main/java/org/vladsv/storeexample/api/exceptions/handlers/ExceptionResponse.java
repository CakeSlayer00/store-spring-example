package org.vladsv.storeexample.api.exceptions.handlers;

import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class ExceptionResponse {

    private Instant timestamp;
    private String message;
    private String details;

}
