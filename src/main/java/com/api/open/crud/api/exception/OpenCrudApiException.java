package com.api.open.crud.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OpenCrudApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OpenCrudApiException(String message) {
        super(message);
    }
}
