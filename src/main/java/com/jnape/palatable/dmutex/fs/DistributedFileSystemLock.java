package com.jnape.palatable.dmutex.fs;

import java.io.IOException;
import java.nio.channels.FileLock;

public class DistributedFileSystemLock {

    private final FileLock fileLock;

    public DistributedFileSystemLock(FileLock fileLock) {
        this.fileLock = fileLock;
    }

    public void release() {
        try {
            fileLock.release();
        } catch (IOException ignored) {
        }
    }
}
