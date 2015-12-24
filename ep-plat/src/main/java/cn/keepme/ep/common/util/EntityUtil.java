package cn.keepme.ep.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jweb.core.util.StringUtil;

public class EntityUtil {

	/**
	 * 将实体集合中对象的id字段作为键，封装到map
	 * @param list
	 * @return
	 */
	public static Map<String,Object> list2map(Collection<? extends Object> list){
		Map<String,Object> map = new HashMap<String, Object>();
		for(Object o : list){
			try {
				Object idObj = PropertyUtils.getSimpleProperty(o, "id");
				if(idObj != null && StringUtil.isNotEmpty(idObj.toString())){
					map.put(idObj.toString(), o);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e){
				
			}
			
		}
		
		return map;
	}
	
	/**
	 * 指定实体中某个字段值作为键，封装到map
	 * @param list
	 * @param fieldName
	 * @return
	 */
	public static Map<String,Object> list2map(Collection<? extends Object> list,String fieldName){
		Map<String,Object> map = new HashMap<String, Object>();
		for(Object o : list){
			try {
				Object idObj = PropertyUtils.getSimpleProperty(o, fieldName);
				if(idObj != null && StringUtil.isNotEmpty(idObj.toString())){
					map.put(idObj.toString(), o);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e){
				
			}
			
		}
		
		return map;
	}
}
