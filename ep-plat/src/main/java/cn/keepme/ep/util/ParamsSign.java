package cn.keepme.ep.util;

import org.jweb.core.util.PasswordUtil;



/**
 * 签名认证 加密/解密
 * @author cp
 *
 */
public class ParamsSign {

	/**
	 * 加密，生成 用户唯一标示
	 * @param payLaunch
	 * @param user
	 * @return
	 */
	public static String encode(String plaintext, String password) {
		String ciphertext = "";

		try {
			byte[] salt = PasswordUtil.getStaticSalt();
			ciphertext = PasswordUtil.encrypt(plaintext, password, salt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ciphertext;
	}
	
	/**
	 * 解密，获取 用户唯一标示
	 * @param payLaunch
	 * @param user
	 * @return
	 */
	public static String decode(String ciphertext, String password){
		String plaintext1 = "";
		try {
			byte[] salt = PasswordUtil.getStaticSalt();
			plaintext1 = PasswordUtil.decrypt(ciphertext, password, salt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plaintext1;
	}

	public static void main(String[] args) {
//		String id = "2";
//		String p = "zhangsan@163.com";
//		String s = encode(p,id);
//		System.out.println(s);
//		String s2 = decode(s,id);
//		System.out.println(s2);
		
		System.out.println(PublicParams.questionType.get("duoxuan"));
		
	}
	
}
