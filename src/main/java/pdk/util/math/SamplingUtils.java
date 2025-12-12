package pdk.util.math;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Utilities from sampling.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Dec 2025, 10:09 AM
 */
public final class SamplingUtils {

    /**
     * all-purpose generator
     */
    public static UniformRandomProvider rng = RandomSource.XO_SHI_RO_256_PP.create();

}
