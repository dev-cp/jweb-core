package org.jweb.core.cipher.spring.placeholder;

import org.jweb.core.util.DESUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 配置文件加密参数解密
 * @author wupan
 *
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

	
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncryptProp(propertyValue)){
			String encryValue = propertyValue.substring(4, propertyValue.length() - 1);
			String decryptValue = DESUtil.getDecryptString(encryValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
		
	}

	private boolean isEncryptProp(String propertyValue){
		String reg = "ENC\\(*\\)";
		if(propertyValue.startsWith("ENC(") && propertyValue.endsWith(")")){
			return true;
		}
		
//		for(String s : encryptPropNames){
//			if(s.equals(propertyName)){
//				return true;
//			}
//		}
		return false;
	}
	
	public static void main(String[] args) {
		String p = "ENC(W1BJSjx6+1O1z3ArmojmaQG+r80ty3zX)";
		if(p.startsWith("ENC(") && p.endsWith(")")){
			System.out.println("ok");
		}
	}
}
