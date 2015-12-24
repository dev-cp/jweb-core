package org.jweb.core.util;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtil {
	private static Log log = LogFactory.getLog(BeanUtil.class);
	/**
	 * 用一个对象填充一个对�?
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static Object transfer(Object source, Object target) {
		BeanWrapper bws = new BeanWrapperImpl(source);
		BeanWrapper bwt = new BeanWrapperImpl(target);
		PropertyDescriptor[] pds = bws.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			String s = pd.getName();
			if (bws.isReadableProperty(s) && bwt.isWritableProperty(s))
				bwt.setPropertyValue(s, bws.getPropertyValue(s));
		}
		return target;
	}

	/**
	 * 根据�?��对象创建�?��新的对象
	 * 
	 * @param source
	 * @param targetClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object transfer(Object source, Class targetClass) {
		Object result = null;
		try {
			result = targetClass.newInstance();
			BeanWrapper bws = new BeanWrapperImpl(source);
			BeanWrapper bwt = new BeanWrapperImpl(result);
			//
			PropertyDescriptor[] pds = bws.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				String s = pd.getName();
				if (bws.isReadableProperty(s) && bwt.isWritableProperty(s)) {
					bwt.setPropertyValue(s, bws.getPropertyValue(s));
				}
			}
		} catch (InstantiationException e) {
			log.error(e, e);
		} catch (IllegalAccessException e) {
			log.error(e, e);
		}
		return result;
	}

	public static Object fillObjectWithMap(Object o, Map<String, Object> map) {
		BeanWrapper bw = new BeanWrapperImpl(o);
		PropertyDescriptor[] pds = bw.getPropertyDescriptors();
		for (int i = 0; i < pds.length; i++) {
			String s = pds[i].getName();
			if (bw.isWritableProperty(s) && map.containsKey(s)) {
				if (map.get(s) != null) {
					bw.setPropertyValue(s, map.get(s));
				}
			}
		}
		return o;
	}

	public static Object fillMapWithObject(Map<String, Object> map, Object o) {
		BeanWrapper bw = new BeanWrapperImpl(o);
		PropertyDescriptor[] pds = bw.getPropertyDescriptors();
		for (int i = 0; i < pds.length; i++) {
			String s = pds[i].getName();
			map.put(s, bw.getPropertyValue(s));
		}
		return o;
	}
}
