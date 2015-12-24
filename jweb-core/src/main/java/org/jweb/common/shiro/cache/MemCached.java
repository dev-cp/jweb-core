package org.jweb.common.shiro.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.ehcache.EhCache;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alisoft.xplatform.asf.cache.IMemcachedCache;

/**
 * 按照shiro 缓存规范实现的memCached，shiro提供的Ehcache实现相对于MemCached来说，
 * 无法实现互不通讯的缓存集群，同时，shiro实现的ehcache是将数据缓存在每个服务系统节点上
 * 的，如果服务系统节点挂掉，则它的ehcache缓存也就挂掉，memcached将会独立于服务系统节点
 * 进行缓存服务器分布式集群，与服务系统通讯是靠memcached的客户端模块进行的
 * @author wupan
 *
 * @param <K>
 * @param <V>
 */
public class MemCached implements Cache<String, Object>{
	
	/**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(EhCache.class);

    /**
     * 
     * 被包裹的IMemcachedCache实例
     */
    private IMemcachedCache cache;

    /**
     * Constructs a new EhCache instance with the given cache.
     *
     * @param cache - delegate EhCache instance this Shiro cache instance will wrap.
     */
    public MemCached(IMemcachedCache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    /**
     * Gets a value of an element which matches the given key.
     *
     * @param key the key of the element to return.
     * @return The value placed into the cache with an earlier put, or null if not found or expired
     */
    public Object get(String key) throws CacheException {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Getting object from memcached  for key [" + key + "]");
            }
            if (key == null) {
                return null;
            } else {
                Object element = cache.get(key);
                
                if (element == null) {
                    if (log.isTraceEnabled()) {
                        log.trace("Element for [" + key + "] is null.");
                    }
                    return null;
                } else {
                    //noinspection unchecked
                    return element;
                }
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Puts an object into the cache.
     *
     * @param key   the key.
     * @param value the value.
     */
    public Object put(String key, Object value) throws CacheException {
        if (log.isTraceEnabled()) {
            log.trace("Putting object in memcached  for key [" + key + "]");
        }
        try {
            Object previous = get(key);
          //相当于memcached的set命令，向memcached中新增一条缓存，如果键存在，则替换原值
            //这里使用memcached的set命令，因为本类中未定义add方法，也未定义replace方法，只有set兼备add和replace的效果
            cache.put(key, value);
//            cache.replace(key, value);//相当于memcached的replace命令，仅当键存在，才做替换操作
//            cache.add(key, value);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Removes the element which matches the key.
     *
     * <p>If no element matches, nothing is removed and no Exception is thrown.</p>
     *
     * @param key the key of the element to remove
     */
    public Object remove(String key) throws CacheException {
        if (log.isTraceEnabled()) {
            log.trace("Removing object from memcached  for key [" + key + "]");
        }
        try {
            Object previous = get(key);
            cache.remove(key);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Removes all elements in the cache, but leaves the cache in a useable state.
     */
    public void clear() throws CacheException {
        if (log.isTraceEnabled()) {
            log.trace("Clearing all objects from memcached ");
        }
        try {
        	cache.clear();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public int size() {
        try {
        	return cache.size();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public Set<String> keys() {
        try {
        	Set<String> keySet = cache.keySet(false);
//            @SuppressWarnings({"unchecked"})
//            
//            List<K> keys = cache.getKeys();
            if (!CollectionUtils.isEmpty(keySet)) {
                return keySet;
            } else {
                return Collections.emptySet();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public Collection<Object> values() {
        try {
        	Collection<Object> values = cache.values();
        	return values;
           
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

   

    /**
     * Returns &quot;MemCached [&quot; + cache.toString() + &quot;]&quot;
     *
     * @return &quot;MemcachedCache [&quot; + cache.toString() + &quot;]&quot;
     */
    public String toString() {
        return "MemcachedCache [" + cache.toString() + "]";
    }

}
