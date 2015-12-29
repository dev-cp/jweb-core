package org.jweb.common.cache.impl;

import org.apache.shiro.cache.CacheException;
import org.jweb.common.cache.CacheI;
import org.jweb.common.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

public class MemCacheManager implements CacheManager {

	 /**
     * 日志服务对象
     */
    private static final Logger log = LoggerFactory.getLogger(MemCacheManager.class);
    
    /**
     * 用于创建caches的memcached管理器，这里使用自定义的memcached管理器
     * The EhCache cache manager used by this implementation to create caches.
     */
    protected ICacheManager<IMemcachedCache> manager;

    /**
     * Indicates if the CacheManager instance was implicitly/automatically created by this instance, indicating that
     * it should be automatically cleaned up as well on shutdown.
     */
    private boolean cacheManagerImplicitlyCreated = false;

    /**
     * 默认是类路径下的memcached.xml配置文件，注意，这里的文件路径不能带类似于spring下用到的
     * classpath、file、http等前缀，因为在memcached实现中是直接使用java ClassLoader来加载的，
     * 并没有提供对路径前缀的解析
     * Classpath file location of the ehcache CacheManager config file.
     */
    private String cacheManagerConfigFile = "memcached.xml";


    /**
     * 获取被包裹的memcached缓存管理器
     * @return
     */
    public ICacheManager<IMemcachedCache> getCacheManager() {
        return manager;
    }

    /**
     * 设置被包裹的memcached缓存管理器
     * @param manager
     */
    public void setCacheManager(ICacheManager<IMemcachedCache> manager) {
        this.manager = manager;
    }

    /**
     * 获取memcached缓存服务的配置文件，例如"memcached.xml"，配置文件应该能够在类路径下面找到
     * @return
     */
    public String getCacheManagerConfigFile() {
        return this.cacheManagerConfigFile;
    }

    /**
     * 设置memcached缓存服务的配置文件，例如"memcached.xml"，配置文件应该能够在类路径下面找到
     * @param classpathLocation
     */
    public void setCacheManagerConfigFile(String classpathLocation) {
        this.cacheManagerConfigFile = classpathLocation;
    }

    

    /**
     * 获取缓存服务对象，可以通过该服务对象进行缓存的增删查改操作
     */
    public final CacheI<String,Object> getCache(String name) {

        if (log.isTraceEnabled()) {
            log.trace("Acquiring EhCache instance named [" + name + "]");
        }

//            net.sf.ehcache.Ehcache cache = ensureCacheManager().getEhcache(name);
            
            IMemcachedCache cache = ensureCacheManager().getCache(name);
            
            if (cache == null) {
                if (log.isInfoEnabled()) {
                    log.info("Cache with name '{}' does not yet exist.  Creating now.", name);
                }
//                this.manager.addCache(name);

                cache = manager.getCache(name);

                if (log.isInfoEnabled()) {
                    log.info("Added EhCache named [" + name + "]");
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Using existing MemCache named [" + cacheManagerConfigFile + "]");
                }
            }
            return new MemcachedCache(cache);
    }

    /**
     * Initializes this instance.
     * <p/>
     * If a {@link #setCacheManager CacheManager} has been
     * explicitly set (e.g. via Dependency Injection or programatically) prior to calling this
     * method, this method does nothing.
     * <p/>
     * However, if no {@code CacheManager} has been set, the default Ehcache singleton will be initialized, where
     * Ehcache will look for an {@code ehcache.xml} file at the root of the classpath.  If one is not found,
     * Ehcache will use its own failsafe configuration file.
     * <p/>
     * Because Shiro cannot use the failsafe defaults (fail-safe expunges cached objects after 2 minutes,
     * something not desirable for Shiro sessions), this class manages an internal default configuration for
     * this case.
     *
     * @throws org.apache.shiro.cache.CacheException
     *          if there are any CacheExceptions thrown by EhCache.
     * @see net.sf.ehcache.CacheManager#create
     */
    public final void init() throws CacheException {
        ensureCacheManager();
    }

    private ICacheManager<IMemcachedCache> ensureCacheManager() {
//    	cacheManagerImplicitlyCreated = true;
//    	return this.manager;
    	
        try {
            if (this.manager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("cacheManager property not set.  Constructing CacheManager instance... ");
                }
                //using the CacheManager constructor, the resulting instance is _not_ a VM singleton
                //(as would be the case by calling CacheManager.getInstance().  We do not use the getInstance here
                //because we need to know if we need to destroy the CacheManager instance - using the static call,
                //we don't know which component is responsible for shutting it down.  By using a single EhCacheManager,
                //it will always know to shut down the instance if it was responsible for creating it.
//                this.manager = new MemcachedCacheManagerAdapter(this.cacheManagerConfigFile);
                manager = CacheUtil.getCacheManager(IMemcachedCache.class, MemcachedCacheManager.class.getName());
        		
        		manager.setConfigFile(this.cacheManagerConfigFile);
        		
        		manager.setResponseStatInterval(5 * 1000);
        		
        		manager.start();
        		
        		
                if (log.isTraceEnabled()) {
                    log.trace("instantiated Ehcache CacheManager instance.");
                }
                cacheManagerImplicitlyCreated = true;
                if (log.isDebugEnabled()) {
                    log.debug("implicit cacheManager created successfully.");
                }
            }
            return this.manager;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * Shuts-down the wrapped Ehcache CacheManager <b>only if implicitly created</b>.
     * <p/>
     * If another component injected
     * a non-null CacheManager into this instace before calling {@link #init() init}, this instance expects that same
     * component to also destroy the CacheManager instance, and it will not attempt to do so.
     */
    public void destroy() {
        if (cacheManagerImplicitlyCreated) {
            try {
            	ICacheManager<IMemcachedCache> cacheMgr = getCacheManager();
            	cacheMgr.stop();
            	
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("Unable to cleanly shutdown implicitly created CacheManager instance.  " +
                            "Ignoring (shutting down)...");
                }
            }
            cacheManagerImplicitlyCreated = false;
        }
    }

}
