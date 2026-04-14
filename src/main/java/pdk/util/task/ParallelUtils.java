package pdk.util.task;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Utility class for parallel processing.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 10:54 AM
 */
public class ParallelUtils {

    /**
     * Returns the number of processors available to the Java virtual machine. This value may change during a particular
     * invocation of the virtual machine. Applications that are sensitive to the number of available processors should
     * therefore occasionally poll this property and adjust their resource usage appropriately.
     *
     * @return number of available processors of the JVM.
     */
    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static ForkJoinPool createForkJoinPool(int threads) {
        return new ForkJoinPool(threads);
    }

    static void main() {
        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().toList();
        ForkJoinPool pool = new ForkJoinPool();
        try {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    numbers.parallelStream().forEach(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) {
                            System.out.println(Thread.currentThread().getName() + " processing " + integer);
                        }
                    });
                }
            }).get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }
}
