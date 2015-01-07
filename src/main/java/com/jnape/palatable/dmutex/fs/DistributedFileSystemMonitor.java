package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.DistributedMonitor;
import com.jnape.palatable.dmutex.FailedAcquisitionAttemptException;

import java.nio.channels.FileChannel;

public class DistributedFileSystemMonitor implements DistributedMonitor<DistributedFileSystemLock> {

    private final FileChannel fileChannel;

    public DistributedFileSystemMonitor(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    @Override
    public DistributedFileSystemLock tryAcquire() throws FailedAcquisitionAttemptException {
        try {
            return new DistributedFileSystemLock(fileChannel.lock());
        } catch (Exception failed) {
            throw new FailedAcquisitionAttemptException(failed);
        }
    }
}
