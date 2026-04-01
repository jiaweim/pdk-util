package pdk.util.math.demo;

import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;

/**
 * 演示 马尔科夫链模型状态转移矩阵的性质
 * <p>
 * 马尔科夫链模型的状态转移矩阵收敛到的稳定概率分布与初始状态概率分布无关。
 * <p>
 * 也就是说，如果我们得到了这个稳定概率分布对应的马尔科夫链模型的状态转移矩阵，则可以用任意的概率分布样本开始，
 * 带入马尔科夫链模型的状态转移矩阵，经过一些序列的转换，最终就可以得到符合对应稳定概率分布的样本。
 * <p>
 * 这个性质不光对我们上面的状态转移矩阵有效，对于绝大多数的其他的马尔科夫链模型的状态转移矩阵也有效。同时不光是离散状态，连续状态时也成立。
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Mar 2026, 5:24 PM
 */
public class MarkovChainDemo {
    static void main() {
        // 状态转移矩阵
        RealMatrix matrix = MatrixUtils.createRealMatrix(
                new double[][]{
                        {0.9, 0.075, 0.025},
                        {0.15, 0.8, 0.05},
                        {0.25, 0.25, 0.5}});
        // 初始状态,通常采用高斯分布生成
        RealMatrix initState = MatrixUtils.createRowRealMatrix(new double[]{0.3, 0.4, 0.3});
        int n = 100;
        for (int i = 0; i < n; i++) {
            initState = initState.multiply(matrix);
            System.out.println("Current State " + i + ": " + initState);
        }
    }
}
