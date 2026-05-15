package pdk.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 15 May 2026, 3:05 PM
 */
@DisplayName("MetaObject test")
class MetaObjectTest {

    private MetaObject meta;

    @BeforeEach
    void setUp() {
        meta = new MetaObject();
    }

    @Nested
    @DisplayName("addMeta(String key, Object value)")
    class AddMeta {

        @Test
        void shouldAddStringValue() {
            meta.addMeta("name", "test");
            assertEquals("test", meta.getMeta("name"));
        }

        @Test
        void shouldAllowNullValue() {
            meta.addMeta("key", null);
            assertNull(meta.getMeta("key"));
            assertTrue(meta.containsMetaKey("key"));
        }

        @Test
        void shouldThrowNPEOnNullKey() {
            NullPointerException ex = assertThrows(NullPointerException.class,
                    () -> meta.addMeta(null, "value"));
            assertEquals("key must not be null", ex.getMessage());
        }

        @Test
        void shouldOverwriteExistingKey() {
            meta.addMeta("key", "old");
            meta.addMeta("key", "new");
            assertEquals("new", meta.getMeta("key"));
        }
    }

    @Nested
    @DisplayName("addAllMeta(MetaObject object)")
    class AddAllMeta {

        @Test
        void shouldMergeMetadata() {
            MetaObject other = new MetaObject();
            other.addMeta("a", 1);
            other.addMeta("b", 2);

            meta.addAllMeta(other);

            assertEquals(1, meta.getMetaInt("a"));
            assertEquals(2, meta.getMetaInt("b"));
        }

        @Test
        void shouldIgnoreNullObject() {
            assertDoesNotThrow(() -> meta.addAllMeta(null));
            assertFalse(meta.hasMeta());
        }

        @Test
        void shouldIgnoreSelfReference() {
            meta.addMeta("x", 1);
            assertDoesNotThrow(() -> meta.addAllMeta(meta));
            assertEquals(1, meta.getMetaKeys().size());
        }

        @Test
        void shouldOverwriteDuringMerge() {
            meta.addMeta("x", 100);
            MetaObject other = new MetaObject();
            other.addMeta("x", 200);
            meta.addAllMeta(other);
            assertEquals(200, meta.getMetaInt("x"));
        }
    }

    @Nested
    @DisplayName("getMetaAs(String key, Class<T> type)")
    class GetMetaAs {

        @Test
        void shouldReturnCorrectType() {
            meta.addMeta("age", 30);
            Integer age = meta.getMetaAs("age", Integer.class);
            assertEquals(30, age);
        }

        @Test
        void shouldReturnNullOnTypeMismatch() {
            meta.addMeta("age", "thirty");
            assertNull(meta.getMetaAs("age", Integer.class));
        }

        @Test
        void shouldReturnNullOnMissingKey() {
            assertNull(meta.getMetaAs("missing", String.class));
        }

        @Test
        void shouldReturnNullWhenValueIsNull() {
            meta.addMeta("key", null);
            assertNull(meta.getMetaAs("key", String.class));
        }
    }

    @Nested
    @DisplayName("getMetaKeys()")
    class GetMetaKeys {

        @Test
        void shouldReturnEmptySetForEmptyObject() {
            Set<String> keys = meta.getMetaKeys();
            assertTrue(keys.isEmpty());
            // 确保返回的是不可变空集合（Collections.emptySet()），尝试修改应抛异常
            assertThrows(UnsupportedOperationException.class, () -> keys.add("new"));
        }

        @Test
        void shouldReturnIndependentCopy() {
            meta.addMeta("a", 1);
            Set<String> keys = meta.getMetaKeys();
            keys.clear(); // 不会影响原对象
            assertTrue(meta.containsMetaKey("a"));
        }
    }

    @Nested
    @DisplayName("getSortedMetaKeys()")
    class GetSortedMetaKeys {

        @Test
        void shouldReturnEmptyListWhenEmpty() {
            List<String> sorted = meta.getSortedMetaKeys();
            assertTrue(sorted.isEmpty());
            // 验证不可变
            assertThrows(UnsupportedOperationException.class, () -> sorted.add("x"));
        }

