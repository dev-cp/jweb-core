package cn.keepme.ep.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SMSUtil {
 /**
  * 向指定 URL 发送POST方法的请求
  * @param url
  *            发送请求的 URL
  * @param param
  *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
  * @return 所代表远程资源的响应结果
  */
 public static String sendPost(String url, String param) {
		
     PrintWriter out = null;
     BufferedReader in = null;
     String result = "";
     try {
         URL realUrl = new URL(url);
         // 打开和URL之间的连接
         URLConnection conn = realUrl.openConnection();
         // 设置通用的请求属性
         conn.setRequestProperty("accept", "*/*");
         conn.setRequestProperty("connection", "Keep-Alive");
         conn.setRequestProperty("user-agent",
                 "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
         // 发送POST请求必须设置如下两行
         conn.setDoOutput(true);
         conn.setDoInput(true);
         // 获取URLConnection对象对应的输出流
         out = new PrintWriter(conn.getOutputStream());
         // 发送请求参数
         out.print(param);
         // flush输出流的缓冲
         out.flush();
         // 定义BufferedReader输入流来读取URL的响应
         in = new BufferedReader(
                 new InputStreamReader(conn.getInputStream()));
         String line;
         while ((line = in.readLine()) != null) {
             result += line;
         }
     } catch (Exception e) {
         System.out.println("发送 POST 请求出现异常！"+e);
         e.printStackTrace();
     }
     //使用finally块来关闭输出流、输入流
     finally{
         try{
             if(out!=null){
                 out.close();
             }
             if(in!=null){
                 in.close();
             }
         }
         catch(IOException ex){
             ex.printStackTrace();
         }
     }	
     return result;
 } 
 
 /** 
  * MD5 加密 
  */  
 public static String getMD5Str(String str) {  
     MessageDigest messageDigest = null;  

     try {  
         messageDigest = MessageDigest.getInstance("MD5");  

         messageDigest.reset();  

         messageDigest.update(str.getBytes());  
     } catch (NoSuchAlgorithmException e) {  
         System.out.println("NoSuchAlgorithmException caught!");  
         System.exit(-1);  
     }  

     byte[] byteArray = messageDigest.digest();  

     StringBuffer md5StrBuff = new StringBuffer();  

     for (int i = 0; i < byteArray.length; i++) {              
         if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
             md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
         else  
             md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
     }  

     return md5StrBuff.toString();  
 } 
 
 public static long GetGMTTime() throws ParseException{
     final int msInMin = 60000;  
     final int minInHr = 60;  
     Date date = new Date();  
     int Hours, Minutes;  
     DateFormat dateFormat =new SimpleDateFormat("yyyyMMddHHmmss");// DateFormat.getDateTimeInstance(DateFormat.LONG,  DateFormat.LONG);  
     TimeZone zone = dateFormat.getTimeZone();  
     System.out.println("IST Time: " + dateFormat.format(date));  
     Minutes = zone.getOffset(date.getTime()) / msInMin;  
     Hours = Minutes / minInHr;  
     System.out.println("GMT Time" + (Hours >= 0 ? "+" : "") + Hours + ":" + Minutes);  
     zone = zone.getTimeZone("GMT Time" + (Hours >= 0 ? "+" : "") + Hours + ":" + Minutes);  
     dateFormat.setTimeZone(zone);  
       
     String  time = dateFormat.format(date);
     System.out.println("GMT: " + time);
     
     long Seconds = 0;
		try {
			 SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
			Seconds = simpledateformat.parse(time).getTime()/1000;
			 System.out.println(Seconds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//毫秒


    return Seconds;
 }

	public static void main(String[] args) throws Exception{
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
		String username = "";//用户名
		String password = "";//密码
		String key = "";//key
		String mobile = "";//电话号码
		String body = java.net.URLEncoder.encode("祝你生日快乐");//测试时尽量用祝福语
		String suffix = java.net.URLEncoder.encode("您的公司简称");//在3-8个字内
		/*-------------请填写以下信息------------*/
		
		String content = body + java.net.URLEncoder.encode("【") + suffix + java.net.URLEncoder.encode("】");//get content
		SMSUtil test = new SMSUtil();
		
		long time = GetGMTTime();//获取格林尼治时间
		String authkey = test.getMD5Str(username+time+ test.getMD5Str(password)+key);//生成authkey
		String para = "username="+username+"&time="+time+"&content="+content+"&mobile="+mobile+"&authkey="+authkey;//生成参数
		
		//post逻辑
		String respost = test.sendPost("http://sms.edmcn.cn/api/cm/trigger_mobile.php", para);
		System.out.println(respost);
		switch (Integer.parseInt(respost)){
		case 1: 
			System.out.println("成功");
			break;
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
	}
}
