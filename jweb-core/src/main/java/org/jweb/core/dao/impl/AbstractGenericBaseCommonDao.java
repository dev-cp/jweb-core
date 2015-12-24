package org.jweb.core.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;

/**
 * 抽象基类通用dao，用于提供一些通用方法
 * @author wupan
 *
 */
public abstract class AbstractGenericBaseCommonDao{

	/**
	 * 将example对象的属性值以键值对集合形式返回，其中键名是example对象属性名
	 * @param example
	 * @return
	 */
	protected Map<String ,Object> getJavaBeanNameValuePairMap(Object example){
		Map<String,Object> nvps = new LinkedHashMap<String, Object>();
		
		//通过反射判断对象字段是否有加密注解，如果有，就进行加密
				Field[] fields = example.getClass().getDeclaredFields();
				
				for(Field field : fields){
						String fieldName = field.getName();
						String firstChar = fieldName.substring(0, 1);
						firstChar = firstChar.toUpperCase();
						
						String getMethodName = "get" + firstChar + fieldName.substring(1);
						try {
							Method getMethod = example.getClass().getDeclaredMethod(getMethodName);
							Object valueObj = getMethod.invoke(example);
							
							//只将非空字段添加到集合
							if(valueObj != null){
								
								nvps.put(fieldName, valueObj);
								
							}
							
							
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
		
		return nvps;
	}
	
	/**
	 * 将example对象的属性值以键值对集合形式返回，其中键名是example对象属性对应数据库表字段名，
	 * 方法是根据javax.persistence.Column表字段映射注解获取的；
	 * 如果javaBean对象的属性未与数据库表字段做映射，则返回空
	 * @param example
	 * @return
	 */
	protected Map<String ,Object> getTableNameValuePairMap(Object example){
		Map<String,Object> nvps = new LinkedHashMap<String, Object>();
		
		//通过反射判断对象字段是否有加密注解，如果有，就进行加密
				Field[] fields = example.getClass().getDeclaredFields();
				
				for(Field field : fields){
//					RequiresEncry requiresEncry = field.getAnnotation(RequiresEncry.class);
//					if(requiresEncry != null){
						String fieldName = field.getName();
						String firstChar = fieldName.substring(0, 1);
						firstChar = firstChar.toUpperCase();
						
						String getMethodName = "get" + firstChar + fieldName.substring(1);
						
						
						
						try {
							Method getMethod = example.getClass().getDeclaredMethod(getMethodName);
							Object valueObj = getMethod.invoke(example);
							
							//获取get方法上的 字段映射注解
							Column column = getMethod.getAnnotation(Column.class);
							if(column != null){
								String columnName = column.name();
								if(columnName != null && !"".equals(columnName)){
									
									//只将非空字段添加到集合
									if(valueObj != null){
										
										nvps.put(columnName, valueObj);
										
									}
								}
							}
							
							
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
		return nvps;
	}
	
	
	
	
}
