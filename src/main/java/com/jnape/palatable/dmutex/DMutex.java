package com.jnape.palatable.dmutex;

import static com.jnape.palatable.dmutex.Duration.forever;
import static com.jnape.palatable.dmutex.LockAcquisitionFailedException.lockAcquisitionFailed;
import static java.lang.System.currentTimeMillis;

public final class DMutex<Lock extends DistributedLock> {

    private final DistributedMonitor<Lock> distributedMonitor;

    public DMutex(DistributedMonitor<Lock> distributedMonitor) {
        this.distributedMonitor = distributedMonitor;
    }

    public Lock acquire() throws LockAcquisitionFailedException {
        return acquire(forever());
    }

    public Lock acquire(Duration maxWait) throws LockAcquisitionFailedException {
        long expirationTimestamp = currentTimeMillis() + maxWait.toMillis();
        do {
            try {
                return distributedMonitor.tryAcquire();
            } catch (LockCurrentlyHeldException ignored) {
            }
        }
        while (currentTimeMillis() < expirationTimestamp);
        throw lockAcquisitionFailed(maxWait);
    }
}
