/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jweb.common.shiro.handler;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

/**
 * 该类是处理单点登录登出业务的工具类，包括提供当登录时，记录登录session到全局访问区，
 * 当登出时，从全局访问区移除session并销毁session的功能
 * 
 * 该类绝大部分功能都是和org.jasig.cas.client.session.SingleSignOutHandler一样的，唯一
 * 不同的是，该类提供了对cache的支持，允许登记在案的session保存到cache中，这样就实现
 * 了session管理和系统应用的分离，在服务器集群情况下，当集群中某个节点服务器挂掉，其他
 * 节点服务器能够从cache（集群）服务器上获取相关session，并接着完成业务工作
 * 
 * Performs CAS single sign-out operations in an API-agnostic fashion.
 *
 * @author wupan
 *
 */
public final class SingleSignOutHandler {

    /** Logger instance */
    private final Log log = LogFactory.getLog(getClass());

    /** Mapping of token IDs and session IDs to HTTP sessions */
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();
    
    /** The name of the artifact parameter.  This is used to capture the session identifier. */
    private String artifactParameterName = "ticket";

    /** Parameter name that stores logout request */
    private String logoutParameterName = "logoutRequest";
    
    private CacheManager manager;

    private String cacheName;

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        this.sessionMappingStorage = storage;
    }

    public SessionMappingStorage getSessionMappingStorage() {
        return this.sessionMappingStorage;
    }

    /**
     * @param name Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * @param name Name of parameter containing CAS logout request message.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    
    
    public SingleSignOutHandler(){
    	
    }
    
   
    
    /**
     * Initializes the component for use.
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
        CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannote be null."); 
        
//        if(manager != null && cacheName != null && !"".equals(cacheName)){
//    		cache = manager.getCache(cacheName);
//    	}
    }
    
    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request HTTP reqest.
     *
     * @return True if request contains authentication token, false otherwise.
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
    }

    /**
     * Determines whether the given request is a CAS logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request) &&
            CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }

    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     * 
     * @param request HTTP request containing an authentication token.
     */
    public void recordSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);

        final String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        if (log.isDebugEnabled()) {
            log.debug("Recording session for token " + token);
        }

        //登录成功，按登录票-sesssion id 键值对将session存放到cache
        //登出时，根据登录票从cache中取出session id,再根据sessionId取出shiro缓存到cache
        //中的session,让session失效
        this.manager.getCache(cacheName).put(token, session.getId());
        
        
        try {
            this.sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid.  Nothing we can do!
        }
        
        sessionMappingStorage.addSessionById(token, session);
    }
   
    /**
     * Destroys the current HTTP session for the given CAS logout request.
     *
     * @param request HTTP request containing a CAS logout message.
     */
    public void destroySession(final HttpServletRequest request) {
        final String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
        if (log.isTraceEnabled()) {
            log.trace ("Logout request:\n" + logoutMessage);
        }
        
        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        if (CommonUtils.isNotBlank(token)) {
//            final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);
        	//从cache中获取sessionId
            
        		Object sessionIdObj = this.manager.getCache(cacheName).get(token);
        		Object sessionObj = this.manager.getCache(cacheName).get(sessionIdObj.toString());
        		
        		if(sessionObj instanceof Session){
        			Session shiroSession = (Session)sessionObj;
        			
        			//使待登出的session停止，然后重新放回cache,这样shiro再从cache取回该session时，才能知道当前
        			//session已经停止，从而拒绝访问，完成登出效果
        			//采用session停止(stop())或超时(setTimeout(0))的方式，shiro都会向外面抛出异常，通过异常来中断程序访问
        			//是否可以用清理session参数的方式来完成登出呢？
        			shiroSession.stop();
//        			shiroSession.setTimeout(0);
//        			shiroSession.setAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY, false);//这种方式，用户是被rememberMe的，再次登录时，将无法到达登录页面
        			
        			this.manager.getCache(cacheName).put(sessionIdObj.toString(), shiroSession);
        			
        			//同时将token在缓存中对应的session信息移除
        			this.manager.getCache(cacheName).remove(token);
        		}
        	
        	
//            if (session != null) {
//                String sessionID = session.getId();
//
//                if (log.isDebugEnabled()) {
//                    log.debug ("Invalidating session [" + sessionID + "] for token [" + token + "]");
//                }
//                try {
//                    session.invalidate();
//                } catch (final IllegalStateException e) {
//                    log.debug("Error invalidating session.", e);
//                }
//            }
        }
    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }

    
    
	public CacheManager getManager() {
		return manager;
	}

	public void setManager(CacheManager manager) {
		this.manager = manager;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
    
    
}
