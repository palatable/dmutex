package com.jnape.palatable.dmutex;

import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class LockAcquisitionFailedException extends RuntimeException {

    public LockAcquisitionFailedException(Throwable cause) {
        super("Unable to acquire a lock", cause);
    }

    public static LockAcquisitionFailedException lockAcquisitionFailed(Throwable cause) {
        return new LockAcquisitionFailedException(cause);
    }

    public static LockAcquisitionFailedException lockAcquisitionFailed(Duration duration) {
        return new LockAcquisitionFailedException(new TimeoutException(format("Could not acquire a lock within %s", duration.toString())));
    }
}
