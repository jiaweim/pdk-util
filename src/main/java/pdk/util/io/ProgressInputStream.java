package pdk.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides a specific FilterInputStream that can be monitored for progress.
 * It is expected to be a bit slower than a regular InputStream, albeit not very much
 * (typical difference compared with only regular FileInputStream for reading a file of appr. 20MB with
 * a wrapped BufferedReader is undetectable).
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 4:47 PM
 */
public class ProgressInputStream extends FilterInputStream implements AutoCloseable {

    /**
     * The total length to be read from the specified InputStream.
     */
    private int iMaximum;
    /**
     * This variable holds the remainder in the InputStream.
     */
    private int iRemainder;
    /**
     * This variable reports on whether the maximum read size has been
     * set through a parameter by the constructor caller.
     */
    private boolean iMaxSet;
    /**
     * When this boolean is set, the continuous cache is bypassed.
     */
    private final boolean iBypassCache;

    /**
     * A MonitorableInputStream can only be constructed around an existing
     * InputStream. This InputStream will be the one monitored.
     *
     * @param aIn InputStream to monitor.
     * @throws IOException when something is wrong with the InpuStream.
     */
    public ProgressInputStream(InputStream aIn) throws IOException {
        this(aIn, false);
    }

    /**
     * A MonitorableInputStream can only be constructed around an existing
     * InputStream. This InputStream will be the one monitored. This constructor
     * allows the setting of the maximum readsize. It is useful when monitoring
     * certain files, which cannot report on the available bytes.
     *
     * @param aIn      InputStream to monitor.
     * @param aMaximum int with the maximum number of bytes that can be read.
     * @throws IOException when something is wrong with the InpuStream.
     */
    public ProgressInputStream(InputStream aIn, int aMaximum) throws IOException {
        this(aIn, false);
        this.iMaximum = aMaximum;
        iMaxSet = true;
    }

    /**
     * A MonitorableInputStream can only be constructed around an existing
     * InputStream. This InputStream will be the one monitored. This constructor
     * can instruct the instance to bypass the progress cache and update the
     * information directly. This is useful when monitoring read progress in
     * zipfiles, which cannot report on the available bytes, and whose
     * underlying streams are masked by a PushBackInputStream.
     *
     * @param aIn          InputStream to monitor.
     * @param aBypassCache boolean that indicates whether the cache should be bypassed.
     * @throws IOException when something is wrong with the InpuStream.
     */
    public ProgressInputStream(InputStream aIn, boolean aBypassCache) throws IOException {
        super(aIn);
        iMaximum = available();
        iRemainder = iMaximum;
        iMaxSet = false;
        iBypassCache = aBypassCache;
    }

    public int getMaximum() {
        return this.iMaximum;
    }

    /**
     * This method returns the progress (as measured by taking the value of iMaximum, minus the
     * current available bytes as reported by the underlying InputStream - variable iRemainder).
     *
     * @return int with the progress.
     */
    public int getProgress() {
        int result;
        if (iBypassCache) {
            try {
                result = this.iMaximum - available();
            } catch (IOException ioe) {
                throw new RuntimeException("Failure when monitoring real-time read progress!");
            }
        } else {
            result = this.iMaximum - this.iRemainder;
        }
        return result;
    }

    public int read(byte[] b) throws IOException {
        int result = super.read(b);
        if (iMaxSet) {
            if (result >= 0) {
                iRemainder -= result;
            }
        } else {
            iRemainder = available();
        }
        return result;
    }

    public int read() throws IOException {
        int result = super.read();
        if (iMaxSet) {
            iRemainder--;
        } else {
            iRemainder = available();
        }
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int result = super.read(b, off, len);
        if (iMaxSet) {
            if (result >= 0) {
                iRemainder -= result;
            }
        } else {
            iRemainder = available();
        }
        return result;
    }

    public long skip(long n) throws IOException {
        long result = super.skip(n);
        if (iMaxSet) {
            iRemainder -= (int) n;
        } else {
            iRemainder = available();
        }
        return result;
    }

    public synchronized void reset() throws IOException {
        super.reset();
        if (!iMaxSet) {
            iMaximum = available();
        }
        iRemainder = iMaximum;
    }
}
