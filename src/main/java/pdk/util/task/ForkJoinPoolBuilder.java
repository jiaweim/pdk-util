package pdk.util.task;

import pdk.util.IBuilder;

import java.util.concurrent.ForkJoinPool;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 11:06 AM
 */
public class ForkJoinPoolBuilder implements IBuilder<ForkJoinPool> {

    public static ForkJoinPoolBuilder create() {
        return new ForkJoinPoolBuilder();
    }

    private int numberOfThreads;


    @Override
    public ForkJoinPool build() {

        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        return pool;
    }
}
