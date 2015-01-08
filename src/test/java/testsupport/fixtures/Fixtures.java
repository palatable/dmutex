package testsupport.fixtures;

import com.jnape.palatable.dmutex.fs.DistributedFileSystemLockTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

public class Fixtures {

    private static final URL WRITABLE_LOCK_FILE     = DistributedFileSystemLockTest.class.getResource("/writableLockFile");
    private static final URL NON_WRITABLE_LOCK_FILE = DistributedFileSystemLockTest.class.getResource("/nonWritableLockFile");

    public static File writableLockFile() {
        try {
            return new File(WRITABLE_LOCK_FILE.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File nonWritableLockFile() {
        try {
            File file = new File(NON_WRITABLE_LOCK_FILE.toURI());
            file.setWritable(false);
            return file;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileChannel createChannel(File file) {
        try {
            return new FileOutputStream(file).getChannel();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
