package org.jweb.core.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author  张代浩
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 *  当容器启动的时候会自动把应用的上下文注入到这里，就可以直接获取bean了
	 */
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