        @Test
        void shouldReturnNaturallySortedKeys() {
            meta.addMeta("c", 1);
            meta.addMeta("a", 2);
            meta.addMeta("b", 3);

            List<String> sorted = meta.getSortedMetaKeys();
            assertEquals(Arrays.asList("a", "b", "c"), sorted);
        }

        @Test
        void shouldBeSafeWithoutNullKeys() {
            meta.addMeta("z", 1);
            meta.addMeta("A", 2);
            List<String> sorted = meta.getSortedMetaKeys();
            assertEquals(Arrays.asList("A", "z"), sorted); // 自然顺序：大写字母排在小写字母前
        }

        @Test
        @DisplayName("返回的列表是独立副本")
        void shouldReturnIndependentList() {
            meta.addMeta("key", 1);
            List<String> sorted = meta.getSortedMetaKeys();
            sorted.clear();
            assertTrue(meta.containsMetaKey("key"));
        }
    }

    @Nested
    @DisplayName("containsMetaKey(String key)")
    class ContainsMetaKey {

        @Test
        void shouldReturnTrueForExistingKey() {
            meta.addMeta("exist", 1);
            assertTrue(meta.containsMetaKey("exist"));
        }

        @Test
        void shouldReturnFalseForMissingKey() {
            assertFalse(meta.containsMetaKey("missing"));
        }

        @Test
        @DisplayName("值为 null 但键存在返回 true")
        void shouldReturnTrueWhenKeyExistsWithNullValue() {
            meta.addMeta("nullValue", null);
            assertTrue(meta.containsMetaKey("nullValue"));
        }
    }

    @Nested
    @DisplayName("hasMeta()")
    class HasMeta {

        @Test
        @DisplayName("空对象返回 false")
        void shouldReturnFalseWhenEmpty() {
            assertFalse(meta.hasMeta());
        }

        @Test
        @DisplayName("添加元素后返回 true")
        void shouldReturnTrueAfterAdding() {
            meta.addMeta("x", 1);
            assertTrue(meta.hasMeta());
        }

        @Test
        @DisplayName("仅包含 null 值也视为有元数据")
        void shouldReturnTrueWhenOnlyNullValuePresent() {
            meta.addMeta("x", null);
            assertTrue(meta.hasMeta());
        }
    }

    @Nested
    @DisplayName("removeMeta(String key)")
    class RemoveMeta {

        @Test
        @DisplayName("移除存在的键")
        void shouldRemoveExistingKey() {
            meta.addMeta("key", 1);
            meta.removeMeta("key");
            assertFalse(meta.containsMetaKey("key"));
        }

        @Test
        @DisplayName("移除不存在的键不抛异常")
        void shouldBeSafeForMissingKey() {
            assertDoesNotThrow(() -> meta.removeMeta("missing"));
        }

        @Test
        @DisplayName("移除后其他键不受影响")
        void shouldNotAffectOtherKeys() {
            meta.addMeta("a", 1);
            meta.addMeta("b", 2);
            meta.removeMeta("a");
            assertFalse(meta.containsMetaKey("a"));
            assertTrue(meta.containsMetaKey("b"));
        }
    }

    @Nested
    @DisplayName("getMeta(String metaKey)")
    class GetMeta {

        @Test
        @DisplayName("返回存储的原始对象")
        void shouldReturnStoredValue() {
            meta.addMeta("key", "value");
            assertEquals("value", meta.getMeta("key"));
        }

        @Test
        @DisplayName("key 不存在返回 null")
        void shouldReturnNullForMissingKey() {
            assertNull(meta.getMeta("missing"));
        }
    }

    @Nested
    @DisplayName("类型便捷方法")
    class TypedGetters {

        @Test
        @DisplayName("getMetaInt 返回 Integer")
        void getMetaIntShouldReturnInteger() {
            meta.addMeta("num", 42);
            assertEquals(42, meta.getMetaInt("num"));
            // 类型不匹配
            meta.addMeta("str", "hello");
            assertNull(meta.getMetaInt("str"));
        }

