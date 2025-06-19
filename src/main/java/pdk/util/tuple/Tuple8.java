package pdk.util.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A tuple of 8 elements
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 */
public final class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>
        implements Tuple, Comparable<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>, Serializable {

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;
    public final T7 _7;
    public final T8 _8;

    /**
     * Constructs a tuple of 8 elements.
     */
    public Tuple8(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> Comparator<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp, Comparator<? super T3> t3Comp, Comparator<? super T4> t4Comp, Comparator<? super T5> t5Comp, Comparator<? super T6> t6Comp, Comparator<? super T7> t7Comp, Comparator<? super T8> t8Comp) {
        return (Comparator<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            final int check2 = t2Comp.compare(t1._2, t2._2);
            if (check2 != 0) {
                return check2;
            }

            final int check3 = t3Comp.compare(t1._3, t2._3);
            if (check3 != 0) {
                return check3;
            }

            final int check4 = t4Comp.compare(t1._4, t2._4);
            if (check4 != 0) {
                return check4;
            }

            final int check5 = t5Comp.compare(t1._5, t2._5);
            if (check5 != 0) {
                return check5;
            }

            final int check6 = t6Comp.compare(t1._6, t2._6);
            if (check6 != 0) {
                return check6;
            }

            final int check7 = t7Comp.compare(t1._7, t2._7);
            if (check7 != 0) {
                return check7;
            }

            final int check8 = t8Comp.compare(t1._8, t2._8);
            if (check8 != 0) {
                return check8;
            }

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>, U4 extends Comparable<? super U4>, U5 extends Comparable<? super U5>, U6 extends Comparable<? super U6>, U7 extends Comparable<? super U7>, U8 extends Comparable<? super U8>> int compareTo(Tuple8<?, ?, ?, ?, ?, ?, ?, ?> o1, Tuple8<?, ?, ?, ?, ?, ?, ?, ?> o2) {
        final Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> t1 = (Tuple8<U1, U2, U3, U4, U5, U6, U7, U8>) o1;
        final Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> t2 = (Tuple8<U1, U2, U3, U4, U5, U6, U7, U8>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }

        final int check3 = t1._3.compareTo(t2._3);
        if (check3 != 0) {
            return check3;
        }

        final int check4 = t1._4.compareTo(t2._4);
        if (check4 != 0) {
            return check4;
        }

        final int check5 = t1._5.compareTo(t2._5);
        if (check5 != 0) {
            return check5;
        }

        final int check6 = t1._6.compareTo(t2._6);
        if (check6 != 0) {
            return check6;
        }

        final int check7 = t1._7.compareTo(t2._7);
        if (check7 != 0) {
            return check7;
        }

        final int check8 = t1._8.compareTo(t2._8);
        if (check8 != 0) {
            return check8;
        }

        // all components are equal
        return 0;
    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    public int compareTo(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> that) {
        return Tuple8.compareTo(this, that);
    }

    /**
     * @return the 1st element of this Tuple.
     */
    public T1 _1() {
        return _1;
    }

    /**
     * Sets the 1st element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 1st element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update1(T1 value) {
        return new Tuple8<>(value, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * @return the 2nd element of this Tuple.
     */
    public T2 _2() {
        return _2;
    }

    /**
     * Sets the 2nd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 2nd element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update2(T2 value) {
        return new Tuple8<>(_1, value, _3, _4, _5, _6, _7, _8);
    }

    /**
     * @return the 3rd element of this Tuple.
     */
    public T3 _3() {
        return _3;
    }

    /**
     * Sets the 3rd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 3rd element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update3(T3 value) {
        return new Tuple8<>(_1, _2, value, _4, _5, _6, _7, _8);
    }

    /**
     * @return the 4th element of this Tuple.
     */
    public T4 _4() {
        return _4;
    }

    /**
     * Sets the 4th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 4th element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update4(T4 value) {
        return new Tuple8<>(_1, _2, _3, value, _5, _6, _7, _8);
    }

    /**
     * @return the 5th element of this Tuple.
     */
    public T5 _5() {
        return _5;
    }

    /**
     * Sets the 5th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 5th element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update5(T5 value) {
        return new Tuple8<>(_1, _2, _3, _4, value, _6, _7, _8);
    }

    /**
     * @return the 6th element of this Tuple.
     */
    public T6 _6() {
        return _6;
    }

    /**
     * Sets the 6th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 6th element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update6(T6 value) {
        return new Tuple8<>(_1, _2, _3, _4, _5, value, _7, _8);
    }

    /**
     * @return the 7th element of this Tuple.
     */
    public T7 _7() {
        return _7;
    }

    /**
     * Sets the 7th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 7th element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update7(T7 value) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, value, _8);
    }

    /**
     * @return the 8th element of this Tuple.
     */
    public T8 _8() {
        return _8;
    }

    /**
     * Sets the 8th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 8th element of this Tuple.
     */
    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> update8(T8 value) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, value);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1   the mapper function of the 1st component
     * @param f2   the mapper function of the 2nd component
     * @param f3   the mapper function of the 3rd component
     * @param f4   the mapper function of the 4th component
     * @param f5   the mapper function of the 5th component
     * @param f6   the mapper function of the 6th component
     * @param f7   the mapper function of the 7th component
     * @param f8   the mapper function of the 8th component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @param <U5> new type of the 5th component
     * @param <U6> new type of the 6th component
     * @param <U7> new type of the 7th component
     * @param <U8> new type of the 8th component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2, U3, U4, U5, U6, U7, U8> Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2, Function<? super T3, ? extends U3> f3, Function<? super T4, ? extends U4> f4, Function<? super T5, ? extends U5> f5, Function<? super T6, ? extends U6> f6, Function<? super T7, ? extends U7> f7, Function<? super T8, ? extends U8> f8) {
        requireNonNull(f1, "f1 is null");
        requireNonNull(f2, "f2 is null");
        requireNonNull(f3, "f3 is null");
        requireNonNull(f4, "f4 is null");
        requireNonNull(f5, "f5 is null");
        requireNonNull(f6, "f6 is null");
        requireNonNull(f7, "f7 is null");
        requireNonNull(f8, "f8 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4), f5.apply(_5), f6.apply(_6), f7.apply(_7), f8.apply(_8));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U>    new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> Tuple8<U, T2, T3, T4, T5, T6, T7, T8> map1(Function<? super T1, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U>    new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> Tuple8<T1, U, T3, T4, T5, T6, T7, T8> map2(Function<? super T2, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U>    new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> Tuple8<T1, T2, U, T4, T5, T6, T7, T8> map3(Function<? super T3, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4, _5, _6, _7, _8);
    }

    /**
     * Maps the 4th component of this tuple to a new value.
     *
     * @param <U>    new type of the 4th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 4th component
     */
    public <U> Tuple8<T1, T2, T3, U, T5, T6, T7, T8> map4(Function<? super T4, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u, _5, _6, _7, _8);
    }

    /**
     * Maps the 5th component of this tuple to a new value.
     *
     * @param <U>    new type of the 5th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 5th component
     */
    public <U> Tuple8<T1, T2, T3, T4, U, T6, T7, T8> map5(Function<? super T5, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_5);
        return Tuple.of(_1, _2, _3, _4, u, _6, _7, _8);
    }

    /**
     * Maps the 6th component of this tuple to a new value.
     *
     * @param <U>    new type of the 6th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 6th component
     */
    public <U> Tuple8<T1, T2, T3, T4, T5, U, T7, T8> map6(Function<? super T6, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_6);
        return Tuple.of(_1, _2, _3, _4, _5, u, _7, _8);
    }

    /**
     * Maps the 7th component of this tuple to a new value.
     *
     * @param <U>    new type of the 7th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 7th component
     */
    public <U> Tuple8<T1, T2, T3, T4, T5, T6, U, T8> map7(Function<? super T7, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_7);
        return Tuple.of(_1, _2, _3, _4, _5, _6, u, _8);
    }

    /**
     * Maps the 8th component of this tuple to a new value.
     *
     * @param <U>    new type of the 8th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 8th component
     */
    public <U> Tuple8<T1, T2, T3, T4, T5, T6, T7, U> map8(Function<? super T8, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_8);
        return Tuple.of(_1, _2, _3, _4, _5, _6, _7, u);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple8<?, ?, ?, ?, ?, ?, ?, ?> tuple8)) return false;
        return Objects.equals(_1, tuple8._1)
                && Objects.equals(_2, tuple8._2)
                && Objects.equals(_3, tuple8._3)
                && Objects.equals(_4, tuple8._4)
                && Objects.equals(_5, tuple8._5)
                && Objects.equals(_6, tuple8._6)
                && Objects.equals(_7, tuple8._7)
                && Objects.equals(_8, tuple8._8);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
    }
}