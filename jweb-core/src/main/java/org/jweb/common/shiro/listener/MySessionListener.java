package org.jweb.common.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.eis.SessionDAO;

/**
 * shiro session事件监听器，这里主要实现当cache中session停止或者失效时，对session的善后工作
 * 
 * 当用户发起登出时，sso系统会通知shiro进行登出处理，实际上shiro端会在shiro登出过滤器中捕获
 * 到登出请求，并从cache中提取出待登出的session，调用session.stop()方法，session再次更新到
 * cache中，shiro定时监测session任务会从cache中提取出session，对session进行验证，当验证到
 * session是stop状态，会触发session监听器，即这里的监听器，我们将会在监听器中对cache中停止
 * 的session进行清理。
 * @author wupan
 *
 */
public class MySessionListener extends SessionListenerAdapter{

	private SessionDAO sessionDAO;
	
	@Override
	public void onStop(Session session) {
		
		//删除session,这里实际会从cache中去删除session
		sessionDAO.delete(session);
		
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}

	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	
}
