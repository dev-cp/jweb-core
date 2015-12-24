package org.jweb.core.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jweb.core.bean.annotation.Mapping;

/**
 * 所有vo的父类vo，除了为子类vo提供公共属性或方法外，还用于在EncryptionInterceptor拦截器
 * 中进行加密处理时的类型标志，只有是BaseVO类型的对象，才会进一步根据@RequiresEncry注解
 * 进行加密
 * @author wupan
 *
 */
public abstract class BaseVO {

	private Map<String,Mapping> mappingInfoMap = new HashMap<String, Mapping>();
	
	/**
	 * 将实体类对象转换到当前vo对象
	 * @param entity
	 * @return
	 */
	public Object copyEntity(Object entity){
		//提取字段映射关系
		Map<String,String> coverMap = new HashMap<String, String>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields){
			Mapping fieldMapping = f.getAnnotation(Mapping.class);
			if(fieldMapping == null){
				continue;
			}
			
			String fieldName = f.getName();
			String mappingName = fieldMapping.value();
			
			if(mappingName == null || "".equals(mappingName)){
				coverMap.put(fieldName, fieldName);
			} else {
				coverMap.put(fieldName, mappingName);
			}
			
			mappingInfoMap.put(fieldName, fieldMapping);
		}
		
		return this.copyValue(entity, coverMap);
		
	}
	
	/**
	 * 根据vo与entity字段的映射表，将目标实体对象中的值复制到当前vo对象中
	 * @param entity
	 * @param coverMap
	 * @return
	 */
	protected Object copyValue(Object entity,Map<String,String> coverMap){
		try{
//			Map<String,Object> mapbean = BeanUtils.describe(entity);
			Map<String,Object> mapbean = this.getPropertyMap(entity);
			Field[] fields = this.getClass().getDeclaredFields();
			SimpleDateFormat dateFormat = new SimpleDateFormat();
			
			for(Field field : fields){
				//通过修改可访问性，获取该字段对应当前对象的字段值
				field.setAccessible(true);
				
				
				String entityFieldName = coverMap.get(field.getName());
				Object entityValue1 = null;
				Object entityValue = null;
				if(entityFieldName != null && !"".equals(entityFieldName)){
					//递归查询映射的实体字段名所对应的值，注意，这里的entityFieldName可能是a.b.c的形式
					if(entityFieldName.indexOf(".") > -1){
						String[] entityFieldNames = entityFieldName.split("\\.");
						
						entityValue1 = mapbean.get(entityFieldNames[0]);
						entityValue = recursiveGetEntityValue(entityValue1,entityFieldNames,1);
					} else {
						entityValue = mapbean.get(entityFieldName);
					}
				}
				//如果该字段对应实体中字段的值为空，空值不必赋值，则结束本次循环
				if(entityValue == null ){
					continue;
				}
				
				/**分三种情况讨论，第一种是field是String类型，第二种是field是集合类型，第三种是field是BaseVO类型**/
				Class fieldTypeClazz = field.getType();
				/**第一种情况，field是String类型*/
				if(fieldTypeClazz.isAssignableFrom(String.class)){
					//将form对象字段转换成entity类型相应字段对应的类型值
					//判断是否是时间类型
					if(entityValue instanceof Date){
						Mapping mapping = mappingInfoMap.get(field.getName());
						if(mapping != null){
							String dateFormatStr = mapping.dateFormat();
							dateFormat.applyPattern(dateFormatStr);
							String dateStr = dateFormat.format(((Date)entityValue));
							field.set(this, dateStr);
						}
					} else {
						String converedValue = ConvertUtils.convert(entityValue);
						field.set(this, converedValue);
						
					}
					continue;
				}
				
				
				
				/**第二种情况，field是集合类型,同时也判断实体类中相应字段是否是集合类型*/
				if(List.class.isAssignableFrom(fieldTypeClazz)
						|| Set.class.isAssignableFrom(fieldTypeClazz)){
					
					//判断相应的实体类型是否是集合类型，如果不是，则结束本次fields的循环
					if(!(entityValue instanceof Set) 
							&& !(entityValue instanceof List )){
						continue;
					}
					
					//判断当前字段是list集合还是set集合，然后分别给该字段设值，设的值就是从entity转换成vo后封装的集合数据
//					Collection voFieldValueCol = null;
					
					Collection voFieldValueCol = (Collection)field.get(this);
					if(voFieldValueCol == null){
						if(List.class.isAssignableFrom(fieldTypeClazz)){
							voFieldValueCol = new ArrayList();
						} else if(Set.class.isAssignableFrom(fieldTypeClazz)){
							voFieldValueCol = new LinkedHashSet();
						}
						
						//再在初始化后继续判断一哈是否为空
						if(voFieldValueCol != null){
							field.set(this, voFieldValueCol);
						} else {
							throw new RuntimeException("can not instance vo object field to a collection object,because current entity object field that be mapped is a collection type");//当前被映射的实体对象的字段类型是集合类型，而我们又无法为vo对象实例化一个集合对象作为字段值
						}
					} 
					
					
					//计算获取field集合对应的泛型类型是什么
					Type fieldType = field.getGenericType();
					if(fieldType instanceof ParameterizedType){//如果是泛型
						 ParameterizedType pfieldType = (ParameterizedType)fieldType;
						 Class genericClazz = (Class)pfieldType.getActualTypeArguments()[0];//得到泛型中的第一个类模板
						 
						 if(entityValue instanceof Collection){
							 
							 Collection properCollection = (Collection)entityValue;//强转集合
							
							 for(Object subObj : properCollection){
								 Object paraObj = genericClazz.newInstance();
								 if(paraObj instanceof BaseVO){
									 BaseVO baseVo = (BaseVO)paraObj;
									 baseVo.copyEntity(subObj);
									 voFieldValueCol.add(baseVo);
								 } else {
									 System.out.println("vo对象中的属性对象推荐定义成BaseVO对象的子类对象！");
								 }
							 }
							
						 }
						 
					}
					
					continue;//处理完毕后，则结束本次循环
				} 
				
				/**第三种情况，field是BaseVO类型*/
				if(fieldTypeClazz.isAssignableFrom(BaseVO.class)){
					Object fieldValueObj = fieldTypeClazz.newInstance();
					if(fieldValueObj instanceof BaseVO){
						BaseVO bfieldValueObj = (BaseVO)fieldValueObj;
						bfieldValueObj.copyEntity(entityValue);//把实体中的值给复制过来
						field.set(this, bfieldValueObj);
						
						continue;//处理完毕后，则结束本次循环
					}
					
				}
				
			}
			
			
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
		return this;
	}
	
	private Object recursiveGetEntityValue(Object entity,String[] fields,int index){
		if(index == fields.length){
			return entity;
		}
		try {
			Object curFieldValue = PropertyUtils.getSimpleProperty(entity, fields[index]);
			
			if(index < fields.length){
				index++;
				return recursiveGetEntityValue(curFieldValue,fields,index);
			}
		} catch (Exception e) {
			return null;//如果从数据库中查询出异常，则忽略本次查询，返回空，因为可能数据库中不存在值。
		} 
		return null;
	}
	
	
	private Map<String,Object> getPropertyMap(Object entity){
		Map<String,Object> mapbean = new HashMap<String, Object>();
		
		Method[] methodArr = entity.getClass().getMethods();//获取所有自身或继承的公共方法
		for(Method m : methodArr){
			Class[] paramArr = m.getParameterTypes();
			String methodName = m.getName();
			if(methodName.startsWith("get") && methodName.length() > 3 && paramArr.length == 0){//找到设置器
				
				String firstChar = methodName.substring(3,4);
				String fieldName = "";
				if(methodName.length() > 4){
					fieldName = methodName.substring(4);
				}
				fieldName = (firstChar.toLowerCase()) + fieldName;
				
				try {
					Object value = m.invoke(entity, new Object[]{});
					mapbean.put(fieldName, value);
				} catch (Exception e) {
					//不做处理，如果没有get方法，说明没有提供
				} 
			}
		}
		
		
		
//		Field[] fields = entity.getClass().getDeclaredFields();
//		
//			for(Field field : fields){
//				String fieldName = field.getName();
//				
//					String firstChar = fieldName.substring(0, 1);
//					firstChar = firstChar.toUpperCase();
//					
//					String getMethodName = "get" + firstChar + fieldName.substring(1);
//					try{
//						Method getMethod = entity.getClass().getDeclaredMethod(getMethodName);
//						Object obj = getMethod.invoke(entity);
//						mapbean.put(fieldName, obj);
//					} catch(Exception e){
//						//不做处理，如果没有get方法，说明没有提供
//					}
//					
//			}
		
		
		return mapbean;
	}
	
}
