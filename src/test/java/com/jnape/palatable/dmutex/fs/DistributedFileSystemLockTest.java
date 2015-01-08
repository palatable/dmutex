package com.jnape.palatable.dmutex.fs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testsupport.fixtures.Fixtures.createChannel;
import static testsupport.fixtures.Fixtures.writableLockFile;
import static testsupport.matchers.FileChannelMatcher.isUnlocked;

public class DistributedFileSystemLockTest {

    private FileChannel fileChannel;
    private FileLock    fileLock;

    @Before
    public void setUp() throws IOException {
        fileChannel = createChannel(writableLockFile());
        fileLock = fileChannel.lock();
    }

    @Test
    public void releaseUnlocksFileChannel() {
        new DistributedFileSystemLock(fileLock).release();
        assertThat(fileChannel, isUnlocked());
    }

    @Test
    public void releaseLeavesFileChannelOpen() {
        new DistributedFileSystemLock(fileLock).release();
        assertTrue(fileChannel.isOpen());
    }

    @After
    public void tearDown() throws IOException {
        fileLock.release();
        fileChannel.close();
    }
}