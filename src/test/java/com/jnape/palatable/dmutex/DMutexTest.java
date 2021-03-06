package com.jnape.palatable.dmutex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.jnape.palatable.dmutex.Duration.milliseconds;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DMutexTest {

    @Mock private DistributedMonitor<DistributedLock> mockMonitor;
    @Mock private DistributedLock mockLock;

    private DMutex dMutex;

    @Before
    public void setUp() {
        dMutex = new DMutex<DistributedLock>(mockMonitor);
    }

    @Test
    public void acquiresLockUsingProvidedDistributedMonitor() {
        when(mockMonitor.tryAcquire()).thenReturn(mockLock);

        assertThat(dMutex.acquire(), is(mockLock));
    }

    @Test
    public void repeatedlyTriesToAcquireLockIfAttemptsAreTemporarilyUnsuccessful() {
        when(mockMonitor.tryAcquire())
                .thenThrow(new LockCurrentlyHeldException(new IllegalStateException("Failed attempt 1")))
                .thenThrow(new LockCurrentlyHeldException(new IllegalStateException("Failed attempt 2")))
                .thenThrow(new LockCurrentlyHeldException(new IllegalStateException("Failed attempt 3")))
                .thenReturn(mockLock);

        assertThat(dMutex.acquire(), is(mockLock));
    }

    @Test(expected = LockAcquisitionFailedException.class)
    public void failsWithExceptionIfTimeExpiresBeforeSuccessfulLockAcquisition() {
        when(mockMonitor.tryAcquire()).thenThrow(new LockCurrentlyHeldException(new IllegalStateException("Always failing attempt")));

        dMutex.acquire(milliseconds(100));
    }

    @Test(expected = LockAcquisitionFailedException.class)
    public void interruptsProperly() {
        when(mockMonitor.tryAcquire())
                .thenThrow(new LockCurrentlyHeldException(new IllegalStateException("Always failing attempt")));

        final Thread testThread = Thread.currentThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(100);
                    testThread.interrupt();
                } catch (InterruptedException ignored) {
                }
            }
        }).start();

        dMutex.acquire();
    }
}