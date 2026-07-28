package pdk.util.data;

import org.junit.jupiter.api.Test;
import pdk.util.io.BinaryIO;
import pdk.util.io.Input;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:43 AM
 */
class PointTest {


    @Test
    void shouldRoundtripUsingInputOutput() {
        Point original = Point.of(3.14);
        byte[] bytes = BinaryIO.serialize(original);
        Point restored = Point.fromBinary(new Input(bytes));

        assertNotSame(original, restored, "Deserialized object should be a new instance");
        assertEquals(original, restored, "Objects should be equal after roundtrip");
    }

    @Test
    void shouldRoundtripUsingBinaryIO() {
        Point original = Point.of(-2.5);
        byte[] data = BinaryIO.serialize(original);
        Point restored = BinaryIO.read(new Input(data), Point.class);

        assertEquals(original, restored);
    }

    @Test
    void shouldHandleZero() {
        Point zero = Point.of(0.0);
        byte[] data = BinaryIO.serialize(zero);
        Point restored = BinaryIO.read(new Input(data), Point.class);
        assertEquals(0.0, restored.getX());
    }

    @Test
    void shouldHandleNaN() {
        Point nan = Point.of(Double.NaN);
        byte[] data = BinaryIO.serialize(nan);
        Point restored = BinaryIO.read(new Input(data), Point.class);
        assertTrue(Double.isNaN(restored.getX()));
    }

    @Test
    void shouldHandlePositiveInfinity() {
        Point inf = Point.of(Double.POSITIVE_INFINITY);
        byte[] data = BinaryIO.serialize(inf);
        Point restored = BinaryIO.read(new Input(data), Point.class);
        assertEquals(Double.POSITIVE_INFINITY, restored.getX());
    }

    @Test
    void shouldHandleNegativeInfinity() {
        Point negInf = Point.of(Double.NEGATIVE_INFINITY);
        byte[] data = BinaryIO.serialize(negInf);
        Point restored = BinaryIO.read(new Input(data), Point.class);
        assertEquals(Double.NEGATIVE_INFINITY, restored.getX());
    }

    @Test
    void serializedFormShouldBeExactly8Bytes() {
        // Point writes exactly one double
        byte[] data = BinaryIO.serialize(Point.of(1.0));
        assertEquals(8, data.length, "Serialized double should occupy 8 bytes");
    }


    @Test
    void deserializedObjectShouldBeNewInstance() {
        Point original = Point.of(10.0);
        byte[] data = BinaryIO.serialize(original);
        Point restored = BinaryIO.read(new Input(data), Point.class);

        assertNotSame(original, restored);
        // Optionally, verify that the 'x' field cannot be modified (it is final)
    }
}