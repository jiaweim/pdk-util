package pdk.util.math.distribution;

import org.junit.jupiter.api.Test;
import pdk.util.DecimalFormatUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Apr 2026, 5:41 PM
 */
class BetaBinomialTest {

    @Test
    void probability() {
        BetaBinomial bb = BetaBinomial.of(20, 2, 5);
        for (int k = 0; k <= 20; k++) {
            System.out.println(k + "\t" + DecimalFormatUtils.F6.format(bb.probability(k)));
        }
    }
}