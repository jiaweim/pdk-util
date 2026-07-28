package pdk.util.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.PDKRuntimeException;
import pdk.util.data.Point;

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
class BinaryIOTest {
    @TempDir
    Path tempDir;

    // ──────────── Basic roundtrip ────────────

    @Test
    void shouldWriteAndReadBackPoint() {
        Point original = Point.of(3.14);
        Path file = tempDir.resolve("point.bin");

        BinaryIO.writeToFile(original, file);
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertEquals(original, restored, "Object should survive file roundtrip");
        assertNotSame(original, restored);
    }

    @Test
    void shouldOverwriteExistingFile() {
        Point first = Point.of(1.0);
        Point second = Point.of(2.0);
        Path file = tempDir.resolve("overwrite.bin");

        BinaryIO.writeToFile(first, file);
        BinaryIO.writeToFile(second, file);   // overwrite
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertEquals(second, restored);
    }

    @Test
    void shouldHandleZero() {
        Point zero = Point.of(0.0);
        Path file = tempDir.resolve("zero.bin");

        BinaryIO.writeToFile(zero, file);
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertEquals(0.0, restored.getX());
    }

    @Test
    void shouldHandleNaN() {
        Point nan = Point.of(Double.NaN);
        Path file = tempDir.resolve("nan.bin");

        BinaryIO.writeToFile(nan, file);
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertTrue(Double.isNaN(restored.getX()));
    }

    @Test
    void shouldHandleInfinity() {
        Point posInf = Point.of(Double.POSITIVE_INFINITY);
        Point negInf = Point.of(Double.NEGATIVE_INFINITY);
        Path posFile = tempDir.resolve("posInf.bin");
        Path negFile = tempDir.resolve("negInf.bin");

        BinaryIO.writeToFile(posInf, posFile);
        BinaryIO.writeToFile(negInf, negFile);

        assertEquals(Double.POSITIVE_INFINITY,
                BinaryIO.readFromFile(posFile, Point.class).getX());
        assertEquals(Double.NEGATIVE_INFINITY,
                BinaryIO.readFromFile(negFile, Point.class).getX());
    }

    // ──────────── Buffer size variants ────────────

    @Test
    void shouldWorkWithCustomBufferSize() {
        Point original = Point.of(42.0);
        Path file = tempDir.resolve("customBuffer.bin");

        BinaryIO.writeToFile(original, file, 16);   // tiny buffer
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertEquals(original, restored);
    }

    @Test
    void shouldWorkWithDefaultBuffer() {
        Point original = Point.of(99.9);
        Path file = tempDir.resolve("defaultBuffer.bin");

        // Uses the single-arg overload (default 4096 buffer)
        BinaryIO.writeToFile(original, file);
        Point restored = BinaryIO.readFromFile(file, Point.class);

        assertEquals(original, restored);
    }

    // ──────────── Error conditions ────────────

    @Test
    void shouldThrowWhenReadingNonexistentFile() {
        Path missing = tempDir.resolve("doesNotExist.bin");

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIO.readFromFile(missing, Point.class));
    }

    @Test
    void shouldThrowWhenWritingToDirectory() throws IOException {
        Path dir = tempDir.resolve("aDirectory");
        Files.createDirectory(dir);

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIO.writeToFile(Point.of(1.0), dir));
    }

    @Test
    void shouldThrowWhenReadingCorruptedFile() throws IOException {
        Path file = tempDir.resolve("corrupted.bin");
        Files.write(file, new byte[]{0x01, 0x02});  // not a valid Point (8 bytes expected)

        assertThrows(PDKRuntimeException.class,
                () -> BinaryIO.readFromFile(file, Point.class));
    }
}