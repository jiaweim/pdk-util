package pdk.util.data;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;
import pdk.util.io.BinaryIOUtils;
import pdk.util.io.Input;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:43 AM
 */
class Point1DTest {


    @Test
    void shouldRoundtripUsingInputOutput() {
        Point1D original = Point.of(3.14);
        byte[] bytes = BinaryIOUtils.write(original);
        Point1D restored = Point1D.fromBinary(new Input(bytes));

        assertNotSame(original, restored, "Deserialized object should be a new instance");
        assertEquals(original, restored, "Objects should be equal after roundtrip");
    }

    @Test
    void shouldRoundtripUsingBinaryIO() {
        Point1D original = Point.of(-2.5);
        byte[] data = BinaryIOUtils.write(original);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);

        assertEquals(original, restored);
    }

    @Test
    void shouldHandleZero() {
        Point1D zero = Point.of(0.0);
        byte[] data = BinaryIOUtils.write(zero);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);
        assertEquals(0.0, restored.getX());
    }

    @Test
    void shouldHandleNaN() {
        Point1D nan = Point.of(Double.NaN);
        byte[] data = BinaryIOUtils.write(nan);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);
        assertTrue(Double.isNaN(restored.getX()));
    }

    @Test
    void shouldHandlePositiveInfinity() {
        Point1D inf = Point.of(Double.POSITIVE_INFINITY);
        byte[] data = BinaryIOUtils.write(inf);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);
        assertEquals(Double.POSITIVE_INFINITY, restored.getX());
    }

    @Test
    void shouldHandleNegativeInfinity() {
        Point1D negInf = Point.of(Double.NEGATIVE_INFINITY);
        byte[] data = BinaryIOUtils.write(negInf);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);
        assertEquals(Double.NEGATIVE_INFINITY, restored.getX());
    }

    @Test
    void serializedFormShouldBeExactly8Bytes() {
        // Point writes exactly one double
        byte[] data = BinaryIOUtils.write(Point.of(1.0));
        assertEquals(8, data.length, "Serialized double should occupy 8 bytes");
    }


    @Test
    void deserializedObjectShouldBeNewInstance() {
        Point1D original = Point.of(10.0);
        byte[] data = BinaryIOUtils.write(original);
        Point1D restored = BinaryIOUtils.read(new Input(data), Point1D.class);

        assertNotSame(original, restored);
        // Optionally, verify that the 'x' field cannot be modified (it is final)
    }
}