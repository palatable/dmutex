package testsupport.fixtures;

import com.jnape.palatable.dmutex.fs.DistributedFileSystemLockTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

public class Fixtures {

    private static final URL LOCK_FILE = DistributedFileSystemLockTest.class.getResource("/lockfile");

    public static File testLockFile() {
        try {
            return new File(LOCK_FILE.toURI());
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
