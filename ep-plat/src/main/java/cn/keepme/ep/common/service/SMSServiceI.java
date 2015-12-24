package cn.keepme.ep.common.service;

import org.jweb.core.service.CommonService;

/**
 * 短信接口
 * @author Administrator
 *
 */
public interface SMSServiceI extends CommonService{

	/**
	 * 发送短信
	 * @param mphone
	 * @param content
	 * @return 成功返回true
	 */
	public boolean sendSMS(String mphone,String content);
}
