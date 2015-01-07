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

    private static final class UnlockedFileChannelMatcher extends FileChannelMatcher {
        @Override
        protected boolean matches(FileChannel fileChannel) {
            try {
                FileLock fileLock = fileChannel.lock();
                try {
                    fileLock.release();
                } catch (IOException ignored) {
                }
                return true;
            } catch (OverlappingFileLockException stillLocked) {
                return false;
            } catch (IOException ioException) {
                throw new AssertionError(ioException);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("not locked");
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText("was locked");
        }
    }

    public static FileChannelMatcher notLocked() {
        return new UnlockedFileChannelMatcher();
    }
}