        @Test
        @DisplayName("getMetaLong 返回 Long")
        void getMetaLongShouldReturnLong() {
            meta.addMeta("long", 999L);
            assertEquals(999L, meta.getMetaLong("long"));
        }

        @Test
        @DisplayName("getMetaBool 返回 Boolean")
        void getMetaBoolShouldReturnBoolean() {
            meta.addMeta("flag", true);
            assertTrue(meta.getMetaBool("flag"));
            meta.addMeta("nonBool", 0);
            assertNull(meta.getMetaBool("nonBool"));
        }

        @Test
        @DisplayName("getMetaDouble 返回 Double")
        void getMetaDoubleShouldReturnDouble() {
            meta.addMeta("pi", 3.14);
            assertEquals(3.14, meta.getMetaDouble("pi"));
        }
    }

    @Nested
    @DisplayName("getMetaString(String metaKey)")
    class GetMetaString {

        @Test
        @DisplayName("返回字符串值")
        void shouldReturnStringValue() {
            meta.addMeta("key", "hello");
            assertEquals("hello", meta.getMetaString("key"));
        }

        @Test
        @DisplayName("非字符串值返回其 toString()")
        void shouldReturnToStringForNonString() {
            meta.addMeta("num", 123);
            assertEquals("123", meta.getMetaString("num"));
        }

        @Test
        @DisplayName("key 不存在返回空字符串")
        void shouldReturnEmptyStringForMissingKey() {
            assertEquals("", meta.getMetaString("missing"));
        }

        @Test
        @DisplayName("值为 null 返回空字符串")
        void shouldReturnEmptyStringWhenValueIsNull() {
            meta.addMeta("null", null);
            assertEquals("", meta.getMetaString("null"));
        }
    }

    @Nested
    @DisplayName("clearAllMeta()")
    class ClearAllMeta {

        @Test
        @DisplayName("清空所有元数据")
        void shouldClearAllMetadata() {
            meta.addMeta("a", 1);
            meta.addMeta("b", 2);
            meta.clearAllMeta();

            assertFalse(meta.hasMeta());
            assertTrue(meta.getMetaKeys().isEmpty());
            assertNull(meta.getMeta("a"));
        }

        @Test
        @DisplayName("清空后可以重新添加")
        void shouldAllowReaddingAfterClear() {
            meta.addMeta("x", 1);
            meta.clearAllMeta();
            meta.addMeta("y", 2);
            assertEquals(2, meta.getMeta("y"));
            assertEquals(1, meta.getMetaKeys().size());
        }
    }

    @Test
    @DisplayName("综合场景：模拟元数据容器的完整生命周期")
    void integrationTest() {
        meta.addMeta("name", "Alice");
        meta.addMeta("age", 30);
        meta.addMeta("active", true);

        assertTrue(meta.hasMeta());
        assertTrue(meta.containsMetaKey("name"));
        Set<String> keys = meta.getMetaKeys();
        assertEquals(3, keys.size());
        assertTrue(keys.containsAll(Arrays.asList("name", "age", "active")));
        assertEquals(Arrays.asList("active", "age", "name"), meta.getSortedMetaKeys());

        assertEquals(30, meta.getMetaAs("age", Integer.class));
        assertEquals(30, meta.getMetaInt("age"));
        assertTrue(meta.getMetaBool("active"));
        assertEquals("Alice", meta.getMetaString("name"));

        // 合并另一个对象
        MetaObject other = new MetaObject();
        other.addMeta("city", "Paris");
        other.addMeta("age", 31); // 会覆盖
        meta.addAllMeta(other);

        assertEquals(31, meta.getMetaInt("age"));
        assertEquals("Paris", meta.getMeta("city"));

        meta.removeMeta("city");
        assertFalse(meta.containsMetaKey("city"));

        meta.clearAllMeta();
        assertFalse(meta.hasMeta());
    }
}