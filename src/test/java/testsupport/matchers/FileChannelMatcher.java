package testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public abstract class FileChannelMatcher extends BaseMatcher<FileChannel> {

    @Override
    public boolean matches(Object other) {
        return other instanceof FileChannel && matches((FileChannel) other);
    }

    protected abstract boolean matches(FileChannel fileChannel);

    private static final class LockedFileChannelMatcher extends FileChannelMatcher {
        @Override
        protected boolean matches(FileChannel fileChannel) {
            try {
                FileLock fileLock = fileChannel.lock();
                try {
                    fileLock.release();
                } catch (IOException ignored) {
                }
                return false;
            } catch (OverlappingFileLockException stillLocked) {
                return true;
            } catch (IOException ioException) {
                throw new AssertionError(ioException);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("FileChannel to be locked");
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText("it was unlocked");
        }
    }

    private static final class UnlockedFileChannelMatcher extends FileChannelMatcher {
        private final LockedFileChannelMatcher lockedFileChannelMatcher;

        public UnlockedFileChannelMatcher(LockedFileChannelMatcher lockedFileChannelMatcher) {
            this.lockedFileChannelMatcher = lockedFileChannelMatcher;
        }

        @Override
        protected boolean matches(FileChannel fileChannel) {
            return !lockedFileChannelMatcher.matches(fileChannel);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("FileChannel to be unlocked");
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText("it was locked");
        }
    }

    public static FileChannelMatcher isLocked() {
        return new LockedFileChannelMatcher();
    }

    public static FileChannelMatcher isUnlocked() {
        return new UnlockedFileChannelMatcher(new LockedFileChannelMatcher());
    }
}
