package pdk.util.io;

import pdk.util.exception.PDKRuntimeException;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/**
 * An {@link InputStream} that reads data from a byte[] and optionally fills the byte[] from
 * another {@link InputStream} as needed. Utility methods are provided for efficiently reading primitive types and strings.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 8:54 AM
 */
public class Input extends InputStream {

    /**
     * Maximum reasonable array length. See: https://stackoverflow.com/questions/3038392/do-java-arrays-have-a-maximum-size
     */
    public static final int maxArraySize = Integer.MAX_VALUE - 8;

    /**
     * buffer to hold data
     */
    protected byte[] buffer;
    /**
     * position in the buffer
     */
    protected int position;
    /**
     * capacity of the buffer
     */
    protected int capacity;
    /**
     * current data length in buffer
     */
    protected int limit;
    /**
     * the total number of byte read
     */
    protected long total;
    protected char[] chars = new char[32];
    protected InputStream inputStream;
    /**
     * true if enable variable encoding
     */
    protected boolean varEncoding = true;

    /**
     * Creates an uninitialized Input, {@link #setBuffer(byte[])} must be called before the Input is used.
     */
    public Input() {}

    /**
     * Creates an Input for reading from a byte[] buffer.
     *
     * @param bufferSize The size of the buffer. An exception is thrown if more bytes than this are read and
     *                   {@link #fill(byte[], int, int)} does not supply more bytes.
     */
    public Input(int bufferSize) {
        this.capacity = bufferSize;
        this.buffer = new byte[bufferSize];
    }

    /**
     * Creates a new Input for reading from a byte[] buffer.
     *
     * @param buffer An exception is thrown if more bytes than this are read and {@link #fill(byte[], int, int)} does not supply
     *               more bytes.
     */
    public Input(byte[] buffer) {
        setBuffer(buffer, 0, buffer.length);
    }

    /**
     * Creates an Input for reading from a byte[] buffer.
     * <p>
     * An exception is thrown if more bytes than this are read and {@link #fill(byte[], int, int)} does not supply
     * more bytes.
     *
     * @param buffer data buffer
     * @param offset offset
     * @param count  number of byte to read
     */
    public Input(byte[] buffer, int offset, int count) {
        setBuffer(buffer, offset, count);
    }

    /**
     * Creates an Input for reading from an {@link InputStream} with a buffer size of 4096.
     *
     * @param inputStream {@link InputStream} instance
     */
    public Input(InputStream inputStream) {
        this(4096);
        if (inputStream == null) throw new IllegalArgumentException("inputStream cannot be null.");
        this.inputStream = inputStream;
    }

    /**
     * Creates a new Input for reading from an InputStream with the specified buffer size.
     *
     * @param inputStream {@link InputStream}
     * @param bufferSize  buffer size
     */
    public Input(InputStream inputStream, int bufferSize) {
        this(bufferSize);
        if (inputStream == null) throw new IllegalArgumentException("inputStream cannot be null.");
        this.inputStream = inputStream;
    }

    /**
     * Sets a new buffer. The offset is 0 and the count is the buffer's length.
     *
     * @param bytes buffer
     * @see #setBuffer(byte[], int, int)
     */
    public void setBuffer(byte[] bytes) {
        setBuffer(bytes, 0, bytes.length);
    }

    /**
     * Sets a new buffer to read from. The bytes are not copied, the old buffer is discarded and the new buffer used in its place.
     * The position and total are reset. The {@link #setInputStream(InputStream) InputStream} is set to null.
     *
     * @param bytes  new  buffer
     * @param offset offset
     * @param count  number of byte to read
     */
    public void setBuffer(byte[] bytes, int offset, int count) {
        requireNonNull(bytes);

        buffer = bytes;
        position = offset;
        limit = offset + count;
        capacity = bytes.length;
        total = 0;
        inputStream = null;
    }

    /**
     * Returns the buffer. The bytes between 0 and {@link #position()} are the data that can be read.
     *
     * @return buffer
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * {@link InputStream} source
     *
     * @return the {@link InputStream}
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Sets an InputStream to read from when data in the buffer is exhausted. The position, limit, and total are reset, discarding
     * any buffered bytes.
     *
     * @param inputStream May be null.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        limit = 0;
        reset();
    }

    /**
     * Return true if enable variable length encoding
     *
     * @return variable length encoding
     */
    public boolean getVariableLengthEncoding() {
        return varEncoding;
    }

    /**
     * If false, {@link #readInt(boolean)}, {@link #readLong(boolean)}, {@link #readInts(int, boolean)}, and
     * {@link #readLongs(int, boolean)} will use fixed length encoding, which may be faster for some data. Default is true.
     *
     * @param varEncoding whether enable variable encoding
     */
    public void setVariableLengthEncoding(boolean varEncoding) {
        this.varEncoding = varEncoding;
    }

