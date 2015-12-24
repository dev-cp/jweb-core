package cn.keepme.ep.util;

import java.util.Locale;
import java.util.Random;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.jweb.core.bean.ReplyDataMode;
import org.jweb.core.util.ReplyCodeResourceUtil;
import org.jweb.core.util.ResourceUtil;

import cn.keepme.ep.common.service.SMSServiceI;
import cn.keepme.ep.common.util.I18NUtil;


public class MemberUtil {

	public static ReplyDataMode sendSMSValidCode(String mphone,Locale locale,SMSServiceI smsService,EhCacheManager ehcacheCacheManager){
		ReplyDataMode j = new ReplyDataMode();
		boolean smsSendOpen = Boolean.valueOf(ResourceUtil.getConfigByName("sms_send_open"));
		//随机生成验证码
		String code = "";
		if(smsSendOpen){//如果开启了短信发送，才生成随机码，否则随机码是123456
			
			Random random = new Random();
			int x = random.nextInt(899999) + 100000;
		//	code = RandomUtil.generateRandom(6);
			code = x + "";
		} else {
			code = "123456";
		}
		/*SMSCodeVO codeVO = new SMSCodeVO();
		codeVO.setMphone(mphone);
		codeVO.setSmsCode(code);*/
		//缓存到服务器
		String defaultEhcacheName = ResourceUtil.getConfigByName("defaultEhcacheName");
		Cache cache = ehcacheCacheManager.getCache(defaultEhcacheName);
		if(cache.get(mphone) != null){
			String waitingStatus = ReplyCodeResourceUtil.getProperties("send_sms_interval_waiting");//短信发送间隔等待状态，一般情况下第二次发送短信会有一定等待时间
			j.setStatusCode(waitingStatus);
			j.setSuccess(false);
		} else {
			cache.put(mphone, code);
			//发送短信验证码
			String content = I18NUtil.get("reset.login.pwd.sms", locale, new Object[]{code});
			if(smsSendOpen){//如果开启了短信发送，才真正发送短信
				if(smsService.sendSMS(mphone, content)){
					j.setSuccess(true);
				} else {
					j.setSuccess(false);
				}
			} else {
				j.setSuccess(true);
			}
		}
		
		
		return j;
	}
	
	public static boolean validSMSCode(String mphone,String smsCode,EhCacheManager ehcacheCacheManager){
		String defaultEhcacheName = ResourceUtil.getConfigByName("defaultEhcacheName");
		//SMSCodeVO smsCodeVO = (SMSCodeVO)ehcacheCacheManager.getCache(defaultEhcacheName).get(mphone);
		String code = ehcacheCacheManager.getCache(defaultEhcacheName).get(mphone).toString();
		/**验证数据*/
		if(code == null){
			return false;
		}
		if(!code.equalsIgnoreCase(smsCode)){
			return false;
		}
		return true;
	}
}
