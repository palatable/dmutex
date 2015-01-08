package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.FailedAcquisitionAttemptException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.jnape.palatable.dmutex.fs.DistributedFileSystemMonitor.create;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static testsupport.fixtures.Fixtures.createChannel;
import static testsupport.fixtures.Fixtures.nonWritableLockFile;
import static testsupport.fixtures.Fixtures.writableLockFile;
import static testsupport.matchers.FileChannelMatcher.isLocked;
import static testsupport.matchers.FileChannelMatcher.isUnlocked;

public class DistributedFileSystemMonitorTest {

    private FileChannel                  fileChannel;
    private DistributedFileSystemMonitor monitor;

    @Before
    public void setUp() {
        fileChannel = createChannel(writableLockFile());
        monitor = new DistributedFileSystemMonitor(fileChannel);
    }

    @Test
    public void createsInstanceFromLockFile() {
        create(writableLockFile())
                .tryAcquire()
                .release();
    }

    @Test(expected = RuntimeException.class)
    public void failsCreationWithExceptionIfFileDoesNotExistAndCannotBeCreated() {
        create(new File("/\\/\\cannotBeCreated"));
    }

    @Test(expected = RuntimeException.class)
    public void failsCreationWithExceptionIfFileIsNotWritable() {
        create(nonWritableLockFile());
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

    @Test(expected = RuntimeException.class)
    public void otherIOExceptionsAreWrappedInRuntimeExceptions() throws IOException {
        fileChannel.close();

        monitor.tryAcquire();
    }

    @Test
    @SuppressWarnings("FinalizeCalledExplicitly")
    public void closesFileChannelDuringFinalization() throws Throwable {
        monitor.finalize();
        assertFalse(fileChannel.isOpen());
    }

    @After
    public void tearDown() throws IOException {
        fileChannel.close();
    }
}