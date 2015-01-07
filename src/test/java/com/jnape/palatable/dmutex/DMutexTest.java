package com.jnape.palatable.dmutex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DMutexTest {

    @Mock private DistributedMonitor mockMonitor;
    @Mock private DistributedLock    mockLock;

    private DMutex dMutex;

    @Before
    public void setUp() {
        dMutex = new DMutex(mockMonitor);
    }

    @Test
    public void acquiresLockUsingProvidedDistributedMonitor() {
        when(mockMonitor.tryAcquire()).thenReturn(mockLock);

        assertThat(dMutex.acquire(), is(mockLock));
    }

    @Test
    public void repeatedlyTriesToAcquireLockIfAttemptsAreTemporarilyUnsuccessful() {
        when(mockMonitor.tryAcquire())
                .thenThrow(new FailedAcquisitionAttemptException(new IllegalStateException("Failed attempt 1")))
                .thenThrow(new FailedAcquisitionAttemptException(new IllegalStateException("Failed attempt 2")))
                .thenThrow(new FailedAcquisitionAttemptException(new IllegalStateException("Failed attempt 3")))
                .thenReturn(mockLock);

        assertThat(dMutex.acquire(), is(mockLock));
    }
}