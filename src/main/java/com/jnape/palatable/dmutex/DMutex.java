package com.jnape.palatable.dmutex;

public class DMutex {

    private final DistributedMonitor distributedMonitor;

    public DMutex(DistributedMonitor distributedMonitor) {
        this.distributedMonitor = distributedMonitor;
    }

    public DistributedLock acquire() {
        do {
            try {
                return distributedMonitor.tryAcquire();
            } catch (FailedAcquisitionAttemptException ignored) {
            }
        }
        while (true);
    }
}
