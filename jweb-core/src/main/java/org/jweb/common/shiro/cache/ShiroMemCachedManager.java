package org.jweb.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

public class ShiroMemCachedManager implements CacheManager, Initializable, Destroyable{

	
	 /**
     * This class's private log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(ShiroMemCachedManager.class);

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
     * Default no argument constructor
     */
    public ShiroMemCachedManager() {
    }

    /**
     * Returns the wrapped Ehcache {@link net.sf.ehcache.CacheManager CacheManager} instance.
     *
     * @return the wrapped Ehcache {@link net.sf.ehcache.CacheManager CacheManager} instance.
     */
    public ICacheManager<IMemcachedCache> getCacheManager() {
        return manager;
    }

    /**
     * Sets the wrapped Ehcache {@link net.sf.ehcache.CacheManager CacheManager} instance.
     *
     * @param manager the wrapped Ehcache {@link net.sf.ehcache.CacheManager CacheManager} instance.
     */
    public void setCacheManager(ICacheManager<IMemcachedCache> manager) {
        this.manager = manager;
    }

    /**
     * Returns the resource location of the config file used to initialize a new
     * EhCache CacheManager instance.  The string can be any resource path supported by the
     * {@link org.apache.shiro.io.ResourceUtils#getInputStreamForPath(String)} call.
     * <p/>
     * This property is ignored if the CacheManager instance is injected directly - that is, it is only used to
     * lazily create a CacheManager if one is not already provided.
     *
     * @return the resource location of the config file used to initialize the wrapped
     *         EhCache CacheManager instance.
     */
    public String getCacheManagerConfigFile() {
        return this.cacheManagerConfigFile;
    }

    /**
     * Sets the resource location of the config file used to initialize the wrapped
     * EhCache CacheManager instance.  The string can be any resource path supported by the
     * {@link org.apache.shiro.io.ResourceUtils#getInputStreamForPath(String)} call.
     * <p/>
     * This property is ignored if the CacheManager instance is injected directly - that is, it is only used to
     * lazily create a CacheManager if one is not already provided.
     *
     * @param classpathLocation resource location of the config file used to create the wrapped
     *                          EhCache CacheManager instance.
     */
    public void setCacheManagerConfigFile(String classpathLocation) {
        this.cacheManagerConfigFile = classpathLocation;
    }

    

    /**
     * Loads an existing EhCache from the cache manager, or starts a new cache if one is not found.
     *
     * @param name the name of the cache to load/create.
     */
    public final  Cache<String,Object> getCache(String name) throws CacheException {

        if (log.isTraceEnabled()) {
            log.trace("Acquiring EhCache instance named [" + name + "]");
        }

        try {
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
//            return new EhCache<K, V>(cache);
            return new MemCached(cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
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
