package org.jweb.common.cache;

import java.util.Collection;
import java.util.Set;


/**
 * 缓存服务对象接口规范，定义了缓存服务对象必须实现的公共方法
 * @author wupan
 *
 * @param <K>
 * @param <V>
 */
public interface CacheI<K, V> {

	/**
	 * 根据键获取值
     * Returns the Cached value stored under the specified {@code key} or
     * {@code null} if there is no Cache entry for that {@code key}.
     *
     * @param key the key that the value was previous added with
     * @return the cached object or {@code null} if there is no entry for the specified {@code key}
     * @throws CacheException if there is a problem accessing the underlying cache system
     */
    public V get(K key);

    /**
     * 添加或更新缓存值，并返回添加的值或更新前的值
     * Adds a Cache entry.
     *
     * @param key   the key used to identify the object being stored.
     * @param value the value to be stored in the cache.
     * @return the previous value associated with the given {@code key} or {@code null} if there was previous value
     * @throws CacheException if there is a problem accessing the underlying cache system
     */
    public V put(K key, V value);

    /**
     * 根据键移除缓存值，并返回移除的值
     * Remove the cache entry corresponding to the specified key.
     *
     * @param key the key of the entry to be removed.
     * @return the previous value associated with the given {@code key} or {@code null} if there was previous value
     * @throws CacheException if there is a problem accessing the underlying cache system
     */
    public V remove(K key);

    /**
     * 清空缓存
     * Clear all entries from the cache.
     *
     * @throws CacheException if there is a problem accessing the underlying cache system
     */
    public void clear();

    /**
     * 当前缓存的数量
     * Returns the number of entries in the cache.
     *
     * @return the number of entries in the cache.
     */
    public int size();

    /**
     * 获取所有缓存的键集
     * Returns a view of all the keys for entries contained in this cache.
     *
     * @return a view of all the keys for entries contained in this cache.
     */
    public Set<K> keys();

    /**
     * 获取所有缓存的值集
     * Returns a view of all of the values contained in this cache.
     *
     * @return a view of all of the values contained in this cache.
     */
    public Collection<V> values();
}
