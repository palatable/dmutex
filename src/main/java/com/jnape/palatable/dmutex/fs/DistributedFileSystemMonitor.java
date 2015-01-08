package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.DistributedMonitor;
import com.jnape.palatable.dmutex.LockCurrentlyHeldException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;

public final class DistributedFileSystemMonitor implements DistributedMonitor<DistributedFileSystemLock> {

    private final FileChannel fileChannel;

    DistributedFileSystemMonitor(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    @Override
    public DistributedFileSystemLock tryAcquire() throws LockCurrentlyHeldException {
        try {
            return new DistributedFileSystemLock(fileChannel.lock());
        } catch (OverlappingFileLockException overlap) {
            throw new LockCurrentlyHeldException(overlap);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
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
