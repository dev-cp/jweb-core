package org.jweb.common.cache;


/**
 * 针对单点登录功能的缓存服务操作接口，本接口定义缓存管理器的操作方法
 * @author wupan
 *
 */
public interface CacheManager {

	/**
	 * 获取缓存服务对象
	 * @param name
	 * @return
	 */
	public <K, V> CacheI<K,V> getCache(String name);
}
