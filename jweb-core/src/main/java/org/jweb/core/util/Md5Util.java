package org.jweb.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Md5Util {
	private static Log log = LogFactory.getLog(Md5Util.class);
	
	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
		}
		byte[] byteArray = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				sb.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return sb.toString();
	}
}