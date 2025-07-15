package pdk.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FileInputStream with reading progress information.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 4:49 PM
 */
public class ProgressFileInputStream extends ProgressInputStream {

    /**
     * The maximum readable size.
     */
    private final long iMaximum;
    /**
     * The current amount of bytes read from the file.
     */
    private long iCurrent;

    private final double scale;

    /**
     * This constructor takes a file to load the FileInputStream from.
     *
     * @param aFile File to connect the input stream to.
     * @throws IOException when the file could not be raed.
     */
    public ProgressFileInputStream(File aFile) throws IOException {
        super(new FileInputStream(aFile));
        long length = aFile.length();
        int factor = 0;
        long max = Integer.MAX_VALUE;
        while (length >= max) {
            length /= 1024;
            factor++;
        }
        this.iMaximum = aFile.length();
        this.scale = Math.pow(1024, factor);
        this.iCurrent = 0;
    }

    /**
     * This method reports on the maximum scale for the monitor.
     *
     * @return int with the maximum for the monitor.
     */
    public int getMaximum() {
        return (int) (this.iMaximum / scale);
    }

    /**
     * This method returns the progress (as measured by taking the value of iMaximum, minus the current progress as
     * reported by the cache).
     *
     * @return int with the progress.
     */
    public int getProgress() {
        return (int) ((this.iCurrent) / scale);
    }

    public int read(byte[] b) throws IOException {
        int result = super.read(b);
        iCurrent += result;
        return result;
    }

    public int read() throws IOException {
        int result = super.read();
        iCurrent++;
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int result = super.read(b, off, len);
        iCurrent += result;
        return result;
    }

    public long skip(long n) throws IOException {
        long result = super.skip(n);
        iCurrent += n;
        return result;
    }

    public synchronized void reset() throws IOException {
        super.reset();
        iCurrent = 0L;
    }
}
