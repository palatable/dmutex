package com.jnape.palatable.dmutex.fs;

import com.jnape.palatable.dmutex.DistributedLock;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.channels.FileLock;

@EqualsAndHashCode
public final class DistributedFileSystemLock implements DistributedLock {

    private final FileLock fileLock;

    public DistributedFileSystemLock(FileLock fileLock) {
        this.fileLock = fileLock;
    }

    @Override
    public void release() {
        try {
            fileLock.release();
        } catch (IOException ignored) {
        }
    }
}
