package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.FailedAcquisitionAttemptException;

import java.nio.channels.FileChannel;

public class DistributedFileSystemMonitor {

    private final FileChannel fileChannel;

    public DistributedFileSystemMonitor(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public DistributedFileSystemLock tryAcquire() throws FailedAcquisitionAttemptException {
        try {
            return new DistributedFileSystemLock(fileChannel.lock());
        } catch (Exception failed) {
            throw new FailedAcquisitionAttemptException(failed);
        }
    }
}
