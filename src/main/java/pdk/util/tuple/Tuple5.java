package pdk.util.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A tuple of 5 elements which can be seen as cartesian product of 5 components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @author Daniel Dietrich
 */
public final class Tuple5<T1, T2, T3, T4, T5> implements Tuple, Comparable<Tuple5<T1, T2, T3, T4, T5>>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T2 _2;

    /**
     * The 3rd element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T3 _3;

    /**
     * The 4th element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T4 _4;

    /**
     * The 5th element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T5 _5;

    /**
     * Constructs a tuple of 5 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     */
    public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
    }

    public static <T1, T2, T3, T4, T5> Comparator<Tuple5<T1, T2, T3, T4, T5>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp, Comparator<? super T3> t3Comp, Comparator<? super T4> t4Comp, Comparator<? super T5> t5Comp) {
        return (Comparator<Tuple5<T1, T2, T3, T4, T5>> & Serializable) (t1, t2) -> {
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

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>, U4 extends Comparable<? super U4>, U5 extends Comparable<? super U5>> int compareTo(Tuple5<?, ?, ?, ?, ?> o1, Tuple5<?, ?, ?, ?, ?> o2) {
        final Tuple5<U1, U2, U3, U4, U5> t1 = (Tuple5<U1, U2, U3, U4, U5>) o1;
        final Tuple5<U1, U2, U3, U4, U5> t2 = (Tuple5<U1, U2, U3, U4, U5>) o2;

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

        // all components are equal
        return 0;
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public int compareTo(Tuple5<T1, T2, T3, T4, T5> that) {
        return Tuple5.compareTo(this, that);
    }

    /**
     * Getter of the 1st element of this tuple.
     *
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
    public Tuple5<T1, T2, T3, T4, T5> update1(T1 value) {
        return new Tuple5<>(value, _2, _3, _4, _5);
    }

    /**
     * Getter of the 2nd element of this tuple.
     *
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
    public Tuple5<T1, T2, T3, T4, T5> update2(T2 value) {
        return new Tuple5<>(_1, value, _3, _4, _5);
    }

    /**
     * Getter of the 3rd element of this tuple.
     *
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
    public Tuple5<T1, T2, T3, T4, T5> update3(T3 value) {
        return new Tuple5<>(_1, _2, value, _4, _5);
    }

    /**
     * Getter of the 4th element of this tuple.
     *
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
    public Tuple5<T1, T2, T3, T4, T5> update4(T4 value) {
        return new Tuple5<>(_1, _2, _3, value, _5);
    }

    /**
     * Getter of the 5th element of this tuple.
     *
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
    public Tuple5<T1, T2, T3, T4, T5> update5(T5 value) {
        return new Tuple5<>(_1, _2, _3, _4, value);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1   the mapper function of the 1st component
     * @param f2   the mapper function of the 2nd component
     * @param f3   the mapper function of the 3rd component
     * @param f4   the mapper function of the 4th component
     * @param f5   the mapper function of the 5th component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @param <U5> new type of the 5th component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2, U3, U4, U5> Tuple5<U1, U2, U3, U4, U5> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2, Function<? super T3, ? extends U3> f3, Function<? super T4, ? extends U4> f4, Function<? super T5, ? extends U5> f5) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        Objects.requireNonNull(f4, "f4 is null");
        Objects.requireNonNull(f5, "f5 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4), f5.apply(_5));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U>    new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> Tuple5<U, T2, T3, T4, T5> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4, _5);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U>    new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> Tuple5<T1, U, T3, T4, T5> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4, _5);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U>    new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> Tuple5<T1, T2, U, T4, T5> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4, _5);
    }

    /**
     * Maps the 4th component of this tuple to a new value.
     *
     * @param <U>    new type of the 4th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 4th component
     */
    public <U> Tuple5<T1, T2, T3, U, T5> map4(Function<? super T4, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u, _5);
    }

    /**
     * Maps the 5th component of this tuple to a new value.
     *
     * @param <U>    new type of the 5th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 5th component
     */
    public <U> Tuple5<T1, T2, T3, T4, U> map5(Function<? super T5, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_5);
        return Tuple.of(_1, _2, _3, _4, u);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T6> type of the value to append
     * @param t6   the value to append
     * @return a new Tuple with the value appended
     */
    public <T6> Tuple6<T1, T2, T3, T4, T5, T6> append(T6 t6) {
        return Tuple.of(_1, _2, _3, _4, _5, t6);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T6>  the type of the 6th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple1<T6> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, _4, _5, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T6>  the type of the 6th value in the tuple
     * @param <T7>  the type of the 7th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple2<T6, T7> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, _4, _5, tuple._1, tuple._2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T6>  the type of the 6th value in the tuple
     * @param <T7>  the type of the 7th value in the tuple
     * @param <T8>  the type of the 8th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple3<T6, T7, T8> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, _4, _5, tuple._1, tuple._2, tuple._3);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple5<?, ?, ?, ?, ?> tuple5)) return false;
        return Objects.equals(_1, tuple5._1)
                && Objects.equals(_2, tuple5._2)
                && Objects.equals(_3, tuple5._3)
                && Objects.equals(_4, tuple5._4)
                && Objects.equals(_5, tuple5._5);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
    }

}