package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.DistributedMonitor;
import com.jnape.palatable.dmutex.FailedAcquisitionAttemptException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public final class DistributedFileSystemMonitor implements DistributedMonitor<DistributedFileSystemLock> {

    private final FileChannel fileChannel;

    DistributedFileSystemMonitor(FileChannel fileChannel) {
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        fileChannel.close();
    }

    public static DistributedFileSystemMonitor create(File lockFile) {
        try {
            FileChannel fileChannel = new FileOutputStream(lockFile).getChannel();
            return new DistributedFileSystemMonitor(fileChannel);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
