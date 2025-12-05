package com.rantomah.drift.framework.core.exception;

public class DriftException extends RuntimeException {

    public DriftException(String message) {
        super(message);
    }

    public DriftException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriftException(Throwable cause) {
        super(cause);
    }
}
