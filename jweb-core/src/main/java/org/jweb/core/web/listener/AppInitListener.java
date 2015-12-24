package org.jweb.core.web.listener;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.beanutils.ConvertUtils;
import org.jweb.core.converter.UtilDateConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 该应用程序初始化监听器，主要用于初始化应用程序相关功能组件，如果有需要在你启动
 * 应用程序时就要初始化的操作，可以在该监听器中进行初始化
 * @author wupan
 *
 */
public class AppInitListener implements ServletContextListener {

	private static ApplicationContext ctx = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		 initConverter();
		 ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}
	
	/**
	 * 初始化转换器
	 */
	private void initConverter(){
		ConvertUtils.register(new UtilDateConverter(), Date.class);
	}

}
