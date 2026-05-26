package pdk.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 08 May 2026, 2:41 PM
 */
class SortUtilsTest {

    private static final Comparator<Integer> INT_COMPARATOR = Comparator.naturalOrder();
    private static final Comparator<String> STR_COMPARATOR = Comparator.naturalOrder();


    @Nested
    class LowerBoundInt {
        @Test
        void lowerBound_existingValueUnique() {
            int[] array = {1, 3, 5, 7, 9};
            assertEquals(2, SortUtils.getLowerBound(array, 5));  // 5 在索引2
            assertEquals(0, SortUtils.getLowerBound(array, 1));  // 最小值
            assertEquals(4, SortUtils.getLowerBound(array, 9));  // 最大值
        }

        @Test
        @DisplayName("存在重复元素，应返回第一次出现的索引")
        void lowerBound_existingValueDuplicate() {
            int[] array = {2, 4, 4, 4, 6, 8};
            assertEquals(1, SortUtils.getLowerBound(array, 4));  // 第一个4在索引1
            assertNotEquals(3, SortUtils.getLowerBound(array, 4));  // 不能是3，这里只验证第一次
            // 更严谨：验证返回值确实是第一个4
            assertEquals(1, SortUtils.getLowerBound(array, 4));
        }

        @Test
        @DisplayName("值不存在于数组中，应返回第一个大于目标值的索引")
        void lowerBound_valueNotExist_insertPosition() {
            int[] array = {10, 20, 30, 40, 50};
            assertEquals(2, SortUtils.getLowerBound(array, 25)); // 25 应插入索引2 (30)
            assertEquals(0, SortUtils.getLowerBound(array, 5));  // 5 < 所有元素 → 0
            assertEquals(5, SortUtils.getLowerBound(array, 55)); // 55 > 所有元素 → length
        }

        // ---------- 边界情况 ----------
        @Test
        @DisplayName("空数组应返回 -1")
        void lowerBound_emptyArray() {
            int[] empty = {};
            assertEquals(-1, SortUtils.getLowerBound(empty, 100));
        }


        @Test
        @DisplayName("数组只有一个元素，且目标小于该元素")
        void lowerBound_singleElement_targetSmaller() {
            int[] array = {42};
            assertEquals(0, SortUtils.getLowerBound(array, 10));
        }

        @Test
        @DisplayName("数组只有一个元素，且目标等于该元素")
        void lowerBound_singleElement_targetEqual() {
            int[] array = {42};
            assertEquals(0, SortUtils.getLowerBound(array, 42));
        }

        @Test
        @DisplayName("数组只有一个元素，且目标大于该元素")
        void lowerBound_singleElement_targetLarger() {
            int[] array = {42};
            assertEquals(1, SortUtils.getLowerBound(array, 100));
        }

        @Test
        @DisplayName("所有元素都小于目标，应返回数组长度")
        void lowerBound_allSmaller() {
            int[] array = {5, 10, 15};
            assertEquals(3, SortUtils.getLowerBound(array, 20));
        }

        @Test
        @DisplayName("所有元素都大于目标，应返回 0")
        void lowerBound_allGreater() {
            int[] array = {100, 200, 300};
            assertEquals(0, SortUtils.getLowerBound(array, 50));
        }

        @Test
        @DisplayName("包含负数且目标在负数范围内")
        void lowerBound_negativeNumbers() {
            int[] array = {-50, -30, -10, 0, 10};
            assertEquals(2, SortUtils.getLowerBound(array, -15)); // -15 应插在 -10 前（索引2）
            assertEquals(1, SortUtils.getLowerBound(array, -30)); // -30存在
            assertEquals(0, SortUtils.getLowerBound(array, -100));
            assertEquals(5, SortUtils.getLowerBound(array, 20));
        }

        // ---------- 参数校验 ----------
        @Test
        @DisplayName("传入 null 数组应抛出 NullPointerException")
        void lowerBound_nullArray_throwsNPE() {
            assertThrows(NullPointerException.class, () -> SortUtils.getLowerBound((int[]) null, 5));
        }

