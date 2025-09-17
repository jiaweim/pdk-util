package pdk.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Array utilities
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 9:56 PM
 */
public final class ArrayUtils {

    /**
     * Return a list of int in given range
     *
     * @param startInclusive start inclusive
     * @param endInclusive   end inclusive
     * @return list of int
     */
    public static List<Integer> rangeClosed(int startInclusive, int endInclusive) {
        List<Integer> list = new ArrayList<>(endInclusive - startInclusive + 1);
        for (int i = startInclusive; i <= endInclusive; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * Take a sample from given dataset
     *
     * @param dataset       dataset
     * @param sampleIndexes indexes in the {@code dataset} of the sample
     * @return sample data
     */
    public static int[] sample(int[] dataset, int[] sampleIndexes) {
        int[] sample = new int[sampleIndexes.length];
        for (int i = 0; i < sampleIndexes.length; i++) {
            sample[i] = dataset[sampleIndexes[i]];
        }
        return sample;
    }

}