    /**
     * Returns the total number of bytes read.
     *
     * @return bytes read
     */
    public long total() {
        return total + position;
    }

    /**
     * Sets the total number of bytes read.
     *
     * @param total number of bytes read
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * Returns the current position in the buffer.
     *
     * @return position in buffer
     */
    public int position() {
        return position;
    }

    /**
     * Sets the current position in the buffer where the next byte will be read.
     *
     * @param position current position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the limit for the buffer.
     *
     * @return limit for the buffer
     */
    public int limit() {
        return limit;
    }

    /**
     * Sets the limit in the buffer which marks the end of the data that can be read.
     *
     * @param limit new limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Sets the position and total to zero.
     */
    @SuppressWarnings("sync-override")
    public void reset() {
        position = 0;
        total = 0;
    }

    /**
     * Discards the specified number of bytes.
     *
     * @param count number of bytes to skip
     * @throws PDKRuntimeException io exception
     */
    public void skip(int count) throws PDKRuntimeException {
        int skipCount = Math.min(limit - position, count);
        while (true) {
            position += skipCount;
            count -= skipCount;
            if (count == 0) break;
            skipCount = Math.min(count, capacity);
            require(skipCount);
        }
    }

    /**
     * Fills the buffer with more bytes. The default implementation reads from the {@link #getInputStream() InputStream}, if set.
     * *Can be overridden to fill the bytes from another source.
     *
     * @param buffer buffer to fill
     * @param offset offset in buffer
     * @param count  number of byte to read
     * @return -1 if there are no more bytes.
     * @throws PDKRuntimeException io exception
     */
    protected int fill(byte[] buffer, int offset, int count) throws PDKRuntimeException {
        if (inputStream == null) return -1;
        try {
            return inputStream.read(buffer, offset, count);
        } catch (IOException ex) {
            throw new PDKRuntimeException(ex);
        }
    }

    /**
     * Fills the buffer with at least the number of bytes specified.
     *
     * @param required Must be > 0.
     * @return The number of bytes remaining in the buffer, which will be at least <code>required</code> bytes.
     * @throws BufferUnderflowException if {@link #fill(byte[], int, int)} is unable to provide more bytes (buffer
     *                                  underflow).
     */
    protected int require(int required) throws PDKRuntimeException {
        int remaining = limit - position;
        if (remaining >= required) return remaining;
        if (required > capacity)
            throw new PDKRuntimeException("Buffer too small: capacity: " + capacity + ", required: " + required);

        int count;
        // Try to fill the buffer.
        if (remaining > 0) {
            count = fill(buffer, limit, capacity - limit);
            if (count == -1) throw new BufferUnderflowException("Buffer underflow.");
            remaining += count;
            if (remaining >= required) {
                limit += count;
                return remaining;
            }
        }

        // Was not enough, compact and try again.
        System.arraycopy(buffer, position, buffer, 0, remaining);
        total += position;
        position = 0;

        while (true) {
            count = fill(buffer, remaining, capacity - remaining);
            if (count == -1) {
                if (remaining >= required) break;
                throw new BufferUnderflowException("Buffer underflow.");
            }
            remaining += count;
            if (remaining >= required) break; // Enough has been read.
        }

        limit = remaining;
        return remaining;
    }

    /**
     * Fills the buffer with at least the number of bytes specified, if possible.
     *
     * @param optional Must be > 0.
     * @return the number of bytes remaining, but not more than optional, or -1 if {@link #fill(byte[], int, int)} is unable to
     * provide more bytes.
     */
    protected int optional(int optional) throws PDKRuntimeException {
        int remaining = limit - position;
        if (remaining >= optional) return optional;
        optional = Math.min(optional, capacity);

        int count;

        // Try to fill the buffer.
        count = fill(buffer, limit, capacity - limit);
        if (count == -1) return remaining == 0 ? -1 : Math.min(remaining, optional);
        remaining += count;
        if (remaining >= optional) {
            limit += count;
            return optional;
        }

        // Was not enough, compact and try again.
        System.arraycopy(buffer, position, buffer, 0, remaining);
        total += position;
        position = 0;

        while (true) {
            count = fill(buffer, remaining, capacity - remaining);
            if (count == -1) break;
            remaining += count;
            if (remaining >= optional) break; // Enough has been read.
        }

        limit = remaining;
        return remaining == 0 ? -1 : Math.min(remaining, optional);
    }

