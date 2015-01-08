package com.jnape.palatable.dmutex;

public class DMutex<Lock extends DistributedLock> {

    private final DistributedMonitor<Lock> distributedMonitor;

    public DMutex(DistributedMonitor<Lock> distributedMonitor) {
        this.distributedMonitor = distributedMonitor;
    }

    public Lock acquire() throws LockAcquisitionFailedException {
        do {
            try {
                return distributedMonitor.tryAcquire();
            } catch (LockCurrentlyHeldException ignored) {
            }
        }
        while (true);
    }
}
