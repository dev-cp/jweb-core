package org.jweb.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加解密工具
 * @author wupan
 *
 */
public class AESUtil {

	public static String encrypt(byte[] sSrc, byte[] sKey,byte[] sIv) throws Exception {    
		if (sKey == null) {    
			System.out.print("Key为空null");    
			return null;    
		}    
		
		byte[] raw = sKey;   
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");    
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"<strong>算法</strong>/模式/补码方式"    
//		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("utf-8"));//使用CBC模式，需要一个向量iv，可增加<strong><strong><strong>加密</strong></strong></strong><strong>算法</strong>的强度
		IvParameterSpec iv = new IvParameterSpec(sIv);//使用CBC模式，需要一个向量iv，可增加<strong><strong><strong>加密</strong></strong></strong><strong>算法</strong>的强度
		
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);    
		byte[] encrypted = cipher.doFinal(sSrc);    
		
		return Base64.encode(encrypted);//此处使用BAES64做转码功能，同时能起到2次<strong><strong><strong>加密</strong></strong></strong>的作用。    
	}    
	
		    
	
	// <strong>解密</strong>    
	public static String decrypt(String sSrc, byte[] sKey,byte[] sIv) throws Exception {    
		try {    
			// 判断Key是否正确    
			if (sKey == null) {    
				System.out.print("Key为空null");    
				return null;    
			}    
			  
			byte[] raw = sKey;    
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");    
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");    
//			IvParameterSpec iv = new IvParameterSpec("0102030405060708" .getBytes());    
			IvParameterSpec iv = new IvParameterSpec(sIv);    
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);    
			byte[] encrypted1 = Base64.decode(sSrc);//先用bAES64<strong>解密</strong>    
			try {    
				byte[] original = cipher.doFinal(encrypted1);    
				String originalString = new String(original);    
				return originalString;    
			} catch (Exception e) {    
				System.out.println(e.toString());    
				return null;    
			}    
		} catch (Exception ex) {    
			System.out.println(ex.toString());    
			return null;    
		}    
	}    
    


}
