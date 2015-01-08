package com.jnape.palatable.dmutex;

public interface DistributedMonitor<Lock extends DistributedLock> {

    Lock tryAcquire() throws LockCurrentlyHeldException;
}
