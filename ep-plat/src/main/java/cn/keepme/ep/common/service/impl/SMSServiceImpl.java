package cn.keepme.ep.common.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.jweb.core.service.impl.CommonServiceImpl;
import org.jweb.core.util.ResourceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.keepme.ep.common.service.SMSServiceI;
import cn.keepme.ep.common.util.SMSUtil;

@Service("smsService")
@Transactional
public class SMSServiceImpl extends CommonServiceImpl implements SMSServiceI{
	

	@Override
	public boolean sendSMS(String mobile, String con) {
		System.out.println("当前号码：" + mobile);
		String username = ResourceUtil.getConfigByName("sms_send_plat_app_id");
		String password = ResourceUtil.getConfigByName("sms_send_plat_app_pwd");
		String companyName = ResourceUtil.getConfigByName("current_sys_company_name");
		
		String key = ResourceUtil.getConfigByName("current_sys_company_key");
		
		/*
		java短信发送示例
		注意：
		1.此接口支持通知类和注册类短信，谢绝一切营销类推广
		2.短信内容总字数超过65个字则扣除2条短信的余额

		短信内容收不到由如下原因引起：
		1.短信信号不好
		2.手机欠费
		3.一个手机号在一小时内收到超过8封信
		4.发送内容不符合接口规定
		5.手机装有手机卫士之类拒收或在被拦截名单中
		6.手机死机
		*/
		
		/*-------------请填写以下信息------------*/
		String utf8Con = "";
		String utf8CompanyName = "";
		try {
			utf8Con = new String(con.getBytes("UTF-8"),"UTF-8");
			utf8CompanyName = new String(companyName.getBytes("UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String body = java.net.URLEncoder.encode(utf8Con);//测试时尽量用祝福语
		String suffix = java.net.URLEncoder.encode(utf8CompanyName);//在3-8个字内
		/*-------------请填写以下信息------------*/
		
		String content = body + java.net.URLEncoder.encode("【") + suffix + java.net.URLEncoder.encode("】");//get content
//		SMSUtil test = new SMSUtil();
		
		long time = 0;
		try {
			time = SMSUtil.GetGMTTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//获取格林尼治时间
		String authkey = SMSUtil.getMD5Str(username+time+ SMSUtil.getMD5Str(password)+key);//生成authkey
		String para = "username="+username+"&time="+time+"&content="+content+"&mobile="+mobile+"&authkey="+authkey;//生成参数
		
		//post逻辑
		String respost = SMSUtil.sendPost("http://sms.edmcn.cn/api/cm/trigger_mobile.php", para);
		
		switch (Integer.parseInt(respost)){
		case 1: 
			System.out.println("成功");
			return true;
		case 2:
			System.out.println("参数不正确");
			break;
		case 3:
			System.out.print("密钥验证失败");
			break;
		case 4:
			System.out.print("用户名或密码错误");
			break;
		case 5:
			System.out.print("服务器内部失败");
			break;
		case 6:
			System.out.print("余额不足");
			break;
		case 7:
			System.out.print("内容不符合格式");
			break;
		case 8:
			System.out.print("频率超限");
			break;
		case 9:
			System.out.print("接口超时");
			break;		
		case 10:
			System.out.print("后缀签名长度超过限制");
			break;
		}
		return false;
		
	}

	
}
