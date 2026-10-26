package pdk.util.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.PDKRuntimeException;
import pdk.util.data.Point;
import pdk.util.data.Point1D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:18 AM
 */
class BinaryIOUtilsTest {
    @TempDir
    Path tempDir;

    // ──────────── Basic roundtrip ────────────

    @Test
    void shouldWriteAndReadBackPoint() {
        Point1D original = Point.of(3.14);
        Path file = tempDir.resolve("point.bin");

        BinaryIOUtils.write(original, file);
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertEquals(original, restored, "Object should survive file roundtrip");
        assertNotSame(original, restored);
    }

    @Test
    void shouldOverwriteExistingFile() {
        Point1D first = Point.of(1.0);
        Point1D second = Point.of(2.0);
        Path file = tempDir.resolve("overwrite.bin");

        BinaryIOUtils.write(first, file);
        BinaryIOUtils.write(second, file);   // overwrite
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertEquals(second, restored);
    }

    @Test
    void shouldHandleZero() {
        Point1D zero = Point.of(0.0);
        Path file = tempDir.resolve("zero.bin");

        BinaryIOUtils.write(zero, file);
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertEquals(0.0, restored.getX());
    }

    @Test
    void shouldHandleNaN() {
        Point1D nan = Point.of(Double.NaN);
        Path file = tempDir.resolve("nan.bin");

        BinaryIOUtils.write(nan, file);
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertTrue(Double.isNaN(restored.getX()));
    }

    @Test
    void shouldHandleInfinity() {
        Point1D posInf = Point.of(Double.POSITIVE_INFINITY);
        Point1D negInf = Point.of(Double.NEGATIVE_INFINITY);
        Path posFile = tempDir.resolve("posInf.bin");
        Path negFile = tempDir.resolve("negInf.bin");

        BinaryIOUtils.write(posInf, posFile);
        BinaryIOUtils.write(negInf, negFile);

        assertEquals(Double.POSITIVE_INFINITY,
                BinaryIOUtils.read(posFile, Point1D.class).getX());
        assertEquals(Double.NEGATIVE_INFINITY,
                BinaryIOUtils.read(negFile, Point1D.class).getX());
    }

    // ──────────── Buffer size variants ────────────

    @Test
    void shouldWorkWithCustomBufferSize() {
        Point1D original = Point.of(42.0);
        Path file = tempDir.resolve("customBuffer.bin");

        BinaryIOUtils.write(original, file, 16);   // tiny buffer
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertEquals(original, restored);
    }

    @Test
    void shouldWorkWithDefaultBuffer() {
        Point1D original = Point.of(99.9);
        Path file = tempDir.resolve("defaultBuffer.bin");

        // Uses the single-arg overload (default 4096 buffer)
        BinaryIOUtils.write(original, file);
        Point1D restored = BinaryIOUtils.read(file, Point1D.class);

        assertEquals(original, restored);
    }

    // ──────────── Error conditions ────────────

    @Test
    void shouldThrowWhenReadingNonexistentFile() {
        Path missing = tempDir.resolve("doesNotExist.bin");

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIOUtils.read(missing, Point1D.class));
    }

    @Test
    void shouldThrowWhenWritingToDirectory() throws IOException {
        Path dir = tempDir.resolve("aDirectory");
        Files.createDirectory(dir);

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIOUtils.write(Point.of(1.0), dir));
    }

    @Test
    void shouldThrowWhenReadingCorruptedFile() throws IOException {
        Path file = tempDir.resolve("corrupted.bin");
        Files.write(file, new byte[]{0x01, 0x02});  // not a valid Point (8 bytes expected)

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIOUtils.read(file, Point1D.class));
    }
}