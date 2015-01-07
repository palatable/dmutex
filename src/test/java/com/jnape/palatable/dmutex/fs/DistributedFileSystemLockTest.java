package com.jnape.palatable.dmutex.fs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testsupport.fixtures.Fixtures;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testsupport.matchers.FileChannelMatcher.notLocked;

public class DistributedFileSystemLockTest {

    private FileChannel fileChannel;

    @Before
    public void setUp() {
        fileChannel = Fixtures.lockFileChannel();
    }

    @Test
    public void releaseUnlocksFileChannel() throws IOException {
        FileLock fileLock = fileChannel.lock();

        new DistributedFileSystemLock(fileLock).release();

        assertThat(fileChannel, is(notLocked()));
    }

    @Test
    public void releaseLeavesFileChannelOpen() throws IOException {
        FileLock fileLock = fileChannel.lock();

        new DistributedFileSystemLock(fileLock).release();

        assertTrue(fileChannel.isOpen());
    }

    @After
    public void tearDown() {
        try {
            fileChannel.close();
        } catch (IOException ignored) {
        }
    }

}