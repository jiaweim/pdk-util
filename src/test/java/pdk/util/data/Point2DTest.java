package pdk.util.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.io.BinaryIO;
import pdk.util.io.Input;
import pdk.util.io.Output;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 11:10 AM
 */
class Point2DTest {

    @TempDir
    Path tempDir;

    // ────────────────── Basic roundtrip ──────────────────

    @Test
    void shouldRoundtripUsingBinaryIO() {
        Point2D original = new Point2D(3.14, -2.71);
        byte[] data = BinaryIO.serialize(original);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        assertEquals(original, restored, "Object should survive binary roundtrip");
        assertNotSame(original, restored);
    }

    @Test
    void shouldRoundtripUsingDirectInputOutput() {
        Point2D original = new Point2D(1.0, 2.0);
        Output out = new Output(256, -1);
        try {
            original.write(out);
        } finally {
            out.close();
        }

        Input in = new Input(out.toBytes());
        Point2D restored = Point2D.fromBinary(in);

        assertEquals(original, restored);
    }


    // ────────────────── File roundtrip ──────────────────

    @Test
    void shouldWriteToAndReadFromFile() {
        Point2D original = new Point2D(10.5, -0.5);
        Path file = tempDir.resolve("point2d.bin");

        BinaryIO.writeToFile(original, file);
        Point2D restored = BinaryIO.readFromFile(file, Point2D.class);

        assertEquals(original, restored);
    }

    // ────────────────── Inherited fields ──────────────────

    @Test
    void shouldSerializeBothXAndY() {
        Point2D original = new Point2D(5.0, 7.0);
        byte[] data = BinaryIO.serialize(original);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        // Both coordinates must be preserved
        assertEquals(5.0, restored.getX());
        assertEquals(7.0, restored.getY());
    }

    // ────────────────── Special double values ──────────────────

    @Test
    void shouldHandleNaN() {
        Point2D nan = new Point2D(Double.NaN, Double.NaN);
        byte[] data = BinaryIO.serialize(nan);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        assertTrue(Double.isNaN(restored.getX()));
        assertTrue(Double.isNaN(restored.getY()));
    }

    @Test
    void shouldHandleInfinity() {
        Point2D posInf = new Point2D(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        byte[] data = BinaryIO.serialize(posInf);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        assertEquals(Double.POSITIVE_INFINITY, restored.getX());
        assertEquals(Double.NEGATIVE_INFINITY, restored.getY());
    }

    @Test
    void shouldHandleZero() {
        Point2D zero = new Point2D(0.0, 0.0);
        byte[] data = BinaryIO.serialize(zero);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        assertEquals(0.0, restored.getX());
        assertEquals(0.0, restored.getY());
    }

    // ────────────────── Byte size verification ──────────────────

    @Test
    void serializedFormShouldBeExactly16Bytes() {
        // Two doubles = 8 bytes each
        Point2D p = new Point2D(1.0, 2.0);
        byte[] data = BinaryIO.serialize(p);
        assertEquals(16, data.length, "Serialized Point2D should occupy 16 bytes");
    }

    // ────────────────── Immutability check ──────────────────

    @Test
    void deserializedInstanceShouldBeNewObject() {
        Point2D original = new Point2D(3.0, 4.0);
        byte[] data = BinaryIO.serialize(original);
        Point2D restored = BinaryIO.read(new Input(data), Point2D.class);

        assertNotSame(original, restored);
        // y field is final, no setter – immutability is enforced by the compiler
    }

    // ────────────────── Equality and hash code after roundtrip ──────────────────

    @Test
    void equalsAndHashCodeShouldBePreserved() {
        Point2D p1 = new Point2D(1.23, 4.56);
        Point2D p2 = BinaryIO.read(new Input(BinaryIO.serialize(p1)), Point2D.class);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}