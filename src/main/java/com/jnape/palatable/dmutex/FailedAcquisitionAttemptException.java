package com.jnape.palatable.dmutex;

public class FailedAcquisitionAttemptException extends RuntimeException {

    public FailedAcquisitionAttemptException(Throwable throwable) {
        super(throwable);
    }
}
