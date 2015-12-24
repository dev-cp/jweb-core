package org.jweb.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.PathMatchingFilter;

/**
 * 单点登出过滤器，该过滤器相当于cas提供的SingleSignOutFilter过滤器的功能
 * SingleSignOutFilter过滤器通过捕获所有请求，判断每个请求“handler.isLogoutRequest(request)”
 * 是否是登出通知，如果是，则调用SingleSignOutHandler#destroySession()方法，先根据登录票
 * 从session仓库中移除并取出session，再让session过期
 * 
 * 
 * 本过滤器是按照shiro规范编写的，目的是为了能将本过滤器加入shiro过滤器链中，内部实现逻辑和SingleSignOutFilter一样
 * 
 * @author wupan
 *
 */
public class SSOLogoutFilter extends PathMatchingFilter{
	/** Logger instance */
    private final Log log = LogFactory.getLog(getClass());
    
    private static final org.jasig.cas.client.session.SingleSignOutHandler casHandler = new org.jasig.cas.client.session.SingleSignOutHandler();
  
    private org.jweb.common.shiro.handler.SingleSignOutHandler cacheHandler = new org.jweb.common.shiro.handler.SingleSignOutHandler();
    
	@Override
	protected boolean onPreHandle(ServletRequest servletRequest,
			ServletResponse response, Object mappedValue) throws Exception {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		//默认优先使用cacheHandler，该处理器拥有缓存功能
		if(cacheHandler != null){
			
			if (cacheHandler.isTokenRequest(request)) {
				
				cacheHandler.recordSession(request);
				
	        } else if (cacheHandler.isLogoutRequest(request)) {//判断当前请求是否是通知退出的请求
				
	        	cacheHandler.destroySession(request);
	        	
		        
		        //shiro完成用户登出后，过滤器直接中断，不必继续执行过滤链，因为本次请求只是为了完成登出，而不是还要
		        //顺带做其他业务操作
		        return false;
			} else {
				//如果是其他请求，则继续过滤链操作
				log.trace("Ignoring URI " + request.getRequestURI());
			}
			//如果是非登出请求，则返回true,表示继续执行过滤链
			return true;
			
		} else {
			if (casHandler.isTokenRequest(request)) {
				
				casHandler.recordSession(request);
				
	        } else if (casHandler.isLogoutRequest(request)) {//判断当前请求是否是通知退出的请求
				
	        	casHandler.destroySession(request);
	        	
		        
		        //shiro完成用户登出后，过滤器直接中断，不必继续执行过滤链，因为本次请求只是为了完成登出，而不是还要
		        //顺带做其他业务操作
		        return false;
			} else {
				//如果是其他请求，则继续过滤链操作
				log.trace("Ignoring URI " + request.getRequestURI());
			}
			//如果是非登出请求，则返回true,表示继续执行过滤链
			return true;
		}
		
		
		
	}

	public org.jweb.common.shiro.handler.SingleSignOutHandler getCacheHandler() {
		return cacheHandler;
	}

	public void setCacheHandler(
			org.jweb.common.shiro.handler.SingleSignOutHandler cacheHandler) {
		this.cacheHandler = cacheHandler;
	}



	
	
}
