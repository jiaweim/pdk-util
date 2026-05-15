package pdk.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * Class support adding arbitrary metadata.
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 15 May 2026, 2:54 PM
 */
public class MetaObject {

    private final HashMap<String, Object> metaMap_ = new HashMap<>();

    /**
     * Add a metadata.
     * <p>
     * If data with the same key already exists, overwrite the original data.
     *
     * @param key   parameter key.
     * @param value parameter value.
     */
    public void addMeta(@NonNull String key, @Nullable Object value) {
        Objects.requireNonNull(key, "key must not be null");
        metaMap_.put(key, value);
    }


    /**
     * Add all metadata of another object.
     *
     * @param object another {@link MetaObject}
     */
    public void addAllMeta(@Nullable MetaObject object) {
        if (object == null || object == this) return;
        for (Map.Entry<String, Object> entry : object.metaMap_.entrySet()) {
            addMeta(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Return the data corresponding to the specified key in the designated type. Return null if:
     *
     * <ul>
     *     <li>Types are incompatible</li>
     *     <li>Does not contain data of the specified key</li>
     *     <li>The value itself is null.</li>
     * </ul>
     *
     * @param key  metadata key
     * @param type class of the type to return
     * @param <T>  type
     * @return metadata, or null if the types are incompatible, or it does not contain data of the specified key.
     */
    public <T> T getMetaAs(String key, Class<T> type) {
        Object value = metaMap_.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    /**
     * @return a copy of all parameter keys, or an empty Set if there is no parameters.
     */
    public Set<String> getMetaKeys() {
        if (metaMap_.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(metaMap_.keySet());
    }

    /**
     * @return the parameter key list with titles ordered.
     */
    public List<String> getSortedMetaKeys() {
        if (metaMap_.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> keyList = new ArrayList<>(metaMap_.keySet());
        keyList.sort(Comparator.naturalOrder());
        return keyList;
    }

    /**
     * Return true if this object has metadata with the specified key
     *
     * @param key metadata key
     * @return true if this object has metadata with the specified key
     */
    public boolean containsMetaKey(String key) {
        return metaMap_.containsKey(key);
    }

    /**
     * Whether this object contains metadata
     *
     * @return true if this object contains metadata
     */
    public boolean hasMeta() {
        return !metaMap_.isEmpty();
    }

    /**
     * Removes a parameter.
     *
     * @param key the key of the parameter
     */
    public void removeMeta(String key) {
        metaMap_.remove(key);
    }

    /**
     * Returns the metadata of a given key.
     *
     * @param metaKey the desired parameter key.
     * @return parameter value.
     */
    public @Nullable Object getMeta(String metaKey) {
        return metaMap_.get(metaKey);
    }

    /**
     * Returns the parameter value
     *
     * @param metaKey the desired parameter
     * @return the value stored, null for absent.
     */
    public @Nullable Integer getMetaInt(String metaKey) {
        return getMetaAs(metaKey, Integer.class);
    }

    /**
     * Returns the parameter value
     *
     * @param metaKey the desired parameter
     * @return the value stored, null for absent.
     */
    public @Nullable Long getMetaLong(String metaKey) {
        return getMetaAs(metaKey, Long.class);
    }

    public @Nullable Boolean getMetaBool(String key) {
        return getMetaAs(key, Boolean.class);
    }

    /**
     * Returns the parameter value
     *
     * @param metaKey the desired parameter
     * @return the value stored.
     */
    public @Nullable Double getMetaDouble(String metaKey) {
        return getMetaAs(metaKey, Double.class);
    }

    /**
     * Returns the parameter value, for other types, its toString() method is called. empty for absent.
     *
     * @param metaKey the desired parameter
     * @return the value stored in string, "" for absent.
     */
    public String getMetaString(String metaKey) {
        Object meta = getMeta(metaKey);
        if (meta == null) {
            return "";
        }
        return meta.toString();
    }

    /**
     * Clears the loaded parameters.
     */
    public void clearAllMeta() {
        metaMap_.clear();
    }
}