        // ---------- 随机性/大规模数据（可选） ----------
        @Test
        @DisplayName("大规模随机有序数组，验证一致性")
        void lowerBound_largeRandomArray() {
            int size = 10_000;
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = i * 2;  // 偶数序列
            }
            // 测试存在值
            for (int i = 0; i < size; i++) {
                assertEquals(i, SortUtils.getLowerBound(array, i * 2));
            }
            // 测试不存在值（奇数）
            for (int i = 0; i < size - 1; i++) {
                int target = i * 2 + 1; // 奇数
                int expected = i + 1;   // 应插入到下一个偶数位置
                assertEquals(expected, SortUtils.getLowerBound(array, target));
            }
            // 边界
            assertEquals(0, SortUtils.getLowerBound(array, -10));
            assertEquals(size, SortUtils.getLowerBound(array, size * 2 + 1));
        }
    }

    @Nested
    class LowerBoundIntRange {
        @Test
        @DisplayName("range 等于数组长度，等同于完整数组搜索")
        void rangeEqualsArrayLength() {
            int[] array = {10, 20, 30, 40, 50};
            // 完整范围搜索
            assertEquals(2, SortUtils.getLowerBound(array, 30, 5));
            assertEquals(5, SortUtils.getLowerBound(array, 60, 5));
            assertEquals(0, SortUtils.getLowerBound(array, 5, 5));
        }

        @Test
        @DisplayName("range 小于数组长度，仅在前 range 个元素内搜索")
        void rangeSmallerThanLength() {
            int[] array = {10, 20, 30, 40, 50, 60, 70};
            // 只在前 3 个元素 [10,20,30] 中搜索
            assertEquals(2, SortUtils.getLowerBound(array, 30, 3));  // 30 存在
            assertEquals(3, SortUtils.getLowerBound(array, 35, 3));  // 35 大于所有，返回 range=3
            assertEquals(0, SortUtils.getLowerBound(array, 5, 3));   // 5 小于所有，返回 0

            // 目标值在后半部分，但 range 限制在前 3 个，应只在前 3 个中比较
            assertEquals(3, SortUtils.getLowerBound(array, 40, 3));  // 40 大于前 3 个所有，返回 3
        }

        @Test
        @DisplayName("range 等于 1，只有一个元素的搜索范围")
        void rangeEqualsOne() {
            int[] array = {100, 200, 300};
            // 范围只包含第一个元素 100
            assertEquals(0, SortUtils.getLowerBound(array, 100, 1));
            assertEquals(1, SortUtils.getLowerBound(array, 150, 1)); // 150 > 100，返回 1
            assertEquals(0, SortUtils.getLowerBound(array, 50, 1));  // 50 < 100，返回 0
        }

        // ---------- range = 0 的特殊情况 ----------
        @Test
        @DisplayName("range = 0，应返回 -1（空范围）")
        void rangeZero() {
            int[] array = {1, 2, 3};
            assertEquals(-1, SortUtils.getLowerBound(array, 10, 0));
        }

        // ---------- 数组中包含重复元素 ----------
        @Test
        @DisplayName("范围内有重复元素，返回第一次出现的位置")
        void duplicateWithinRange() {
            int[] array = {5, 5, 5, 10, 15};
            // 范围包含前 3 个重复元素
            assertEquals(0, SortUtils.getLowerBound(array, 5, 3));
            // 范围仅包含前 2 个重复元素
            assertEquals(0, SortUtils.getLowerBound(array, 5, 2));
            // 范围包含前 4 个元素 (5,5,5,10)
            assertEquals(0, SortUtils.getLowerBound(array, 5, 4));
            assertEquals(3, SortUtils.getLowerBound(array, 10, 4)); // 10 在索引3
        }

        // ---------- 边界值测试 ----------
        @Test
        @DisplayName("range 为 1 且目标等于该元素")
        void rangeOne_equal() {
            int[] array = {42};
            assertEquals(0, SortUtils.getLowerBound(array, 42, 1));
        }

        @Test
        @DisplayName("range 为 1 且目标小于该元素")
        void rangeOne_targetSmaller() {
            int[] array = {42};
            assertEquals(0, SortUtils.getLowerBound(array, 10, 1));
        }

        @Test
        @DisplayName("range 为 1 且目标大于该元素")
        void rangeOne_targetLarger() {
            int[] array = {42};
            assertEquals(1, SortUtils.getLowerBound(array, 100, 1));
        }

        @Test
        @DisplayName("范围内所有元素都小于目标，返回 range")
        void allWithinRangeSmallerThanTarget() {
            int[] array = {10, 20, 30, 40};
            assertEquals(3, SortUtils.getLowerBound(array, 35, 3)); // 前3个:10,20,30 < 35
            assertEquals(2, SortUtils.getLowerBound(array, 25, 2)); // 前2个:10,20 < 25
        }

        @Test
        @DisplayName("范围内所有元素都大于目标，返回 0")
        void allWithinRangeGreaterThanTarget() {
            int[] array = {100, 200, 300, 400};
            assertEquals(0, SortUtils.getLowerBound(array, 50, 3)); // 前3个都 > 50
            assertEquals(1, SortUtils.getLowerBound(array, 150, 2)); // 前2个(100,200) 都 >150? 200>150 但100<150，不满足全部大于。我们换一个场景：
            // 修正：确保范围内所有元素都大于目标
            assertEquals(0, SortUtils.getLowerBound(array, 90, 1)); // 只考虑100>90
            assertEquals(2, SortUtils.getLowerBound(array, 250, 3)); // 前3个(100,200,300) 中 100和200小于250，所以不是都大于。正确场景：
            // 选择更小的目标，比如目标=50，所有元素>50
            assertEquals(0, SortUtils.getLowerBound(array, 50, 4));
            assertEquals(0, SortUtils.getLowerBound(array, 50, 3));
        }

        // ---------- 负数测试 ----------
        @Test
        @DisplayName("包含负数的数组，range 限制范围")
        void negativeNumbers() {
            int[] array = {-50, -30, -10, 0, 10, 20};
            // 只搜索前 4 个：-50, -30, -10, 0
            assertEquals(1, SortUtils.getLowerBound(array, -30, 4)); // -30 存在
            assertEquals(2, SortUtils.getLowerBound(array, -20, 4)); // -20 应插入到 -10 前（索引2）
            assertEquals(4, SortUtils.getLowerBound(array, 5, 4));   // 5 > 0, 返回 range=4
            assertEquals(0, SortUtils.getLowerBound(array, -60, 4));
        }

        // ---------- 参数校验 ----------
        @Test
        @DisplayName("传入 null 数组应抛出 NullPointerException")
        void nullArrayThrowsNPE() {
            assertThrows(NullPointerException.class, () -> SortUtils.getLowerBound(null, 5, 3));
        }

        // 注意：未测试 range > array.length 的情况，因为方法不保证处理这种越界。
        // 如果实际调用时 range 超过数组长度，会导致 ArrayIndexOutOfBoundsException。
        // 这属于调用者错误，不在单元测试范围内。

        // ---------- 随机/大规模验证（可选） ----------
        @Test
        @DisplayName("大规模随机有序数组，验证范围搜索正确性")
        void largeArrayRandomTest() {
            int size = 5000;
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = i * 2; // 偶数序列
            }

            // 测试不同的 range 值
            for (int range : new int[]{1, size / 3, size / 2, size - 1, size}) {
                for (int target = -10; target <= size * 2 + 10; target++) {
                    int expected;
                    if (range == 0) {
                        expected = -1;
                    } else {
                        // 手动计算 lower bound 在 [0, range)
                        int idx = 0;
                        while (idx < range && array[idx] < target) {
                            idx++;
                        }
                        expected = idx;
                    }
                    assertEquals(expected, SortUtils.getLowerBound(array, target, range),
                            String.format("Failed for range=%d, target=%d", range, target));
                }
            }
        }
    }

    @Test
    void getMinInsertIndex() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.naturalOrder();

        // left end
        assertEquals(0, SortUtils.getLowerBound(data, comparator, 0.5));

        // equal left
        assertEquals(0, SortUtils.getLowerBound(data, comparator, 1.0));

        // between
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.1));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.2));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.45));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.5));

        assertEquals(2, SortUtils.getLowerBound(data, comparator, 1.6));
        assertEquals(2, SortUtils.getLowerBound(data, comparator, 2.0));

        assertEquals(6, SortUtils.getLowerBound(data, comparator, 3.6));
        assertEquals(6, SortUtils.getLowerBound(data, comparator, 4.0));
        assertEquals(7, SortUtils.getLowerBound(data, comparator, 4.1));

        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        assertEquals(1, SortUtils.getLowerBound(data2, comparator, 1.5));
        assertEquals(7, SortUtils.getLowerBound(data2, comparator, 3.0));

        assertEquals(0, SortUtils.getLowerBound(data2, comparator, 0.0));
        assertEquals(0, SortUtils.getLowerBound(data2, comparator, 1.0));
        assertEquals(1, SortUtils.getLowerBound(data2, comparator, 1.2));
        assertEquals(5, SortUtils.getLowerBound(data2, comparator, 1.6));
        assertEquals(5, SortUtils.getLowerBound(data2, comparator, 2.0));
        assertEquals(10, SortUtils.getLowerBound(data2, comparator, 4.0));
    }

    @Test
    void getLowerBound_List() {
        List<Double> data = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0);
        Comparator<Double> comparator = Comparator.naturalOrder();

        // left end
        assertEquals(0, SortUtils.getLowerBound(data, comparator, 0.5));

        // equal left
        assertEquals(0, SortUtils.getLowerBound(data, comparator, 1.0));

        // between
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.1));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.2));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.45));
        assertEquals(1, SortUtils.getLowerBound(data, comparator, 1.5));

        assertEquals(2, SortUtils.getLowerBound(data, comparator, 1.6));
        assertEquals(2, SortUtils.getLowerBound(data, comparator, 2.0));

        assertEquals(6, SortUtils.getLowerBound(data, comparator, 3.6));
        assertEquals(6, SortUtils.getLowerBound(data, comparator, 4.0));
        assertEquals(7, SortUtils.getLowerBound(data, comparator, 4.1));

        List<Double> data2 = Arrays.asList(1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0);

        assertEquals(1, SortUtils.getLowerBound(data2, comparator, 1.5));
        assertEquals(7, SortUtils.getLowerBound(data2, comparator, 3.0));

        assertEquals(0, SortUtils.getLowerBound(data2, comparator, 0.0));
        assertEquals(0, SortUtils.getLowerBound(data2, comparator, 1.0));
        assertEquals(1, SortUtils.getLowerBound(data2, comparator, 1.2));
        assertEquals(5, SortUtils.getLowerBound(data2, comparator, 1.6));
        assertEquals(5, SortUtils.getLowerBound(data2, comparator, 2.0));
        assertEquals(10, SortUtils.getLowerBound(data2, comparator, 4.0));
    }

    @Test
    void getLowerBoundEmptyArray() {
        Integer[] array = {};
        assertEquals(-1, SortUtils.getLowerBound(array, INT_COMPARATOR, 5));
    }

    @Test
    void getLowerBoundSingle_Less() {
        Integer[] array = {10};
        assertEquals(0, SortUtils.getLowerBound(array, INT_COMPARATOR, 5));
    }

    @Test
    void getLowerBoundSingle_Equal() {
        Integer[] array = {10};
        assertEquals(0, SortUtils.getLowerBound(array, INT_COMPARATOR, 10));
    }

    @Test
    void getLowerBoundSingle_Greater() {
        Integer[] array = {10};
        assertEquals(1, SortUtils.getLowerBound(array, INT_COMPARATOR, 20));
    }

    @Test
    void getLowerBound_NoDuplicates_TargetAbsent() {
        Integer[] arr = {1, 3, 7, 9};
        assertEquals(2, SortUtils.getLowerBound(arr, INT_COMPARATOR, 5));
    }

    @Test
    void getLowerBound_NoDuplicates_TargetSmallerThanAll() {
        Integer[] arr = {2, 4, 6};
        assertEquals(0, SortUtils.getLowerBound(arr, INT_COMPARATOR, 1));
    }

    @Test
    void getLowerBound_NoDuplicates_TargetLargerThanAll() {
        Integer[] arr = {2, 4, 6};
        assertEquals(3, SortUtils.getLowerBound(arr, INT_COMPARATOR, 10));
    }

    @Test
    void testDuplicates_TargetPresent() {
        Integer[] arr = {1, 2, 2, 2, 3, 4};
        assertEquals(1, SortUtils.getLowerBound(arr, INT_COMPARATOR, 2)); // 第一个2的位置
    }

    @Test
    void testDuplicates_TargetAbsent() {
        Integer[] arr = {1, 2, 2, 4, 4, 5};
        assertEquals(3, SortUtils.getLowerBound(arr, INT_COMPARATOR, 3)); // 第一个>=3的是索引3的4
    }

    @Test
    void testAllSameElements_TargetEqual() {
        Integer[] arr = {7, 7, 7, 7};
        assertEquals(0, SortUtils.getLowerBound(arr, INT_COMPARATOR, 7));
    }

    @Test
    void testAllSameElements_TargetSmallerThan() {
        Integer[] arr = {7, 7, 7};
        assertEquals(0, SortUtils.getLowerBound(arr, INT_COMPARATOR, 5));
    }

    @Test
    void testAllSameElements_TargetLargerThan() {
        Integer[] arr = {7, 7, 7};
        assertEquals(3, SortUtils.getLowerBound(arr, INT_COMPARATOR, 9));
    }

    // ---------- 逆序比较器（降序）----------
    @Test
    void testReverseComparator() {
        Integer[] arr = {9, 7, 5, 3, 1};
        Comparator<Integer> reversed = Comparator.reverseOrder();
        // 对于降序数组，lowerBound 返回第一个 <= 值的位置（因为大小于符号反转）
        assertEquals(2, SortUtils.getLowerBound(arr, reversed, 5)); // 第一个 <=5 的元素是索引2的5
        assertEquals(0, SortUtils.getLowerBound(arr, reversed, 10)); // 第一个 <=10 的元素是索引0的9
        assertEquals(5, SortUtils.getLowerBound(arr, reversed, 0)); // 全部大于0，插入点=长度
    }

    // ---------- 自定义类型 ----------
    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        int getAge() {return age;}

        @Override
        public String toString() {return name + "(" + age + ")";}
    }

    @Test
    void testCustomTypeComparator() {
        Person[] people = {
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Charlie", 30),
                new Person("Diana", 35)
        };
        Comparator<Person> byAge = Comparator.comparingInt(Person::getAge);
        Person target = new Person("Target", 30);
        int idx = SortUtils.getLowerBound(people, byAge, target);
        assertEquals(1, idx); // 第一个年龄>=30的是Bob（索引1）

        // 测试不存在的情况
        idx = SortUtils.getLowerBound(people, byAge, new Person("Eve", 33));
        assertEquals(3, idx); // 插入到Diana之前
    }

    // ---------- 字符串 ----------
    @Test
    void testStringArray() {
        String[] arr = {"apple", "banana", "cherry", "cherry", "date"};
        assertEquals(2, SortUtils.getLowerBound(arr, STR_COMPARATOR, "cherry"));
        assertEquals(1, SortUtils.getLowerBound(arr, STR_COMPARATOR, "apricot"));
        assertEquals(5, SortUtils.getLowerBound(arr, STR_COMPARATOR, "fig"));
        assertEquals(0, SortUtils.getLowerBound(arr, STR_COMPARATOR, "aardvark"));
    }

    // ---------- 大数组压力测试 (可选) ----------
    @Test
    void testLargeArray() {
        int size = 1_000_000;
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i * 2; // 偶数序列
        }
        assertEquals(250_000, SortUtils.getLowerBound(arr, INT_COMPARATOR, 500_000));
        assertEquals(0, SortUtils.getLowerBound(arr, INT_COMPARATOR, -1));
        assertEquals(size, SortUtils.getLowerBound(arr, INT_COMPARATOR, 2_000_001));
    }


    @Test
    void getLowerBoundDouble() {
        double[] data = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};

        // left end
        assertEquals(0, SortUtils.getLowerBound(data, 0.5));

        // equal left
        assertEquals(0, SortUtils.getLowerBound(data, 1.0));

        // between
        assertEquals(1, SortUtils.getLowerBound(data, 1.1));
        assertEquals(1, SortUtils.getLowerBound(data, 1.2));
        assertEquals(1, SortUtils.getLowerBound(data, 1.45));
        assertEquals(1, SortUtils.getLowerBound(data, 1.5));

        assertEquals(2, SortUtils.getLowerBound(data, 1.6));
        assertEquals(2, SortUtils.getLowerBound(data, 2.0));

        assertEquals(6, SortUtils.getLowerBound(data, 3.6));
        assertEquals(6, SortUtils.getLowerBound(data, 4.0));
        assertEquals(7, SortUtils.getLowerBound(data, 4.1));

        double[] data2 = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        assertEquals(1, SortUtils.getLowerBound(data2, 1.5));
        assertEquals(7, SortUtils.getLowerBound(data2, 3.0));

        assertEquals(0, SortUtils.getLowerBound(data2, 0.0));
        assertEquals(0, SortUtils.getLowerBound(data2, 1.0));
        assertEquals(1, SortUtils.getLowerBound(data2, 1.2));
        assertEquals(5, SortUtils.getLowerBound(data2, 1.6));
        assertEquals(5, SortUtils.getLowerBound(data2, 2.0));
        assertEquals(10, SortUtils.getLowerBound(data2, 4.0));
    }


    @Test
    void getUpperBound_EmptyArray() {
        Integer[] arr = {};
        assertEquals(-1, SortUtils.getUpperBound(arr, INT_COMPARATOR, 5));
    }

    @Test
    void getUpperBound_SingleElement_LessThan() {
        Integer[] arr = {10};
        assertEquals(0, SortUtils.getUpperBound(arr, INT_COMPARATOR, 5));   // 5 < 10, first >5 is index 0
    }

    @Test
    void getUpperBound_SingleElement_Equal() {
        Integer[] arr = {10};
        assertEquals(1, SortUtils.getUpperBound(arr, INT_COMPARATOR, 10));  // no element >10, return length
    }

    @Test
    void getUpperBound_SingleElement_GreaterThan() {
        Integer[] arr = {10};
        assertEquals(1, SortUtils.getUpperBound(arr, INT_COMPARATOR, 20));  // all <=20, return length
    }

    @Test
    void getUpperBound_NoDuplicates_TargetPresent() {
        Integer[] arr = {1, 3, 5, 7, 9};
        assertEquals(3, SortUtils.getUpperBound(arr, INT_COMPARATOR, 5));   // first >5 is 7 at index 3
    }

    @Test
    void getUpperBound_NoDuplicates_TargetAbsent() {
        Integer[] arr = {1, 3, 7, 9};
        assertEquals(2, SortUtils.getUpperBound(arr, INT_COMPARATOR, 5));   // first >5 is 7 at index 2
    }

    @Test
    void getUpperBound_NoDuplicates_TargetSmallerThanAll() {
        Integer[] arr = {2, 4, 6};
        assertEquals(0, SortUtils.getUpperBound(arr, INT_COMPARATOR, 1));   // all >1, first at index 0
    }

    @Test
    void getUpperBound_NoDuplicates_TargetLargerThanAll() {
        Integer[] arr = {2, 4, 6};
        assertEquals(3, SortUtils.getUpperBound(arr, INT_COMPARATOR, 10));  // none >10, return length
    }

    @Test
    void getUpperBound_Duplicates_TargetPresent() {
        Integer[] arr = {1, 2, 2, 2, 3, 4};
        assertEquals(4, SortUtils.getUpperBound(arr, INT_COMPARATOR, 2));   // first >2 is 3 at index 4
    }

    @Test
    void getUpperBound_Duplicates_TargetAbsent() {
        Integer[] arr = {1, 2, 2, 4, 4, 5};
        assertEquals(3, SortUtils.getUpperBound(arr, INT_COMPARATOR, 3));   // first >3 is 4 at index 3
    }

    @Test
    void getUpperBound_AllSameElements_TargetEqual() {
        Integer[] arr = {7, 7, 7, 7};
        assertEquals(4, SortUtils.getUpperBound(arr, INT_COMPARATOR, 7));   // all <=7, return length
    }

    @Test
    void getUpperBound_AllSameElements_TargetLarger() {
        Integer[] arr = {7, 7, 7};
        assertEquals(3, SortUtils.getUpperBound(arr, INT_COMPARATOR, 9));   // none >9, return length
    }

    @Test
    void getUpperBound_ReverseComparator() {
        Integer[] arr = {9, 7, 5, 3, 1};
        Comparator<Integer> reversed = Comparator.reverseOrder();
        // With reversed order: upper_bound returns index of first element < value
        assertEquals(2, SortUtils.getUpperBound(arr, reversed, 7));   // first <7 is 5 at index 2
        assertEquals(0, SortUtils.getUpperBound(arr, reversed, 10));  // first <10 is 9 at index 0
        assertEquals(5, SortUtils.getUpperBound(arr, reversed, 0));   // no element <0, return length
    }

    @Test
    void getUpperBound_StringArray() {
        String[] arr = {"apple", "banana", "cherry", "cherry", "date"};
        assertEquals(4, SortUtils.getUpperBound(arr, STR_COMPARATOR, "cherry")); // first >cherry is date at index 4
        assertEquals(1, SortUtils.getUpperBound(arr, STR_COMPARATOR, "apricot")); // first >apricot is banana at index 1
        assertEquals(0, SortUtils.getUpperBound(arr, STR_COMPARATOR, "aardvark")); // all >aardvark
        assertEquals(5, SortUtils.getUpperBound(arr, STR_COMPARATOR, "fig"));      // none >fig
    }

    @Test
    void getUpperBound_LargeArray() {
        int size = 1_000_000;
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i * 2; // even numbers
        }
        assertEquals(250_001, SortUtils.getUpperBound(arr, INT_COMPARATOR, 500_000)); // first >500000 is 500002 at index 250001
        assertEquals(0, SortUtils.getUpperBound(arr, INT_COMPARATOR, -1));
        assertEquals(size, SortUtils.getUpperBound(arr, INT_COMPARATOR, 2_000_000));
    }

    @Test
    void getUpperBound() {
        double[] data = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};

        assertEquals(0, SortUtils.getUpperBound(data, 0.5));
        assertEquals(1, SortUtils.getUpperBound(data, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data, 1.2));
        assertEquals(2, SortUtils.getUpperBound(data, 1.5));
        assertEquals(3, SortUtils.getUpperBound(data, 2.0));
        assertEquals(3, SortUtils.getUpperBound(data, 2.2));
        assertEquals(4, SortUtils.getUpperBound(data, 2.5));
        assertEquals(7, SortUtils.getUpperBound(data, 4.0));
        assertEquals(7, SortUtils.getUpperBound(data, 4.1));

        double[] data2 = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        assertEquals(0, SortUtils.getUpperBound(data2, 0.0));
        assertEquals(1, SortUtils.getUpperBound(data2, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data2, 1.2));
        assertEquals(5, SortUtils.getUpperBound(data2, 1.5));
        assertEquals(7, SortUtils.getUpperBound(data2, 2.9));
        assertEquals(9, SortUtils.getUpperBound(data2, 3.0));
        assertEquals(9, SortUtils.getUpperBound(data2, 3.1));
        assertEquals(11, SortUtils.getUpperBound(data2, 4.0));
        assertEquals(11, SortUtils.getUpperBound(data2, 4.5));
    }

    @Test
    void getUpperBoundObject() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        assertEquals(0, SortUtils.getUpperBound(data, comparator, 0.5));
        assertEquals(1, SortUtils.getUpperBound(data, comparator, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data, comparator, 1.2));
        assertEquals(2, SortUtils.getUpperBound(data, comparator, 1.5));
        assertEquals(3, SortUtils.getUpperBound(data, comparator, 2.0));
        assertEquals(3, SortUtils.getUpperBound(data, comparator, 2.2));
        assertEquals(4, SortUtils.getUpperBound(data, comparator, 2.5));
        assertEquals(7, SortUtils.getUpperBound(data, comparator, 4.0));
        assertEquals(7, SortUtils.getUpperBound(data, comparator, 4.1));

        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        assertEquals(0, SortUtils.getUpperBound(data2, comparator, 0.0));
        assertEquals(1, SortUtils.getUpperBound(data2, comparator, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data2, comparator, 1.2));
        assertEquals(5, SortUtils.getUpperBound(data2, comparator, 1.5));
        assertEquals(7, SortUtils.getUpperBound(data2, comparator, 2.9));
        assertEquals(9, SortUtils.getUpperBound(data2, comparator, 3.0));
        assertEquals(9, SortUtils.getUpperBound(data2, comparator, 3.1));
        assertEquals(11, SortUtils.getUpperBound(data2, comparator, 4.0));
        assertEquals(11, SortUtils.getUpperBound(data2, comparator, 4.5));
    }

    @Test
    void getUpperBound_List() {
        List<Double> data = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0);
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        assertEquals(0, SortUtils.getUpperBound(data, comparator, 0.5));
        assertEquals(1, SortUtils.getUpperBound(data, comparator, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data, comparator, 1.2));
        assertEquals(2, SortUtils.getUpperBound(data, comparator, 1.5));
        assertEquals(3, SortUtils.getUpperBound(data, comparator, 2.0));
        assertEquals(3, SortUtils.getUpperBound(data, comparator, 2.2));
        assertEquals(4, SortUtils.getUpperBound(data, comparator, 2.5));
        assertEquals(7, SortUtils.getUpperBound(data, comparator, 4.0));
        assertEquals(7, SortUtils.getUpperBound(data, comparator, 4.1));

        List<Double> data2 = Arrays.asList(1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0);
        assertEquals(0, SortUtils.getUpperBound(data2, comparator, 0.0));
        assertEquals(1, SortUtils.getUpperBound(data2, comparator, 1.0));
        assertEquals(1, SortUtils.getUpperBound(data2, comparator, 1.2));
        assertEquals(5, SortUtils.getUpperBound(data2, comparator, 1.5));
        assertEquals(7, SortUtils.getUpperBound(data2, comparator, 2.9));
        assertEquals(9, SortUtils.getUpperBound(data2, comparator, 3.0));
        assertEquals(9, SortUtils.getUpperBound(data2, comparator, 3.1));
        assertEquals(11, SortUtils.getUpperBound(data2, comparator, 4.0));
        assertEquals(11, SortUtils.getUpperBound(data2, comparator, 4.5));
    }

    @Test
    void getBound_EmptyArray() {
        Integer[] arr = {};
        int[] bounds = SortUtils.getBound(arr, INT_COMPARATOR, 2, 5);
        assertArrayEquals(new int[]{-1, -1}, bounds);
    }

    @Test
    void getBound_SingleElement_InsideRange() {
        Integer[] arr = {10};
        // 5 <= 10 <= 15 => low=lower_bound(5)=0, high=upper_bound(15)=1
        assertArrayEquals(new int[]{0, 1}, SortUtils.getBound(arr, INT_COMPARATOR, 5, 15));
    }

    @Test
    void getBound_SingleElement_OutsideRange() {
        Integer[] arr = {10};
        // 1, 3 both less than 10 => low=lower_bound(1)=0, high=upper_bound(3)=0
        assertArrayEquals(new int[]{0, 0}, SortUtils.getBound(arr, INT_COMPARATOR, 1, 3));
        // 12, 15 both greater => low=1, high=1
        assertArrayEquals(new int[]{1, 1}, SortUtils.getBound(arr, INT_COMPARATOR, 12, 15));
    }

    // ---------- 正常数组 ----------
    @Test
    void getBound_NoDuplicates_NormalRange() {
        Integer[] arr = {1, 3, 5, 7, 9};
        // lower_bound(3)=1, upper_bound(7)=4 -> [1,4) 包含 3,5,7
        assertArrayEquals(new int[]{1, 4}, SortUtils.getBound(arr, INT_COMPARATOR, 3, 7));
    }

    @Test
    void getBound_Duplicates_RangeWithEqualElements() {
        Integer[] arr = {1, 2, 2, 2, 3, 4};
        // lower_bound(2)=1, upper_bound(2)=4 -> 区间 [1,4) 包含三个 2
        assertArrayEquals(new int[]{1, 4}, SortUtils.getBound(arr, INT_COMPARATOR, 2, 2));
    }

    @Test
    void getBound_Duplicates_RangeStretchingBeyondDuplicates() {
        Integer[] arr = {1, 2, 2, 2, 3, 4};
        // lower_bound(2)=1, upper_bound(3)=5 -> 区间 [1,5) 包含 2,2,2,3
        assertArrayEquals(new int[]{1, 5}, SortUtils.getBound(arr, INT_COMPARATOR, 2, 3));
    }

    @Test
    void getBound_ValuesEqual() {
        Integer[] arr = {1, 3, 3, 3, 5};
        // lower_bound(3)=1, upper_bound(3)=4
        assertArrayEquals(new int[]{1, 4}, SortUtils.getBound(arr, INT_COMPARATOR, 3, 3));
    }

    @Test
    void getBound_OneValuePresentOtherAbsent() {
        Integer[] arr = {1, 3, 5, 7, 9};
        // lower_bound(4)=2, upper_bound(6)=3
        assertArrayEquals(new int[]{2, 3}, SortUtils.getBound(arr, INT_COMPARATOR, 4, 6));
    }

    // ---------- 边界 ----------
    @Test
    void getBound_SmallerLargerThanAll() {
        Integer[] arr = {2, 4, 6};
        // all values > 6 => lower_bound(7)=3, upper_bound(8)=3
        assertArrayEquals(new int[]{3, 3}, SortUtils.getBound(arr, INT_COMPARATOR, 7, 8));
    }

    @Test
    void getBound_LargerSmallerThanAll() {
        Integer[] arr = {2, 4, 6};
        // all values < 1 => lower_bound(0)=0, upper_bound(1)=0
        assertArrayEquals(new int[]{0, 0}, SortUtils.getBound(arr, INT_COMPARATOR, 0, 1));
    }

    @Test
    void getBound_RangeCoversWholeArray() {
        Integer[] arr = {1, 2, 3};
        // lower_bound(1)=0, upper_bound(3)=3
        assertArrayEquals(new int[]{0, 3}, SortUtils.getBound(arr, INT_COMPARATOR, 1, 3));
    }

    // ---------- 逆序比较器 ----------
    @Test
    void getBound_ReverseOrder() {
        Integer[] arr = {9, 7, 5, 3, 1};
        Comparator<Integer> rev = Comparator.reverseOrder();
        // 逆序下，lower_bound(8)=1 (第一个<=8)，upper_bound(4)=3 (第一个<4) -> [0,3)
        // 注意：对于逆序，lower_bound 仍返回第一个“不小于”(即<=)的位置，upper_bound返回第一个“大于”(即<)的位置
        assertArrayEquals(new int[]{1, 3}, SortUtils.getBound(arr, rev, 8, 4));
    }

    // ---------- 字符串 ----------
    @Test
    void getBound_StringArray() {
        String[] arr = {"apple", "banana", "cherry", "cherry", "date"};
        // lower_bound("apricot")=1, upper_bound("cherry")=4
        assertArrayEquals(new int[]{1, 4}, SortUtils.getBound(arr, STR_COMPARATOR, "apricot", "cherry"));
        // 相等时 cherry -> lower=2, upper=4
        assertArrayEquals(new int[]{2, 4}, SortUtils.getBound(arr, STR_COMPARATOR, "cherry", "cherry"));
    }


    @Test
    void getBound() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        int[] range = SortUtils.getBound(data, comparator, 1.75, 3.5);
        assertArrayEquals(new int[]{2, 6}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.75));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.5));

        // left end
        range = SortUtils.getBound(data, comparator, 0.5, 3.0);
        assertArrayEquals(new int[]{0, 5}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 0.5));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.0));

        // right end
        range = SortUtils.getBound(data, comparator, 1.75, 6.0);
        assertArrayEquals(new int[]{2, 7}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.75));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 6.0));

        // both end
        range = SortUtils.getBound(data, comparator, 0.0, 5.0);
        assertArrayEquals(range, new int[]{0, data.length});
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 0.0));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 5.0));

        // out
        range = SortUtils.getBound(data, comparator, 0.1, 0.3);
        assertArrayEquals(new int[]{0, 0}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 0.1));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 0.3));

        range = SortUtils.getBound(data, comparator, 4.1, 4.2);
        assertArrayEquals(new int[]{data.length, data.length}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 4.1));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 4.2));

        // between
        range = SortUtils.getBound(data, comparator, 1.6, 1.8);
        assertArrayEquals(new int[]{2, 2}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.6));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 1.8));
    }

    @Test
    void getBound_Equal() {
        Double[] data = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        // low equal
        int[] range = SortUtils.getBound(data, comparator, 1.5, 3.25);
        assertArrayEquals(new int[]{1, 9}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.5));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.25));

        assertArrayEquals(new Double[]{1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0},
                Arrays.copyOfRange(data, range[0], range[1]));

        // high equal
        range = SortUtils.getBound(data, comparator, 1.6, 3.0);
        assertArrayEquals(new int[]{5, 9}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.6));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.0));

        // bot equal
        range = SortUtils.getBound(data, comparator, 1.5, 3.0);
        assertArrayEquals(new int[]{1, 9}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.5));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.0));

        // both not equal
        range = SortUtils.getBound(data, comparator, 1.6, 3.2);
        assertArrayEquals(new int[]{5, 9}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, comparator, 1.6));
        assertEquals(range[1], SortUtils.getUpperBound(data, comparator, 3.2));
    }

    @Test
    void getBound_Boundary() {
        Double[] data = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        int[] range = SortUtils.getBound(data, Comparator.comparingDouble(o -> o), 1.5, 4.);
        assertArrayEquals(new int[]{1, 11}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, Comparator.comparingDouble(o -> o), 1.5));
        assertEquals(range[1], SortUtils.getUpperBound(data, Comparator.comparingDouble(o -> o), 4.));

        range = SortUtils.getBound(data, Comparator.comparingDouble(o -> o), 1.0, 3.);
        assertArrayEquals(new int[]{0, 9}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range[1], SortUtils.getUpperBound(data, Comparator.comparingDouble(o -> o), 3.));

        range = SortUtils.getBound(data, Comparator.comparingDouble(o -> o), 1.0, 4.);
        assertArrayEquals(new int[]{0, 11}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range[1], SortUtils.getUpperBound(data, Comparator.comparingDouble(o -> o), 4.0));

        range = SortUtils.getBound(data, Comparator.comparingDouble(o -> o), 1.0, 5.);
        assertArrayEquals(new int[]{0, 11}, range);
        assertEquals(range[0], SortUtils.getLowerBound(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range[1], SortUtils.getUpperBound(data, Comparator.comparingDouble(o -> o), 5.0));
    }


    @Test
    void binarySearchDouble() {
        double[] data = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        int[] range = SortUtils.getBound(data, 1.75, 3.5);
        assertArrayEquals(new int[]{2, 6}, range);

        range = SortUtils.getBound(data, 0.0, 5.0);
        assertArrayEquals(new int[]{0, data.length}, range);
    }

    @Test
    void binarySearchDoubleBounds() {
        double[] data = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        int[] range = SortUtils.getBound(data, 1.5, 3.25);
        assertArrayEquals(new int[]{1, 9}, range);
    }

    @Test
    void binarySearchDoubleOut() {
        assertArrayEquals(new int[]{10, 11},
                SortUtils.getBound(new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0},
                        4.0, 6.0));
        assertArrayEquals(new int[]{8, 10},
                SortUtils.getBound(new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 4.0, 4.0},
                        4.0, 6.0));
    }


    @Test
    void binarySearchOut() {
        double[] data = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        // low
        int[] range = SortUtils.getBound(data, 0.1, 0.2);
        assertArrayEquals(new int[]{0, 0}, range);
        assertArrayEquals(new int[]{data.length, data.length}, SortUtils.getBound(data, 4.1, 4.2));

        assertArrayEquals(new int[]{0, 1}, SortUtils.getBound(data, 0.1, 1.0));
        assertArrayEquals(new int[]{0, 5}, SortUtils.getBound(data, 0.1, 1.5));
        assertArrayEquals(new int[]{0, 9}, SortUtils.getBound(data, 1.0, 3.0));

        assertArrayEquals(new int[]{7, 11}, SortUtils.getBound(data, 3.0, 4.0));
        assertArrayEquals(new int[]{7, 11}, SortUtils.getBound(data, 3.0, 4.1));
        assertArrayEquals(new int[]{10, 11}, SortUtils.getBound(data, 4.0, 4.0));
        System.out.println(Arrays.toString(SortUtils.getBound(data, 1.6, 1.8)));
    }

    @Test
    void binarySearchRange() {
        Double[] data1 = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        assertArrayEquals(new int[]{2, 6}, SortUtils.getBound(data1, 2, 6, comparator, 0.0, 10.0));
        assertArrayEquals(new int[]{3, 5}, SortUtils.getBound(data1, 2, 6, comparator, 2.1, 3.4));
        assertArrayEquals(new int[]{2, 9}, SortUtils.getBound(data2, 2, 9, comparator, 0.0, 10.0));
        assertArrayEquals(new int[]{2, 9}, SortUtils.getBound(data2, 2, 9, comparator, 1.4, 10.0));
        assertArrayEquals(new int[]{2, 9}, SortUtils.getBound(data2, 2, 9, comparator, 1.5, 10.0));
        assertArrayEquals(new int[]{5, 9}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 10.0));
        assertArrayEquals(new int[]{5, 9}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 3.5));
        assertArrayEquals(new int[]{5, 9}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 3.0));
        assertArrayEquals(new int[]{5, 7}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 2.9));
        assertArrayEquals(new int[]{5, 7}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 2.5));
        assertArrayEquals(new int[]{5, 6}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 2.4));
        assertArrayEquals(new int[]{5, 6}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 2.0));
        assertArrayEquals(new int[]{5, 5}, SortUtils.getBound(data2, 2, 9, comparator, 1.51, 1.9));
    }
}