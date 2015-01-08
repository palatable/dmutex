package com.jnape.palatable.dmutex;

public class LockCurrentlyHeldException extends RuntimeException {

    public LockCurrentlyHeldException(Throwable throwable) {
        super(throwable);
    }
}
