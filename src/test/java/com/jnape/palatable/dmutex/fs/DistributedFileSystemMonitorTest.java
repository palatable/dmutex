package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.FailedAcquisitionAttemptException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertThat;
import static testsupport.fixtures.Fixtures.createChannel;
import static testsupport.fixtures.Fixtures.testLockFile;
import static testsupport.matchers.FileChannelMatcher.isLocked;
import static testsupport.matchers.FileChannelMatcher.isUnlocked;

public class DistributedFileSystemMonitorTest {

    private FileChannel                  fileChannel;
    private DistributedFileSystemMonitor monitor;

    @Before
    public void setUp() {
        fileChannel = createChannel(testLockFile());
        monitor = new DistributedFileSystemMonitor(fileChannel);
    }

    @Test
    public void successfulAcquisitionLocksTheChannel() {
        monitor.tryAcquire();
        assertThat(fileChannel, isLocked());
    }

    @Test
    public void successfulAcquisitionProducesDistributedLockCapableOfUnlockingTheChannel() {
        monitor.tryAcquire()
                .release();

        assertThat(fileChannel, isUnlocked());
    }

    @Test(expected = FailedAcquisitionAttemptException.class)
    public void acquisitionAttemptThrowsExceptionIfChannelWasAlreadyLocked() throws IOException {
        fileChannel.lock();

        monitor.tryAcquire();
    }

    @Test(expected = FailedAcquisitionAttemptException.class)
    public void otherIOExceptionsAreAlsoConsideredFailedAcquisitionAttempts() throws IOException {
        fileChannel.close();

        monitor.tryAcquire();
    }

    @After
    public void tearDown() throws IOException {
        fileChannel.close();
    }
}