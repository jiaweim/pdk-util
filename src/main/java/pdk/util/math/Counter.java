package pdk.util.math;

import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

import java.util.*;

/**
 * A Class for counting.
 *
 * @param <T> type of the element to be counted
 * @author JiaweiMao
 * @version 1.1.0
 * @since 09 Nov 2019, 8:59 AM
 */
public class Counter<T> {

    private final HashMap<T, Integer> countMap;
    private int count;

    /**
     * Create a count map with default natural comparator
     */
    public Counter() {
        this.countMap = new HashMap<>();
    }

    public Counter(int capacity) {
        this.countMap = new HashMap<>(capacity);
    }

    /**
     * Increase one for given delta T
     *
     * @param delta a delta name
     */
    public void increase(T delta) {
        countMap.put(delta, countMap.getOrDefault(delta, 0) + 1);
        this.count++;
    }

    /**
     * Increase count for given delta.
     *
     * @param delta delta name
     * @param count count
     */
    public void increase(T delta, int count) {
        countMap.put(delta, countMap.getOrDefault(delta, 0) + count);
        this.count += count;
    }

    /**
     * @return sum of all items added to this histogram.
     */
    public int getTotalCount() {
        return count;
    }

    /**
     * Return count of given key.
     *
     * @param key key
     * @return count of the key.
     */
    public int getCount(T key) {
        return countMap.getOrDefault(key, 0);
    }

    /**
     * @return the probabilities of all deltas.
     */
    public Map<T, Double> getProbabilities() {

        HashMap<T, Double> probMap = new HashMap<>();
        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            double prob = entry.getValue() / (double) count;
            probMap.put(entry.getKey(), prob);
        }
        return probMap;
    }


    /**
     * @return probabilities of all items in descending order.
     */
    public List<Tuple2<T, Double>> getSortedProbabilities() {

        List<Tuple2<T, Double>> list = new ArrayList<>();
        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            list.add(Tuple.of(entry.getKey(), entry.getValue() / (double) count));
        }

        list.sort((o1, o2) -> Double.compare(o2._2, o1._2));

        return list;
    }

    /**
     * Return the mode value(s), e.g. the most frequent number
     *
     * @return a list containing the value(s) which appear most often
     * @since 2025-02-18 ⭐
     */
    public List<T> getMode() {
        int mostPopular = 0;
        for (Integer count : countMap.values()) {
            if (count > mostPopular)
                mostPopular = count;
        }
        List<T> modelist = new ArrayList<>();
        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == mostPopular) {
                modelist.add(entry.getKey());
            }
        }
        return modelist;
    }

    /**
     * @return keys
     */
    public Set<T> keySet() {
        return Collections.unmodifiableSet(countMap.keySet());
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        for (T key : countMap.keySet()) {
            builder.append(key).append("=").append(countMap.get(key)).append("\n");
        }

        return builder.toString();
    }
}
