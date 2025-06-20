package pdk.util.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A tuple of 9 elements
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 * @param <T9> type of the 9th element
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 Jun 2025, 5:37 PM
 */
public final class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Tuple, Comparable<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>, Serializable {

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;
    public final T7 _7;
    public final T8 _8;
    public final T9 _9;

    /**
     * Create a tuple of 9 elements.
     */
    public Tuple9(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8, T9 _9) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
        this._7 = _7;
        this._8 = _8;
        this._9 = _9;
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> comparator(
            Comparator<? super T1> t1Comp,
            Comparator<? super T2> t2Comp,
            Comparator<? super T3> t3Comp,
            Comparator<? super T4> t4Comp,
            Comparator<? super T5> t5Comp,
            Comparator<? super T6> t6Comp,
            Comparator<? super T7> t7Comp,
            Comparator<? super T8> t8Comp,
            Comparator<? super T9> t9Comp
    ) {
        return (Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> & Serializable) (t1, t2) -> {
            int check = t1Comp.compare(t1._1, t2._1);
            if (check != 0) {
                return check;
            }

            check = t2Comp.compare(t1._2, t2._2);
            if (check != 0) {
                return check;
            }

            check = t3Comp.compare(t1._3, t2._3);
            if (check != 0) {
                return check;
            }

            check = t4Comp.compare(t1._4, t2._4);
            if (check != 0) {
                return check;
            }

            check = t5Comp.compare(t1._5, t2._5);
            if (check != 0) {
                return check;
            }

            check = t6Comp.compare(t1._6, t2._6);
            if (check != 0) {
                return check;
            }

            check = t7Comp.compare(t1._7, t2._7);
            if (check != 0) {
                return check;
            }

            check = t8Comp.compare(t1._8, t2._8);
            if (check != 0) {
                return check;
            }

            check = t9Comp.compare(t1._9, t2._9);
            if (check != 0) {
                return check;
            }

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>, U4 extends Comparable<? super U4>, U5 extends Comparable<? super U5>, U6 extends Comparable<? super U6>, U7 extends Comparable<? super U7>, U8 extends Comparable<? super U8>, U9 extends Comparable<? super U9>> int compareTo(Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> o1, Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> o2) {
        final Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> t1 = (Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9>) o1;
        final Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> t2 = (Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9>) o2;

        int check = t1._1.compareTo(t2._1);
        if (check != 0) {
            return check;
        }

        check = t1._2.compareTo(t2._2);
        if (check != 0) {
            return check;
        }

        check = t1._3.compareTo(t2._3);
        if (check != 0) {
            return check;
        }

        check = t1._4.compareTo(t2._4);
        if (check != 0) {
            return check;
        }

        check = t1._5.compareTo(t2._5);
        if (check != 0) {
            return check;
        }

        check = t1._6.compareTo(t2._6);
        if (check != 0) {
            return check;
        }

        check = t1._7.compareTo(t2._7);
        if (check != 0) {
            return check;
        }

        check = t1._8.compareTo(t2._8);
        if (check != 0) {
            return check;
        }

        check = t1._9.compareTo(t2._9);
        if (check != 0) {
            return check;
        }

        return 0;
    }

    @Override
    public int compareTo(Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> o) {
        return Tuple9.compareTo(this, o);
    }

    @Override
    public int size() {
        return 9;
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update1(T1 value) {
        return new Tuple9<>(value, _2, _3, _4, _5, _6, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update2(T2 value) {
        return new Tuple9<>(_1, value, _3, _4, _5, _6, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update3(T3 value) {
        return new Tuple9<>(_1, _2, value, _4, _5, _6, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update4(T4 value) {
        return new Tuple9<>(_1, _2, _3, value, _5, _6, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update5(T5 value) {
        return new Tuple9<>(_1, _2, _3, _4, value, _6, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update6(T6 value) {
        return new Tuple9<>(_1, _2, _3, _4, _5, value, _7, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update7(T7 value) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, value, _8, _9);
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
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update8(T8 value) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, value, _9);
    }

    /**
     * @return the 9th element of this Tuple.
     */
    public T9 _9() {
        return _9;
    }

    /**
     * Sets the 8th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 9th element of this Tuple.
     */
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> update9(T9 value) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, _8, value);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2, U3, U4, U5, U6, U7, U8, U9> Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> map(
            Function<? super T1, ? extends U1> f1,
            Function<? super T2, ? extends U2> f2,
            Function<? super T3, ? extends U3> f3,
            Function<? super T4, ? extends U4> f4,
            Function<? super T5, ? extends U5> f5,
            Function<? super T6, ? extends U6> f6,
            Function<? super T7, ? extends U7> f7,
            Function<? super T8, ? extends U8> f8,
            Function<? super T9, ? extends U9> f9) {
        requireNonNull(f1, "f1 is null");
        requireNonNull(f2, "f2 is null");
        requireNonNull(f3, "f3 is null");
        requireNonNull(f4, "f4 is null");
        requireNonNull(f5, "f5 is null");
        requireNonNull(f6, "f6 is null");
        requireNonNull(f7, "f7 is null");
        requireNonNull(f8, "f8 is null");
        requireNonNull(f9, "f8 is null");

        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4), f5.apply(_5), f6.apply(_6), f7.apply(_7), f8.apply(_8), f9.apply(_9));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U>    new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> Tuple9<U, T2, T3, T4, T5, T6, T7, T8, T9> map1(Function<? super T1, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(mapper.apply(_1), _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U>    new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> Tuple9<T1, U, T3, T4, T5, T6, T7, T8, T9> map2(Function<? super T2, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, mapper.apply(_2), _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U>    new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> Tuple9<T1, T2, U, T4, T5, T6, T7, T8, T9> map3(Function<? super T3, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, mapper.apply(_3), _4, _5, _6, _7, _8, _9);
    }

    /**
     * Maps the 4th component of this tuple to a new value.
     *
     * @param <U>    new type of the 4th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 4th component
     */
    public <U> Tuple9<T1, T2, T3, U, T5, T6, T7, T8, T9> map4(Function<? super T4, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, mapper.apply(_4), _5, _6, _7, _8, _9);
    }

    /**
     * Maps the 5th component of this tuple to a new value.
     *
     * @param <U>    new type of the 5th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 5th component
     */
    public <U> Tuple9<T1, T2, T3, T4, U, T6, T7, T8, T9> map5(Function<? super T5, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, _4, mapper.apply(_5), _6, _7, _8, _9);
    }

    /**
     * Maps the 6th component of this tuple to a new value.
     *
     * @param <U>    new type of the 6th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 6th component
     */
    public <U> Tuple9<T1, T2, T3, T4, T5, U, T7, T8, T9> map6(Function<? super T6, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, _4, _5, mapper.apply(_6), _7, _8, _9);
    }

    /**
     * Maps the 7th component of this tuple to a new value.
     *
     * @param <U>    new type of the 7th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 7th component
     */
    public <U> Tuple9<T1, T2, T3, T4, T5, T6, U, T8, T9> map7(Function<? super T7, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, _4, _5, _6, mapper.apply(_7), _8, _9);
    }

    /**
     * Maps the 8th component of this tuple to a new value.
     *
     * @param <U>    new type of the 8th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 8th component
     */
    public <U> Tuple9<T1, T2, T3, T4, T5, T6, T7, U, T9> map8(Function<? super T8, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, _4, _5, _6, _7, mapper.apply(_8), _9);
    }

    /**
     * Maps the 9th component of this tuple to a new value.
     *
     * @param <U>    new type of the 8th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 8th component
     */
    public <U> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, U> map9(Function<? super T9, ? extends U> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(_1, _2, _3, _4, _5, _6, _7, _8, mapper.apply(_9));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> tuple9)) return false;
        return Objects.equals(_1, tuple9._1)
                && Objects.equals(_2, tuple9._2)
                && Objects.equals(_3, tuple9._3)
                && Objects.equals(_4, tuple9._4)
                && Objects.equals(_5, tuple9._5)
                && Objects.equals(_6, tuple9._6)
                && Objects.equals(_7, tuple9._7)
                && Objects.equals(_8, tuple9._8)
                && Objects.equals(_9, tuple9._9);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ')';
    }
}
