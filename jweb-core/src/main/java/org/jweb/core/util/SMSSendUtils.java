package org.jweb.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 该工具方法是按照掌讯科技公司的短信接口规范编写的，采用掌讯科技公司的短息发送服务
 * 掌讯科技官网：http://www.cdzxkj.com/Item/list.asp?id=1453
 * @author Administrator
 *
 */
public class SMSSendUtils {
    
    private final static String SEND_MSG_URL = "http://sdk.cdzxkj.com/BatchSend.aspx?";
    
    
    /**
     *  发送短信
     * 				-1、帐号未注册；
					-2、其他错误；
					-3、密码错误；
					-4、手机号格式不对；
					-5、余额不足；
					-6、定时发送时间不是有效的时间格式；
					-7、禁止10小时以内向同一手机号发送相同短信
     * @param appId 在掌讯科技注册的账号
     * @param appPwd 在掌讯科技注册的密码
     * @param phoneNumber 待发送短信的目标手机号
     * @param content 短信内容
     * @param company 公司名称
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public static int sendMsg(String appId,String appPwd,String phoneNumber,String content,String company) throws UnsupportedEncodingException,
                                                 MalformedURLException {
        URL url;
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++)
            result += random.nextInt(10);
        content = content +  "。【"+company+"】";
        String send_content = URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");// 发送内容
        url = new URL(SEND_MSG_URL + "CorpID="
                      + appId
                      + "&Pwd="
                      + appPwd
                      + "&Mobile="
                      + phoneNumber
                      + "&Content="
                      + send_content);
        BufferedReader in;
        int inputLine;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputLine = new Integer(in.readLine()).intValue();
           
        }
        catch (Exception e) {
            System.out.println("网络异常,发送短信失败！");
            inputLine = -11;
            
        }
        return inputLine;
    }
}
