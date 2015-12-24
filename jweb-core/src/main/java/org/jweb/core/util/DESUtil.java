package org.jweb.core.util;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class DESUtil {

	private static Key key;
	private static String KEY_STR = "myKey";//这个值应该配置到操作系统的环境变量中
	
	static{
		try{
			
			//获取环境变量中配置的密码
			Map m = System.getenv();
            for ( Iterator it = m.keySet().iterator(); it.hasNext(); )
            {
                   String key = (String ) it.next();
                   if("APP_ENCRYPTION_PASSWORD".equals(key)){
                	   
                	   String v = (String )  (String )  m.get(key);
                	   if(StringUtil.isNotEmpty(v)){
                		   KEY_STR = (String )  (String )  m.get(key);
                	   }
                   }
                  
            }
            
           
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom(KEY_STR.getBytes()));
			key = generator.generateKey();
			generator = null;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getEncryptString(String str){
		try{
			byte[] strBytes = str.getBytes("UTF8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return Base64.encode(encryptStrBytes);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getDecryptString(String str){
		try{
			byte[] strBytes = Base64.decode(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptStrBytes = cipher.doFinal(strBytes);
			return new String(decryptStrBytes,"UTF8");
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		if(args == null || args.length < 1){
			System.out.println("请输入要加密的字符，用空格分隔。");
		} else {
			for(String arg : args){
				System.out.println(arg + ":" +getEncryptString(arg));
			}
		}
		
//		System.out.println("123456" + ":" +getEncryptString("123456"));
//		System.out.println(KEY_STR);
//		System.out.println(getDecryptString("QAHlVoUc49w="));
	}
}