    /**
     * Returns true if the {@link #limit()} has been reached and {@link #fill(byte[], int, int)} is unable to provide more
     * bytes.
     *
     * @return true if reach the end
     */
    public boolean end() {
        return optional(1) <= 0;
    }

    // InputStream:

    public int available() throws IOException {
        return limit - position + (inputStream != null ? inputStream.available() : 0);
    }

    /**
     * Reads a single byte as an int from 0 to 255, or -1 if there are no more bytes are available.
     */
    public int read() throws PDKRuntimeException {
        if (optional(1) <= 0) return -1;
        return buffer[position++] & 0xFF;
    }

    /**
     * Reads bytes.length bytes or less and writes them to the specified byte[], starting at 0, and returns the number of bytes
     * read or -1 if no more bytes are available.
     */
    public int read(byte[] bytes) throws PDKRuntimeException {
        return read(bytes, 0, bytes.length);
    }

    /**
     * Read {@code count} bytes or less and write them to the specified byte[]
     *
     * @param bytes  the buffer into which the data is read.
     * @param offset the start offset in array {@code bytes}
     *               at which the data is written.
     * @param count  the maximum number of bytes to read.
     * @return the number of bytes read or -1 ir no more bytes are available
     */
    public int read(byte[] bytes, int offset, int count) throws PDKRuntimeException {
        if (bytes == null) throw new IllegalArgumentException("bytes cannot be null.");
        int startingCount = count;
        int copyCount = Math.min(limit - position, count);
        while (true) {
            System.arraycopy(buffer, position, bytes, offset, copyCount);
            position += copyCount;
            count -= copyCount;
            if (count == 0) break;
            offset += copyCount;
            copyCount = optional(count);
            if (copyCount == -1) {
                // End of data.
                if (startingCount == count) return -1;
                break;
            }
            if (position == limit) break;
        }
        return startingCount - count;
    }

    /**
     * Discards the specified number of bytes.
     */
    public long skip(long count) throws PDKRuntimeException {
        long remaining = count;
        while (remaining > 0) {
            int skip = (int) Math.min(maxArraySize, remaining);
            skip(skip);
            remaining -= skip;
        }
        return count;
    }

