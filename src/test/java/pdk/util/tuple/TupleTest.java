package pdk.util.tuple;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 Jun 2025, 5:16 PM
 */
class TupleTest {

    // tuple 0
    @Test
    public void shouldCreateEmptyTuple() {
        assertEquals("()", Tuple.empty().toString());
    }

    @Test
    public void shouldHashTuple0() {
        assertEquals(Objects.hash(), Tuple.empty().hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple0() {
        assertEquals(0, Tuple.empty().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple0Instances() {
        final Tuple0 t = tuple0();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple0EqualsNull() {
        assertNotEquals(null, tuple0());
    }

    @Test
    public void shouldNotTuple0EqualsObject() {
        assertNotEquals(new Object(), tuple0());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldTuple0EqualTuple0() {
        assertEquals(tuple0(), tuple0());
    }

    @Test
    public void shouldDeserializeSingletonOfTuple0() {
        final Object tuple0 = SerializationUtils.deserialize(SerializationUtils.serialize(Tuple0.instance()));
        assertSame(tuple0, Tuple0.instance());
    }

    @Test
    public void shouldReturnComparator() {
        assertEquals(0, Tuple0.comparator().compare(Tuple0.instance(), Tuple0.instance()));
    }

    @Test
    public void shouldApplyTuple0() {
        assertEquals(1, Tuple0.instance().apply(() -> 1));
    }

    @Test
    public void shouldCompareTuple0() {
        assertEquals(0, Tuple0.instance().compareTo(Tuple0.instance()));
    }

    // -- Tuple1

    @Test
    public void shouldCreateSingle() {
        assertEquals("(1)", tuple1().toString());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple1() {
        assertEquals(1, tuple1().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple1Instances() {
        final Tuple1<?> t = tuple1();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple1EqualsNull() {
        assertNotEquals(null, tuple1());
    }

    @Test
    public void shouldNotTuple1EqualsObject() {
        assertNotEquals(new Object(), tuple1());
    }

    @Test
    public void shouldTuple1EqualTuple1() {
        assertEquals(tuple1(), tuple1());
    }

    @Test
    public void shouldNarrowTuple1() {
        final Tuple1<Double> wideTuple = Tuple.of(1.0d);
        final Tuple1<Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals(1.0d, narrowTuple._1());
    }

    // -- Tuple2

    @Test
    public void shouldCreatePair() {
        assertEquals("(1, 2)", tuple2().toString());
    }

    @Test
    public void shouldCreateTuple2FromEntry() {
        final Tuple2<Integer, Integer> tuple2FromEntry = Tuple.fromEntry(new AbstractMap.SimpleEntry<>(1, 2));
        assertEquals("(1, 2)", tuple2FromEntry.toString());
    }

    @Test
    public void shouldHashTuple2() {
        final Tuple2<?, ?> t = tuple2();
        assertEquals(Objects.hash(t._1, t._2), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple2() {
        assertEquals(2, tuple2().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple2Instances() {
        final Tuple2<?, ?> t = tuple2();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple2EqualsNull() {
        assertNotEquals(null, tuple2());
    }

    @Test
    public void shouldNotTuple2EqualsObject() {
        assertNotEquals(new Object(), tuple2());
    }

    @Test
    public void shouldTuple2EqualTuple2() {
        assertEquals(tuple2(), tuple2());
    }

    @Test
    public void shouldCompareTuple2() {
        assertEquals(0, Tuple.of(1, 1).compareTo(Tuple.of(1, 1)));
        assertEquals(1, Tuple.of(2, 1).compareTo(Tuple.of(1, 1)));
        assertEquals(1, Tuple.of(1, 2).compareTo(Tuple.of(1, 1)));
        assertEquals(-1, Tuple.of(1, 1).compareTo(Tuple.of(2, 1)));
        assertEquals(-1, Tuple.of(1, 1).compareTo(Tuple.of(1, 2)));
    }

    @Test
    public void shouldNarrowTuple2() {
        final Tuple2<String, Double> wideTuple = Tuple.of("test", 1.0d);
        final Tuple2<CharSequence, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("test", narrowTuple._1());
        assertEquals(1.0d, narrowTuple._2());
    }

    // -- Tuple3

    @Test
    public void shouldCreateTriple() {
        assertEquals("(1, 2, 3)", tuple3().toString());
    }

    @Test
    public void shouldHashTuple3() {
        final Tuple3<?, ?, ?> t = tuple3();
        assertEquals(Objects.hash(t._1, t._2, t._3), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple3() {
        assertEquals(3, tuple3().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple3Instances() {
        final Tuple3<?, ?, ?> t = tuple3();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple3EqualsNull() {
        assertNotEquals(null, tuple3());
    }

    @Test
    public void shouldNotTuple3EqualsObject() {
        assertNotEquals(new Object(), tuple3());
    }

    @Test
    public void shouldTuple3EqualTuple3() {
        assertEquals(tuple3(), tuple3());
    }

    @Test
    public void shouldNarrowTuple3() {
        final Tuple3<String, Double, Float> wideTuple = Tuple.of("zero", 1.0D, 2.0F);
        final Tuple3<CharSequence, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
    }

    // -- Tuple4

    @Test
    public void shouldCreateQuadruple() {
        assertEquals("(1, 2, 3, 4)", tuple4().toString());
    }

    @Test
    public void shouldHashTuple4() {
        final Tuple4<?, ?, ?, ?> t = tuple4();
        assertEquals(Objects.hash(t._1, t._2, t._3, t._4), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple4() {
        assertEquals(4, tuple4().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple4Instances() {
        final Tuple4<?, ?, ?, ?> t = tuple4();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple4EqualsNull() {
        assertNotEquals(null, tuple4());
    }

    @Test
    public void shouldNotTuple4EqualsObject() {
        assertNotEquals(new Object(), tuple4());
    }

    @Test
    public void shouldTuple4EqualTuple4() {
        assertEquals(tuple4(), tuple4());
    }

    @Test
    public void shouldNarrowTuple4() {
        final Tuple4<String, Double, Float, Integer> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3);
        final Tuple4<CharSequence, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
        assertEquals(3, narrowTuple._4());
    }

    // -- Tuple5

    @Test
    public void shouldCreateQuintuple() {
        assertEquals("(1, 2, 3, 4, 5)", tuple5().toString());
    }

    @Test
    public void shouldHashTuple5() {
        final Tuple5<?, ?, ?, ?, ?> t = tuple5();
        assertEquals(Objects.hash(t._1, t._2, t._3, t._4, t._5), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple5() {
        assertEquals(5, tuple5().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple5Instances() {
        final Tuple5<?, ?, ?, ?, ?> t = tuple5();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple5EqualsNull() {
        assertNotEquals(null, tuple5());
    }

    @Test
    public void shouldNotTuple5EqualsObject() {
        assertNotEquals(new Object(), tuple5());
    }

    @Test
    public void shouldTuple5EqualTuple5() {
        assertEquals(tuple5(), tuple5());
    }

    @Test
    public void shouldNarrowTuple5() {
        final Tuple5<String, Double, Float, Integer, Long> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L);
        final Tuple5<CharSequence, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
        assertEquals(3, narrowTuple._4());
        assertEquals(4L, narrowTuple._5());
    }

    // -- Tuple6

    @Test
    public void shouldCreateSextuple() {
        assertEquals("(1, 2, 3, 4, 5, 6)", tuple6().toString());
    }

    @Test
    public void shouldHashTuple6() {
        final Tuple6<?, ?, ?, ?, ?, ?> t = tuple6();
        assertEquals(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple6() {
        assertEquals(6, tuple6().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple6Instances() {
        final Tuple6<?, ?, ?, ?, ?, ?> t = tuple6();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple6EqualsNull() {
        assertNotEquals(null, tuple6());
    }

    @Test
    public void shouldNotTuple6EqualsObject() {
        assertNotEquals(new Object(), tuple6());
    }

    @Test
    public void shouldTuple6EqualTuple6() {
        assertEquals(tuple6(), tuple6());
    }

    @Test
    public void shouldNarrowTuple6() {
        final Tuple6<String, Double, Float, Integer, Long, Byte> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5);
        final Tuple6<CharSequence, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
        assertEquals(3, narrowTuple._4());
        assertEquals(4L, narrowTuple._5());
        assertEquals((byte) 5, narrowTuple._6());
    }

    // -- Tuple7

    @Test
    public void shouldCreateTuple7() {
        assertEquals("(1, 2, 3, 4, 5, 6, 7)", tuple7().toString());
    }

    @Test
    public void shouldHashTuple7() {
        final Tuple7<?, ?, ?, ?, ?, ?, ?> t = tuple7();
        assertEquals(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple7() {
        assertEquals(7, tuple7().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple7Instances() {
        final Tuple7<?, ?, ?, ?, ?, ?, ?> t = tuple7();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple7EqualsNull() {
        assertNotEquals(null, tuple7());
    }

    @Test
    public void shouldNotTuple7EqualsObject() {
        assertNotEquals(new Object(), tuple7());
    }

    @Test
    public void shouldTuple7EqualTuple7() {
        assertEquals(tuple7(), tuple7());
    }

    @Test
    public void shouldNarrowTuple7() {
        final Tuple7<String, Double, Float, Integer, Long, Byte, Short> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5, (short) 6);
        final Tuple7<CharSequence, Number, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
        assertEquals(3, narrowTuple._4());
        assertEquals(4L, narrowTuple._5());
        assertEquals((byte) 5, narrowTuple._6());
        assertEquals((short) 6, narrowTuple._7());
    }

    // -- Tuple8

    @Test
    public void shouldCreateTuple8() {
        assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", tuple8().toString());
    }

    @Test
    public void shouldHashTuple8() {
        final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> t = tuple8();
        assertEquals(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8), t.hashCode());
    }

    @Test
    public void shouldReturnCorrectSizeOfTuple8() {
        assertEquals(8, tuple8().size());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple8Instances() {
        final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> t = tuple8();
        assertEquals(t, t);
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple8EqualsNull() {
        assertNotEquals(null, tuple8());
    }

    @Test
    public void shouldNotTuple8EqualsObject() {
        assertNotEquals(new Object(), tuple8());
    }

    @Test
    public void shouldTuple8EqualTuple8() {
        assertEquals(tuple8(), tuple8());
    }

    @Test
    public void shouldNarrowTuple8() {
        final Tuple8<String, Double, Float, Integer, Long, Byte, Short, BigDecimal> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5, (short) 6, new BigDecimal(7));
        final Tuple8<CharSequence, Number, Number, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);

        assertEquals("zero", narrowTuple._1());
        assertEquals(1.0D, narrowTuple._2());
        assertEquals(2.0F, narrowTuple._3());
        assertEquals(3, narrowTuple._4());
        assertEquals(4L, narrowTuple._5());
        assertEquals((byte) 5, narrowTuple._6());
        assertEquals((short) 6, narrowTuple._7());
        assertEquals(new BigDecimal(7), narrowTuple._8());
    }

    // -- nested tuples

    @Test
    public void shouldDetectEqualityOnTupleOfTuples() {

        final Tuple tupleA = Tuple.of(Tuple.of(1), Tuple.of(1));
        final Tuple tupleB = Tuple.of(Tuple.of(1), Tuple.of(1));

        assertEquals(tupleA, tupleB);
    }

    @Test
    public void shouldDetectUnequalityOnTupleOfTuples() {

        final Tuple tupleA = Tuple.of(Tuple.of(1), Tuple.of(1));
        final Tuple tupleB = Tuple.of(Tuple.of(1), Tuple.of(2));

        assertNotEquals(tupleA, tupleB);
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeTuple0() {
        final Object actual = SerializationUtils.deserialize(SerializationUtils.serialize(Tuple0.instance()));
        final Object expected = Tuple0.instance();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = SerializationUtils.deserialize(SerializationUtils.serialize(Tuple0.instance())) == Tuple0.instance();
        assertTrue(actual);
    }

    @Test
    public void shouldSerializeDeserializeNonEmptyTuple() {
        final Object actual = SerializationUtils.deserialize(SerializationUtils.serialize(Tuple.of(1, 2, 3)));
        final Object expected = Tuple.of(1, 2, 3);
        assertEquals(expected, actual);
    }

    // -- helpers

    private Tuple0 tuple0() {
        return Tuple.empty();
    }

    private Tuple1<?> tuple1() {
        return Tuple.of(1);
    }

    private Tuple2<?, ?> tuple2() {
        return Tuple.of(1, 2);
    }

    private Tuple3<?, ?, ?> tuple3() {
        return Tuple.of(1, 2, 3);
    }

    private Tuple4<?, ?, ?, ?> tuple4() {
        return Tuple.of(1, 2, 3, 4);
    }

    private Tuple5<?, ?, ?, ?, ?> tuple5() {
        return Tuple.of(1, 2, 3, 4, 5);
    }

    private Tuple6<?, ?, ?, ?, ?, ?> tuple6() {
        return Tuple.of(1, 2, 3, 4, 5, 6);
    }

    private Tuple7<?, ?, ?, ?, ?, ?, ?> tuple7() {
        return Tuple.of(1, 2, 3, 4, 5, 6, 7);
    }

    private Tuple8<?, ?, ?, ?, ?, ?, ?, ?> tuple8() {
        return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
    }

    private Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> tuple9() {
        return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}