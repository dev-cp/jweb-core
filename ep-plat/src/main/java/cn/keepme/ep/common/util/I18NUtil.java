package cn.keepme.ep.common.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化资源工具类
 * @author Administrator
 *
 */
public class I18NUtil {
	private static Locale currentLocale;
	private static ResourceBundle bundle;
	
	public static String get(String key,Object... params){
		currentLocale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("i18n/resource", currentLocale);
		String value = bundle.getString(key);
		value = MessageFormat.format(value, params); 
		return value;
	}
	
	public static String get(String key,Locale locale,Object... params){
		currentLocale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("i18n/resource", locale);
		String value = bundle.getString(key);
		value = MessageFormat.format(value, params); 
		return value;
	}
}