    /**
     * Closes the underlying InputStream, if any.
     */
    public void close() throws PDKRuntimeException {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Reads a single byte.
     *
     * @return the read byte
     * @throws PDKRuntimeException io exception
     */
    public byte readByte() throws PDKRuntimeException {
        if (position == limit) require(1);
        return buffer[position++];
    }

    /**
     * Reads a byte as an int from 0 to 255.
     *
     * @return byte
     * @throws PDKRuntimeException io exception
     */
    public int readByteUnsigned() throws PDKRuntimeException {
        if (position == limit) require(1);
        return buffer[position++] & 0xFF;
    }

    /**
     * Reads the specified number of bytes into a new byte[].
     *
     * @param length number of byte to read
     * @return read bytes
     * @throws PDKRuntimeException io exception
     */
    public byte[] readBytes(int length) throws PDKRuntimeException {
        byte[] bytes = new byte[length];
        readBytes(bytes, 0, length);
        return bytes;
    }

    /**
     * Reads bytes.length bytes and writes them to the specified byte[], starting at index 0.
     *
     * @param bytes array to store read bytes
     * @throws PDKRuntimeException for io exception
     */
    public void readBytes(byte[] bytes) throws PDKRuntimeException {
        readBytes(bytes, 0, bytes.length);
    }

    /**
     * Reads count bytes and writes them to the specified byte[], starting at offset.
     *
     * @param bytes  array to store read bytes
     * @param offset offset in the target array
     * @param count  number of bytes to read
     * @throws PDKRuntimeException io exception
     */
    public void readBytes(byte[] bytes, int offset, int count) throws PDKRuntimeException {
        requireNonNull(bytes);
        int copyCount = Math.min(limit - position, count);
        while (true) {
            System.arraycopy(buffer, position, bytes, offset, copyCount);
            position += copyCount;
            count -= copyCount;
            if (count == 0) break;
            offset += copyCount;
            copyCount = Math.min(count, capacity);
            require(copyCount);
        }
    }

    /**
     * Reads count bytes and returns them as int, the last byte read will be the lowest byte in the int.
     *
     * @param count number of bytes
     * @return the int value read
     */
    public int readInt(int count) {
        if (count < 0 || count > 4) throw new IllegalArgumentException("count must be >= 0 and <= 4: " + count);
        require(count);
        int p = position;
        position = p + count;
        return switch (count) {
            case 1 -> buffer[p];
            case 2 -> buffer[p] << 8
                    | buffer[p + 1] & 0xFF;
            case 3 -> buffer[p] << 16
                    | (buffer[p + 1] & 0xFF) << 8
                    | buffer[p + 2] & 0xFF;
            case 4 -> buffer[p] << 24
                    | (buffer[p + 1] & 0xFF) << 16
                    | (buffer[p + 2] & 0xFF) << 8
                    | buffer[p + 3] & 0xFF;
            default -> throw new IllegalStateException(); // impossible
        };
    }

    /**
     * Reads count bytes and returns them as long, the last byte read will be the lowest byte in the long.
     *
     * @param count number of bytes
     * @return the long value read
     */
    public long readLong(int count) {
        if (count < 0 || count > 8) throw new IllegalArgumentException("count must be >= 0 and <= 8: " + count);
        if (count <= 4) {
            return readInt(count);
        } else {
            require(count);
            long highBytes = ((long) readInt(count - 4)) << 32;
            long lowBytes = ((long) readInt(4)) & (1L << 32) - 1;
            return highBytes | lowBytes;
        }
    }

    // int:

    /**
     * Reads a 4 byte int.
     *
     * @return the read int value
     */
    public int readInt() throws PDKRuntimeException {
        require(4);
        byte[] buffer = this.buffer;
        int p = this.position;
        this.position = p + 4;
        return buffer[p] & 0xFF //
                | (buffer[p + 1] & 0xFF) << 8 //
                | (buffer[p + 2] & 0xFF) << 16 //
                | (buffer[p + 3] & 0xFF) << 24;
    }

    /**
     * Reads an int using fixed or variable length encoding, depending on {@link #setVariableLengthEncoding(boolean)}. Use
     * {@link #readVarInt(boolean)} explicitly when reading values that should always use variable length encoding (eg values that
     * appear many times).
     *
     * @param optimizePositive true if optimize for positive int
     * @return int value
     * @see #canReadInt()
     */
    public int readInt(boolean optimizePositive) throws PDKRuntimeException {
        if (varEncoding) return readVarInt(optimizePositive);
        return readInt();
    }

    /**
     * Returns true if enough bytes are available to read an int with {@link #readInt(boolean)}.
     *
     * @return true if enough bytes are available to read an int with {@link #readInt(boolean)}.
     */
    public boolean canReadInt() throws PDKRuntimeException {
        if (varEncoding) return canReadVarInt();
        if (limit - position >= 4) return true;
        return optional(4) == 4;
    }

    /**
     * Reads a 1-5 byte int.
     *
     * @param optimizePositive true if optimize for positive int
     * @return an int value
     * @see #canReadVarInt()
     */
    public int readVarInt(boolean optimizePositive) throws PDKRuntimeException {
        if (require(1) < 5) return readVarInt_slow(optimizePositive);
        int b = buffer[position++];
        int result = b & 0x7F;
        if ((b & 0x80) != 0) {
            byte[] buffer = this.buffer;
            int p = position;
            b = buffer[p++];
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                b = buffer[p++];
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    b = buffer[p++];
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        b = buffer[p++];
                        result |= (b & 0x7F) << 28;
                    }
                }
            }
            position = p;
        }
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    private int readVarInt_slow(boolean optimizePositive) {
        // The buffer is guaranteed to have at least 1 byte.
        int b = buffer[position++];
        int result = b & 0x7F;
        if ((b & 0x80) != 0) {
            if (position == limit) require(1);
            byte[] buffer = this.buffer;
            b = buffer[position++];
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                if (position == limit) require(1);
                b = buffer[position++];
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    if (position == limit) require(1);
                    b = buffer[position++];
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        if (position == limit) require(1);
                        b = buffer[position++];
                        result |= (b & 0x7F) << 28;
                    }
                }
            }
        }
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    /**
     * Returns true if enough bytes are available to read an int with {@link #readVarInt(boolean)}.
     *
     * @return true if the remaining bytes are enough to read an int for {@link #readVarInt(boolean)}.
     */
    public boolean canReadVarInt() throws PDKRuntimeException {
        if (limit - position >= 5) return true;
        if (optional(5) <= 0) return false;
        int p = position, limit = this.limit;
        byte[] buffer = this.buffer;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        return true;
    }

    /**
     * Reads the boolean part of a varint flag. The position is not advanced, {@link #readVarIntFlag(boolean)} should be used to
     * advance the position.
     *
     * @return boolean part of the varint flag
     */
    public boolean readVarIntFlag() {
        if (position == limit) require(1);
        return (buffer[position] & 0x80) != 0;
    }

    /**
     * Reads the 1-5 byte int part of a varint flag. The position is advanced so if the boolean part is needed it should be read
     * first with {@link #readVarIntFlag()}.
     *
     * @param optimizePositive Whether to optimize for positive numbers
     * @return a int value
     */
    public int readVarIntFlag(boolean optimizePositive) {
        if (require(1) < 5) return readVarIntFlag_slow(optimizePositive);
        int b = buffer[position++];
        int result = b & 0x3F; // Mask first 6 bits.
        if ((b & 0x40) != 0) { // Bit 7 means another byte, bit 8 means UTF8.
            byte[] buffer = this.buffer;
            int p = position;
            b = buffer[p++];
            result |= (b & 0x7F) << 6;
            if ((b & 0x80) != 0) {
                b = buffer[p++];
                result |= (b & 0x7F) << 13;
                if ((b & 0x80) != 0) {
                    b = buffer[p++];
                    result |= (b & 0x7F) << 20;
                    if ((b & 0x80) != 0) {
                        b = buffer[p++];
                        result |= (b & 0x7F) << 27;
                    }
                }
            }
            position = p;
        }
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    private int readVarIntFlag_slow(boolean optimizePositive) {
        // The buffer is guaranteed to have at least 1 byte.
        int b = buffer[position++];
        int result = b & 0x3F;
        if ((b & 0x40) != 0) {
            if (position == limit) require(1);
            byte[] buffer = this.buffer;
            b = buffer[position++];
            result |= (b & 0x7F) << 6;
            if ((b & 0x80) != 0) {
                if (position == limit) require(1);
                b = buffer[position++];
                result |= (b & 0x7F) << 13;
                if ((b & 0x80) != 0) {
                    if (position == limit) require(1);
                    b = buffer[position++];
                    result |= (b & 0x7F) << 20;
                    if ((b & 0x80) != 0) {
                        if (position == limit) require(1);
                        b = buffer[position++];
                        result |= (b & 0x7F) << 27;
                    }
                }
            }
        }
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    // long:

    /**
     * Reads an 8 byte long.
     *
     * @return the long value read
     */
    public long readLong() throws PDKRuntimeException {
        require(8);
        byte[] buffer = this.buffer;
        int p = position;
        position = p + 8;
        return buffer[p] & 0xFF //
                | (buffer[p + 1] & 0xFF) << 8 //
                | (buffer[p + 2] & 0xFF) << 16 //
                | (long) (buffer[p + 3] & 0xFF) << 24 //
                | (long) (buffer[p + 4] & 0xFF) << 32 //
                | (long) (buffer[p + 5] & 0xFF) << 40 //
                | (long) (buffer[p + 6] & 0xFF) << 48 //
                | (long) buffer[p + 7] << 56;
    }

    /**
     * Reads a long using fixed or variable length encoding, depending on {@link #setVariableLengthEncoding(boolean)}. Use
     * {@link #readVarLong(boolean)} explicitly when reading values that should always use variable length encoding (eg values that
     * appear many times).
     *
     * @param optimizePositive Whether to optimize for positive numbers
     * @return the long value read
     * @see #canReadLong()
     */
    public long readLong(boolean optimizePositive) throws PDKRuntimeException {
        if (varEncoding) return readVarLong(optimizePositive);
        return readLong();
    }

    /**
     * Reads a 1-9 byte long.
     *
     * @param optimizePositive Whether to optimize for positive numbers
     * @return the long value read
     * @see #canReadLong()
     */
    public long readVarLong(boolean optimizePositive) throws PDKRuntimeException {
        if (require(1) < 9) return readVarLong_slow(optimizePositive);
        int p = position;
        int b = buffer[p++];
        long result = b & 0x7F;
        if ((b & 0x80) != 0) {
            byte[] buffer = this.buffer;
            b = buffer[p++];
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                b = buffer[p++];
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    b = buffer[p++];
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        b = buffer[p++];
                        result |= (long) (b & 0x7F) << 28;
                        if ((b & 0x80) != 0) {
                            b = buffer[p++];
                            result |= (long) (b & 0x7F) << 35;
                            if ((b & 0x80) != 0) {
                                b = buffer[p++];
                                result |= (long) (b & 0x7F) << 42;
                                if ((b & 0x80) != 0) {
                                    b = buffer[p++];
                                    result |= (long) (b & 0x7F) << 49;
                                    if ((b & 0x80) != 0) {
                                        b = buffer[p++];
                                        result |= (long) b << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        position = p;
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    private long readVarLong_slow(boolean optimizePositive) {
        // The buffer is guaranteed to have at least 1 byte.
        int b = buffer[position++];
        long result = b & 0x7F;
        if ((b & 0x80) != 0) {
            if (position == limit) require(1);
            byte[] buffer = this.buffer;
            b = buffer[position++];
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                if (position == limit) require(1);
                b = buffer[position++];
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    if (position == limit) require(1);
                    b = buffer[position++];
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        if (position == limit) require(1);
                        b = buffer[position++];
                        result |= (long) (b & 0x7F) << 28;
                        if ((b & 0x80) != 0) {
                            if (position == limit) require(1);
                            b = buffer[position++];
                            result |= (long) (b & 0x7F) << 35;
                            if ((b & 0x80) != 0) {
                                if (position == limit) require(1);
                                b = buffer[position++];
                                result |= (long) (b & 0x7F) << 42;
                                if ((b & 0x80) != 0) {
                                    if (position == limit) require(1);
                                    b = buffer[position++];
                                    result |= (long) (b & 0x7F) << 49;
                                    if ((b & 0x80) != 0) {
                                        if (position == limit) require(1);
                                        b = buffer[position++];
                                        result |= (long) b << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
    }

    /**
     * Returns true if enough bytes are available to read a long with {@link #readLong(boolean)}.
     */
    public boolean canReadLong() throws PDKRuntimeException {
        if (varEncoding) return canReadVarLong();
        if (limit - position >= 8) return true;
        return optional(8) == 8;
    }

    /**
     * Returns true if enough bytes are available to read a long with {@link #readVarLong(boolean)}.
     */
    public boolean canReadVarLong() throws PDKRuntimeException {
        if (limit - position >= 9) return true;
        if (optional(5) <= 0) return false;
        int p = position, limit = this.limit;
        byte[] buffer = this.buffer;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        if ((buffer[p++] & 0x80) == 0) return true;
        if (p == limit) return false;
        return true;
    }

    // float:

    /**
     * Reads a 4 byte float.
     */
    public float readFloat() throws PDKRuntimeException {
        require(4);
        byte[] buffer = this.buffer;
        int p = this.position;
        this.position = p + 4;
        return Float.intBitsToFloat(buffer[p] & 0xFF //
                | (buffer[p + 1] & 0xFF) << 8 //
                | (buffer[p + 2] & 0xFF) << 16 //
                | (buffer[p + 3] & 0xFF) << 24);
    }

    /**
     * Reads a 1-5 byte float with reduced precision.
     */
    public float readVarFloat(float precision, boolean optimizePositive) throws PDKRuntimeException {
        return readVarInt(optimizePositive) / precision;
    }

    // double:

    /**
     * Reads an 8 byte double.
     */
    public double readDouble() throws PDKRuntimeException {
        require(8);
        byte[] buffer = this.buffer;
        int p = position;
        position = p + 8;
        return Double.longBitsToDouble(buffer[p] & 0xFF //
                | (buffer[p + 1] & 0xFF) << 8 //
                | (buffer[p + 2] & 0xFF) << 16 //
                | (long) (buffer[p + 3] & 0xFF) << 24 //
                | (long) (buffer[p + 4] & 0xFF) << 32 //
                | (long) (buffer[p + 5] & 0xFF) << 40 //
                | (long) (buffer[p + 6] & 0xFF) << 48 //
                | (long) buffer[p + 7] << 56);
    }

    /**
     * Reads a 1-9 byte double with reduced precision.
     */
    public double readVarDouble(double precision, boolean optimizePositive) throws PDKRuntimeException {
        return readVarLong(optimizePositive) / precision;
    }

    // short:

    /**
     * Reads a 2 byte short.
     */
    public short readShort() throws PDKRuntimeException {
        require(2);
        int p = position;
        position = p + 2;
        return (short) ((buffer[p] & 0xFF) | ((buffer[p + 1] & 0xFF)) << 8);
    }

    /**
     * Reads a 2 byte short as an int from 0 to 65535.
     */
    public int readShortUnsigned() throws PDKRuntimeException {
        require(2);
        int p = position;
        position = p + 2;
        return (buffer[p] & 0xFF) | ((buffer[p + 1] & 0xFF)) << 8;
    }

    // char:

    /**
     * Reads a 2 byte char.
     */
    public char readChar() throws PDKRuntimeException {
        require(2);
        int p = position;
        position = p + 2;
        return (char) ((buffer[p] & 0xFF) | ((buffer[p + 1] & 0xFF)) << 8);
    }

    // boolean:

    /**
     * Reads a 1 byte boolean.
     */
    public boolean readBoolean() throws PDKRuntimeException {
        if (position == limit) require(1);
        return buffer[position++] == 1;
    }

    // String:

    /**
     * Reads the length and string of UTF8 characters, or null. This can read strings written by
     * {@link Output#writeString(String)} and {@link Output#writeAscii(String)}.
     *
     * @return May be null.
     */
    public String readString() {
        if (!readVarIntFlag()) return readAsciiString(); // ASCII.
        // Null, empty, or UTF8.
        int charCount = readVarIntFlag(true);
        switch (charCount) {
            case 0:
                return null;
            case 1:
                return "";
        }
        charCount--;
        readUtf8Chars(charCount);
        return new String(chars, 0, charCount);
    }

    /**
     * Reads the length and string of UTF8 characters, or null. For non-ASCII strings, this method avoids allocating a string by
     * reading directly to the StringBuilder. This can read strings written by {@link Output#writeString(String)} and
     * {@link Output#writeAscii(String)}.
     *
     * @return May be null.
     */
    public StringBuilder readStringBuilder() {
        if (!readVarIntFlag()) return new StringBuilder(readAsciiString()); // ASCII.
        // Null, empty, or UTF8.
        int charCount = readVarIntFlag(true);
        switch (charCount) {
            case 0:
                return null;
            case 1:
                return new StringBuilder(0);
        }
        charCount--;
        readUtf8Chars(charCount);
        StringBuilder builder = new StringBuilder(charCount);
        builder.append(chars, 0, charCount);
        return builder;
    }

    private void readUtf8Chars(int charCount) {
        if (chars.length < charCount) chars = new char[charCount];
        byte[] buffer = this.buffer;
        char[] chars = this.chars;
        // Try to read 7 bit ASCII chars.
        int charIndex = 0;
        int count = Math.min(require(1), charCount);
        int p = position, b;
        while (charIndex < count) {
            b = buffer[p++];
            if (b < 0) {
                p--;
                break;
            }
            chars[charIndex++] = (char) b;
        }
        position = p;
        // If buffer didn't hold all chars or any were not ASCII, use slow path for remainder.
        if (charIndex < charCount) readUtf8Chars_slow(charCount, charIndex);
    }

    private void readUtf8Chars_slow(int charCount, int charIndex) {
        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        while (charIndex < charCount) {
            if (position == limit) require(1);
            int b = buffer[position++] & 0xFF;
            switch (b >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    chars[charIndex] = (char) b;
                    break;
                case 12:
                case 13:
                    if (position == limit) require(1);
                    chars[charIndex] = (char) ((b & 0x1F) << 6 | buffer[position++] & 0x3F);
                    break;
                case 14:
                    require(2);
                    int p = position;
                    position = p + 2;
                    chars[charIndex] = (char) ((b & 0x0F) << 12 | (buffer[p] & 0x3F) << 6 | buffer[p + 1] & 0x3F);
                    break;
            }
            charIndex++;
        }
    }

    private String readAsciiString() {
        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        int p = position;
        int charCount = 0;
        for (int n = Math.min(chars.length, limit - position); charCount < n; charCount++, p++) {
            int b = buffer[p];
            if ((b & 0x80) == 0x80) {
                position = p + 1;
                chars[charCount] = (char) (b & 0x7F);
                return new String(chars, 0, charCount + 1);
            }
            chars[charCount] = (char) b;
        }
        position = p;
        return readAscii_slow(charCount);
    }

    private String readAscii_slow(int charCount) {
        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        while (true) {
            if (position == limit) require(1);
            int b = buffer[position++];
            if (charCount == chars.length) {
                char[] newChars = new char[charCount * 2];
                System.arraycopy(chars, 0, newChars, 0, charCount);
                chars = newChars;
                this.chars = newChars;
            }
            if ((b & 0x80) == 0x80) {
                chars[charCount] = (char) (b & 0x7F);
                return new String(chars, 0, charCount + 1);
            }
            chars[charCount++] = (char) b;
        }
    }

    // Primitive arrays:

    /**
     * Reads an int array in bulk. This may be more efficient than reading them individually.
     */
    public int[] readInts(int length) throws PDKRuntimeException {
        int[] array = new int[length];
        if (optional(length << 2) == length << 2) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 4) {
                array[i] = buffer[p] & 0xFF //
                        | (buffer[p + 1] & 0xFF) << 8 //
                        | (buffer[p + 2] & 0xFF) << 16 //
                        | (buffer[p + 3] & 0xFF) << 24;
            }
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readInt();
        }
        return array;
    }

    /**
     * Reads an int array in bulk using fixed or variable length encoding, depending on
     * {@link #setVariableLengthEncoding(boolean)}. This may be more efficient than reading them individually.
     */
    public int[] readInts(int length, boolean optimizePositive) throws PDKRuntimeException {
        if (varEncoding) {
            int[] array = new int[length];
            for (int i = 0; i < length; i++)
                array[i] = readVarInt(optimizePositive);
            return array;
        }
        return readInts(length);
    }

    /**
     * Reads a long array in bulk. This may be more efficient than reading them individually.
     */
    public long[] readLongs(int length) throws PDKRuntimeException {
        long[] array = new long[length];
        if (optional(length << 3) == length << 3) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 8) {
                array[i] = buffer[p] & 0xFF //
                        | (buffer[p + 1] & 0xFF) << 8 //
                        | (buffer[p + 2] & 0xFF) << 16 //
                        | (long) (buffer[p + 3] & 0xFF) << 24 //
                        | (long) (buffer[p + 4] & 0xFF) << 32 //
                        | (long) (buffer[p + 5] & 0xFF) << 40 //
                        | (long) (buffer[p + 6] & 0xFF) << 48 //
                        | (long) buffer[p + 7] << 56;
            }
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readLong();
        }
        return array;
    }

    /**
     * Reads an int array in bulk using fixed or variable length encoding, depending on
     * {@link #setVariableLengthEncoding(boolean)}. This may be more efficient than reading them individually.
     */
    public long[] readLongs(int length, boolean optimizePositive) throws PDKRuntimeException {
        if (varEncoding) {
            long[] array = new long[length];
            for (int i = 0; i < length; i++)
                array[i] = readVarLong(optimizePositive);
            return array;
        }
        return readLongs(length);
    }

    /**
     * Reads a float array in bulk. This may be more efficient than reading them individually.
     */
    public float[] readFloats(int length) throws PDKRuntimeException {
        float[] array = new float[length];
        if (optional(length << 2) == length << 2) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 4) {
                array[i] = Float.intBitsToFloat(buffer[p] & 0xFF //
                        | (buffer[p + 1] & 0xFF) << 8 //
                        | (buffer[p + 2] & 0xFF) << 16 //
                        | (buffer[p + 3] & 0xFF) << 24);
            }
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readFloat();
        }
        return array;
    }

    /**
     * Reads a double array in bulk. This may be more efficient than reading them individually.
     *
     * @param length number of double to read
     * @return read double values
     * @throws PDKRuntimeException for reading error
     */
    public double[] readDoubles(int length) throws PDKRuntimeException {
        double[] array = new double[length];
        if (optional(length << 3) == length << 3) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 8) {
                array[i] = Double.longBitsToDouble(buffer[p] & 0xFF //
                        | (buffer[p + 1] & 0xFF) << 8 //
                        | (buffer[p + 2] & 0xFF) << 16 //
                        | (long) (buffer[p + 3] & 0xFF) << 24 //
                        | (long) (buffer[p + 4] & 0xFF) << 32 //
                        | (long) (buffer[p + 5] & 0xFF) << 40 //
                        | (long) (buffer[p + 6] & 0xFF) << 48 //
                        | (long) buffer[p + 7] << 56);
            }
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readDouble();
        }
        return array;
    }

    /**
     * Reads a short array in bulk. This may be more efficient than reading them individually.
     *
     * @param length number of short to read
     * @return read short values
     * @throws PDKRuntimeException for reading error
     */
    public short[] readShorts(int length) throws PDKRuntimeException {
        short[] array = new short[length];
        if (optional(length << 1) == length << 1) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 2)
                array[i] = (short) ((buffer[p] & 0xFF) | ((buffer[p + 1] & 0xFF)) << 8);
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readShort();
        }
        return array;
    }

    /**
     * Reads a char array in bulk. This may be more efficient than reading them individually.
     *
     * @param length number of char to read
     * @return read char values
     * @throws PDKRuntimeException for reading error
     */
    public char[] readChars(int length) throws PDKRuntimeException {
        char[] array = new char[length];
        if (optional(length << 1) == length << 1) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p += 2)
                array[i] = (char) ((buffer[p] & 0xFF) | ((buffer[p + 1] & 0xFF)) << 8);
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readChar();
        }
        return array;
    }

    /**
     * Reads a boolean array in bulk. This may be more efficient than reading them individually.
     *
     * @param length number of boolean to read
     * @return read boolean values
     * @throws PDKRuntimeException for reading error
     */
    public boolean[] readBooleans(int length) throws PDKRuntimeException {
        boolean[] array = new boolean[length];
        if (optional(length) == length) {
            byte[] buffer = this.buffer;
            int p = this.position;
            for (int i = 0; i < length; i++, p++)
                array[i] = buffer[p] != 0;
            position = p;
        } else {
            for (int i = 0; i < length; i++)
                array[i] = readBoolean();
        }
        return array;
    }
}
