package org.jweb.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.jweb.core.bean.ReplyDataMode;
import org.jweb.core.util.ReplyCodeResourceUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 用于捕获异常的过滤器
 * @author wupan
 *
 */
public class ExceptionFilter implements Filter{

	private final Log log = LogFactory.getLog(getClass());
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String url = req.getRequestURL().toString();
		String uri = req.getRequestURI();
		
		log.debug("excute doFilter method.  current path is " + url);
		
		try{
			chain.doFilter(request, response);
		} catch(Exception e){
			
			Throwable causeThrowable = this.getRootCauseException(e);
			
			if(causeThrowable instanceof InvalidSessionException){
				//如果是UnknownSessionException异常，则移除当前cookie，因为shiro在未能提取出session时，会抛出该异常，并且
				//不会执行session的valid,也就不会在执行valid方法中触发onInvalid方法，去移除当前cookie，所以，这里针对
				//UnknownSessionException异常进行cookie清除
				if(causeThrowable instanceof UnknownSessionException){
					//				req.getSession().invalidate();
					
					//将当前的cookie失效
			        Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
			        cookie.setHttpOnly(true); //more secure, protects against XSS attacks
			        cookie.removeFrom(req, resp);
			        log.trace("Removed '"+cookie.getName()+"' cookie by setting maxAge=0");
			        
				}
				
				//重定向回来
				resp.sendRedirect(uri);
			} else if((causeThrowable instanceof AuthorizationException)||
					(causeThrowable instanceof UnauthorizedException)){
				ReplyDataMode r = new ReplyDataMode();
				r.setStatusCode(ReplyCodeResourceUtil.getProperties("lose_permit"));
				r.setData("lose permit");
				
				resp.getWriter().write(JSONObject.toJSONString(r));
			}
			
			
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 递归获取当前异常的最原始的引起异常
	 * @param t
	 * @return
	 */
	private Throwable getRootCauseException(Throwable t){
		Throwable c = t.getCause();
		if(c != null){
			Throwable cc = getRootCauseException(c);
			return cc;
		} else {
			return t;
		}
	}
}
