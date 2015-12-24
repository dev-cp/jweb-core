package org.jweb.core.util;

import java.util.ResourceBundle;

/**
 * 回复码文档操作工具类
 * @author wupan
 *
 */
public class ReplyCodeResourceUtil {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("code/replyCode");

	/**
	 * 根据键获取值
	 * @param key
	 * @return
	 */
	public static String getProperties(String key){
		
		String str = bundle.getString(key);
		return str;
	}
}
