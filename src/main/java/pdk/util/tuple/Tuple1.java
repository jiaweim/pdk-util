package pdk.util.tuple;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A tuple of one element which can be seen as cartesian product of one component.
 *
 * @param <T1> type of the 1st element
 * @author Daniel Dietrich
 */
public final class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T1 _1;

    /**
     * Constructs a tuple of one element.
     *
     * @param t1 the 1st element
     */
    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    public static <T1> Comparator<Tuple1<T1>> comparator(Comparator<? super T1> t1Comp) {
        return (Comparator<Tuple1<T1>> & Serializable) (t1, t2) -> t1Comp.compare(t1._1, t2._1);
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>> int compareTo(Tuple1<?> o1, Tuple1<?> o2) {
        final Tuple1<U1> t1 = (Tuple1<U1>) o1;
        final Tuple1<U1> t2 = (Tuple1<U1>) o2;

        return t1._1.compareTo(t2._1);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int compareTo(Tuple1<T1> that) {
        return Tuple1.compareTo(this, that);
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
    public Tuple1<T1> update1(T1 value) {
        return new Tuple1<>(value);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1>   new type of the 1st component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U1> Tuple1<U1> map(Function<? super T1, ? extends U1> mapper) {
        requireNonNull(mapper, "mapper is null");
        return Tuple.of(mapper.apply(_1));
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f   Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(Function<? super T1, ? extends U> f) {
        requireNonNull(f, "f is null");
        return f.apply(_1);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T2> type of the value to append
     * @param t2   the value to append
     * @return a new Tuple with the value appended
     */
    public <T2> Tuple2<T1, T2> append(T2 t2) {
        return Tuple.of(_1, t2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2> Tuple2<T1, T2> concat(Tuple1<T2> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3> Tuple3<T1, T2, T3> concat(Tuple2<T2, T3> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param <T4>  the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple3<T2, T3, T4> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2, tuple._3);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param <T4>  the type of the 4th value in the tuple
     * @param <T5>  the type of the 5th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple4<T2, T3, T4, T5> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2, tuple._3, tuple._4);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param <T4>  the type of the 4th value in the tuple
     * @param <T5>  the type of the 5th value in the tuple
     * @param <T6>  the type of the 6th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple5<T2, T3, T4, T5, T6> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2, tuple._3, tuple._4, tuple._5);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param <T4>  the type of the 4th value in the tuple
     * @param <T5>  the type of the 5th value in the tuple
     * @param <T6>  the type of the 6th value in the tuple
     * @param <T7>  the type of the 7th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple6<T2, T3, T4, T5, T6, T7> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param <T3>  the type of the 3rd value in the tuple
     * @param <T4>  the type of the 4th value in the tuple
     * @param <T5>  the type of the 5th value in the tuple
     * @param <T6>  the type of the 6th value in the tuple
     * @param <T7>  the type of the 7th value in the tuple
     * @param <T8>  the type of the 8th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple7<T2, T3, T4, T5, T6, T7, T8> tuple) {
        requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6, tuple._7);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple1<?> tuple1)) return false;
        return Objects.equals(_1, tuple1._1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_1);
    }

    @Override
    public String toString() {
        return "(" + _1 + ")";
    }

}