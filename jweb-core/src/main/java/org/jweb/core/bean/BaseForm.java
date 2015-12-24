package org.jweb.core.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jweb.core.bean.annotation.Mapping;
import org.jweb.core.query.annotation.Begin;
import org.jweb.core.query.annotation.End;
import org.jweb.core.util.StringUtil;

/**
 * 所有form的父类form，除了为子类form提供公共属性或方法外，还用于在DecryptionPreAdvice增强
 * 中进行解密处理时的类型标志，只有是BaseForm类型的对象，才会进一步根据@RequiresDecry注解
 * 进行解密
 * 
 * BaseForm能够允许它的子类
 * 1、将String类型字段值转到entity对象的基本类型字段上去（特殊类型需要根据commons-beanUtils转换器规范写转换器，并注册到ConvertUtils上去）
 * 2、将String类型字段值转到entity对象的非基本类型字段对象的字段上去，其中非基本类型字段对象是可以实例化的，否则抛异常。如@Mapping(goodsEntity.name)，将值设置到goodsEntity属性对象的name属性上去
 * 3、将String类型字段值转到entity对象的集合类型字段所存放的第一个成员对象的字段上去。如，@Mapping(picSet.name),将值设置到picSet集合类型属性对象所装的第一个成员对象的name属性上去。注意，集合类型属性必须带能够实例化的泛型，目前支持自动化实例Set和List集合
 * 
 * 4、将BaseForm类型字段转换成subentity对象，并将转换后的subentity对象设置到父entity对象的相应字段上
 * 5、将集合类型字段中存放的全部成员转换成subentity对象，并存放到entity相应的集合中去
 * @author wupan
 *
 */
public abstract class BaseForm {
	
	private Class entityClass;

	/**
	 * 子类必须实现form对象到entity对象的转换
	 * form对象是页面提交的数据封装模型，最终会传递到数据持久层，故form对象需要有个到
	 * entity转换的过程，此方法的定义就是基于这个原因
	 * 通常，为了数据安全起见，页面表单字段名不应该与数据库字段名一致，这样有助于数据库字段
	 * 名称不易被暴露给应用程序客户端
	 * @return
	 */
	public Object toEntity(){
		Mapping mapping = this.getClass().getAnnotation(Mapping.class);
		if(mapping == null){
			return null;
		}
		
		entityClass = mapping.entityClass();
		if(entityClass == null){
			return null;
		}
		
		Object entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		
		//提取字段映射关系
		Map<String,String> coverMap = this.getFormAndEntityFieldMap();
		
		return this.copyValue(entity, coverMap);
		
	}
	
	//PropertyUtils.getSimpleProperty(this, fieldName);
	
	/**
	 * 当前对象中字段值复制转换到entity对象中去，转换依据是coverMap中设置的form对象与entity对象的feild对应
	 * 关系表；注意，coverMap的key是form对象的字段名，value是entity的字段名；
	 * 此方法会根据entity中字段类型对form的字段值进行转换。form对象的所有字段都应该是String类型。
	 * 允许coverMap中的value采用“.”表示属性层级关系
	 * 
	 * 注意，传入的entity中，除了基本类型字段，集合字段或者自定义类型字段都应该初始化，目前本方法未提供自动初始化功能
	 * @param entity 待填充值得实体对象
	 * @param coverMap form对象鱼entity对象之间字段名对照表
	 * @return
	 */
	private Object copyValue(Object entity,Map<String,String> coverMap){

		Field[] fields = this.getClass().getDeclaredFields();
		try{
			for(Field field : fields){
				//通过修改可访问性，获取该字段对应当前对象的字段值
				field.setAccessible(true);
				
				Object curFieldValueObj = field.get(this);
				if(curFieldValueObj == null){//如果当前字段没有值，则不对该字段进行复制
					continue;
				}
				
				//判断当前值对象是String 还是list集合
				if(curFieldValueObj instanceof Collection){
					//再判断form的集合属性对象是否为空集合
					Collection curFieldValueCol = (Collection)curFieldValueObj;
					
					//先判断是否有字段映射信息，有才做复制，将子实体添加到目标实体entity的相关集合中
					String entityFieldName = coverMap.get(field.getName());
					if(entityFieldName != null){//如果有映射，则将Form中数据转到entity中集合对象中去
						
						//先初始化实体对象中的集合属性
						Object entityFieldObj = PropertyUtils.getSimpleProperty(entity, entityFieldName);//实体中的集合字段对象
						Class entityFieldClazz = PropertyUtils.getPropertyType(entity, entityFieldName);
						
						if(Collection.class.isAssignableFrom(entityFieldClazz)){//如果当前实体的字段是集合类型
							if(entityFieldObj == null){
//								Collection entityFieldCol = null;
								
								//分情况讨论实例化什么集合类型
								if(Set.class.isAssignableFrom(PropertyUtils.getPropertyType(entity, entityFieldName))){//如果是Set集合
									//实例化一个set集合
									entityFieldObj = new HashSet();
								} else if(List.class.isAssignableFrom(PropertyUtils.getPropertyType(entity, entityFieldName))){
									entityFieldObj = new ArrayList();
								}
								
								//再将实体存放到集合中
								if(entityFieldObj != null){
									PropertyUtils.setProperty(entity, entityFieldName, entityFieldObj);
								} else {
									throw new RuntimeException("current method copyValue() only provide List and Set Object to instanced");//当前setValue方法只提供List和Set集合对象实例化
								}
								
							} 
							
							Collection entityFieldCol = (Collection)entityFieldObj;
							for(Object obj : curFieldValueCol){
								if(obj instanceof BaseForm){//判断集合中是否存放的是BaseForm类型，所以form对象都应继承BaseForm是很重要的
									BaseForm baseForm = (BaseForm)obj;
									Object subEntity = baseForm.toEntity();//获取到转换后的子实体
									
									//判断子实体对象中是否存在父实体对象字段，如果存在，则把父实体对象关联到子实体中
									relateParentEntity2SubEntity(entity,subEntity);
									entityFieldCol.add(subEntity);
									
								}
							}
							
						} else {
							throw new RuntimeException("can not copy Collection Object cover to another Collection Object from BaseForm to Entity");//无法将BaseForm类型对象中的集合属性转换到实体类型对象的集合属性中
						}
						
						
						
					}
					
					//没有映射关系，则不做处理
					
				} else if(curFieldValueObj instanceof String){
					String curFieldValue = (String)curFieldValueObj;
					
					String entityFieldName = coverMap.get(field.getName());
					
					if(entityFieldName != null && !"".equals(entityFieldName)){
						//判断实体字段名中是否有“.”号，如果有，说明需要将指定值级联设置到entity对象的子属性字段上
						String[] fieldNames = entityFieldName.split("\\.");
//						setValue(entity,fieldNames,0,curFieldValue,this);
						List<String> tempFieldList = Arrays.asList(fieldNames);
						List<String> fieldList = new ArrayList<String>();
						fieldList.addAll(tempFieldList);//之所以要将tempFieldList翻一遍，是因为tempFieldList不支持remove()方法
						scanTargetToSetValue(entity,fieldList,curFieldValue);
					} else {//判断当前循环的baseform对象字段是否是有@Begin或@End注解
						Begin begin = field.getAnnotation(Begin.class);
						End end = field.getAnnotation(End.class);
						if(begin != null ){//如果注解不为空，则保证注解中值指定的实体对象被初始化
							String targetFieldName =begin.value();
							String[] targetFieldNameArr = targetFieldName.split("\\.");
							if(targetFieldNameArr != null && targetFieldNameArr.length > 0){
								recursionInitEntity(entity,targetFieldNameArr,targetFieldNameArr[0]);
							}
						}
						if(end != null ){//如果注解不为空，则保证注解中值指定的实体对象被初始化
							String targetFieldName =end.value();
							String[] targetFieldNameArr = targetFieldName.split(".");
							if(targetFieldNameArr != null && targetFieldNameArr.length > 0){
								recursionInitEntity(entity,targetFieldNameArr,targetFieldNameArr[0]);
							}
						}
						
					}
					
					
				} else if(curFieldValueObj instanceof BaseForm){//支持BaseForm类型到entity实体复制
					BaseForm subBaseForm = (BaseForm)curFieldValueObj;
					Object subEntity = subBaseForm.toEntity();
					//将子entity设置到父entity中
					//将子实体添加到目标实体entity的相关集合中
						String entityFieldName = coverMap.get(field.getName());
						if(entityFieldName == null){//如果未做映射，则退出当前循环，执行下一次循环
							continue;
						}
						
						Class entityFieldTypeClazz = PropertyUtils.getPropertyType(entity, entityFieldName);
						if(entityFieldTypeClazz.isAssignableFrom(subEntity.getClass())){//判断subEntity类型是否与entity实体中对应字段的类型匹配
							PropertyUtils.setProperty(entity, entityFieldName, subEntity);
							
							//同时将父实体关联到子实体
							relateParentEntity2SubEntity(entity, subEntity);
							
						} else {
							//类型是BaseForm,名字叫subBaseForm.getClass().getName()的属性无法转换成名字叫entityFieldName、
							//类型叫entityFieldTypeClazz.getName()的对象到名字叫entity.getClass().getName()的实体中 属性类型叫entityFieldTypeClazz.getName()的字段上去
							throw new RuntimeException("the property of BaseForm type who name is " 
										+ subBaseForm.getClass().getName() 
										+ " can not corver to entity that name is " 
										+ entityFieldName 
										+ " who type is " 
										+ entityFieldTypeClazz.getName()
										+ " in entity \"" 
										+ entity.getClass().getName() + "\"");
						}
						
				}
				
				
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return entity;
	}
	
	private void recursionInitEntity(Object entity,String[] fields,String curField){
		try {
			Field field = entity.getClass().getDeclaredField(curField);
			field.setAccessible(true);
			Class fieldClazz = field.getType();
			
			if(field.get(entity) != null){//如果字段已经初始化过了，则直接结束方法
				return;
			}
			Object nextEntity = null;
			if(StringUtil.isJavaClass(fieldClazz)){//如果不是java自带的类型，则说明是自定义的实体类型，需初始化

				if(Collection.class.isAssignableFrom(fieldClazz)){
					//初始化集合
					Collection colObj = null;
					if(Set.class.isAssignableFrom(fieldClazz)){
						colObj = new HashSet();
					} else if(List.class.isAssignableFrom(fieldClazz)){
						colObj = new ArrayList();
					} else {
						throw new RuntimeException("can not know this field is what Collection type");//不知道该集合用什么实际集合类型来初始化
					}
					field.set(entity, colObj);
					
					Type curFieldType = field.getGenericType();
					if(curFieldType instanceof ParameterizedType){ // 如果是泛型参数的类型   则实例化泛型对象并填值，否则抛异常
				         
		                   ParameterizedType pt = (ParameterizedType) curFieldType;  
		  
		                   Class genericClazz = (Class)pt.getActualTypeArguments()[0]; // 得到泛型里的class类型对象。  
		                   nextEntity = genericClazz.newInstance();
		                   
		                   colObj.add(nextEntity);//将泛型对象存放到集合，使之与entity实体中集合字段做好引用关联
		                   
		            }
					
					
				}
			} else {
				nextEntity = fieldClazz.newInstance();
				field.set(entity, nextEntity);
			}
			
			String nextField = null;
			for(int i = 0 ; i < fields.length; i++){
				if(fields[i].equals(curField)){
					if((i+1) < fields.length){
						nextField = fields[i+1];
						break;
					}
				}
			}
			
			if(nextField != null){
				recursionInitEntity(nextEntity, fields, nextField);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 递归方法
	 * 将值value设置到fields数组最后一个元素所表示的字段名对应的字段上，该字段是entity对象中n级下的一个属性
	 * 注意，entity中凡是需要设置n级属性的父属性，都应该初始化
	 * @param entity
	 * @param fields
	 * @param value
	 * @param num 当前方法递归到fields数组的第num个索引位置，注意num是从0开始，与数组索引规则一致
	 */
	private void scanTargetToSetValue(Object entity,List<String> fields,String value) throws Exception{
		//判断num是否是fields数组的最后位，如果是最后位，就开始设置
		if(fields == null || fields.isEmpty()){
			return;
		}
		
		String curFieldName = fields.get(0);//默认认为第一个索引位置的字段名就是entity中当前待设值的字段名
		Class curFieldClazz = PropertyUtils.getPropertyType(entity, curFieldName);
		Object curFieldValue = PropertyUtils.getSimpleProperty(entity, curFieldName);//当前字段的值对象
		//分情况讨论当前字段类型
		if(Collection.class.isAssignableFrom(curFieldClazz)){//如果是Set集合
			
			
			if(curFieldValue == null){//如果为空，则实例化
				//分情况讨论实例化Set还是List
				if(Set.class.isAssignableFrom(curFieldClazz)){
					curFieldValue = new HashSet();
					PropertyUtils.setSimpleProperty(entity, curFieldName, curFieldValue);//将实例化的集合字段值关联到entity实体中
				} else if(List.class.isAssignableFrom(curFieldClazz)){
					curFieldValue = new ArrayList();
					PropertyUtils.setSimpleProperty(entity, curFieldName, curFieldValue);//将实例化的集合字段值关联到entity实体中
				} else {
					throw new RuntimeException("current method setValue() only provide List and Set Object to instanced");//当前setValue方法只提供List和Set集合对象实例化 
				}
				
			} 
			
			Collection curFieldValueCol = (Collection)curFieldValue;//强转成Set
			
			//判断curFieldValueSet集合对象中是否已存在元素，有，则沿用已有元素，没有，则通过泛型实例化
			Object memberObj = null;//声明一个集合中的成员对像
			if(!curFieldValueCol.isEmpty()){
				Iterator it = curFieldValueCol.iterator();
				memberObj = it.next();//只取第一个元素
			} else {//如果没有，则泛型实例化
				//实例化泛型对象
				Class entityClass = null;
				Mapping formMapping = this.getClass().getAnnotation(Mapping.class);
				if(formMapping != null){
					entityClass = formMapping.entityClass();
					if(entityClass == null ||
							entityClass.isAssignableFrom(Object.class)){
						throw new RuntimeException("can not get Generic type,because we can not find out entity type by current BaseForm type object from it's @Mapping info ");//无法从BaseForm类型对象的@Mapping注解中获取实体类类型信息，所以就无法提取到泛型类型
					}
				} else {
					throw new RuntimeException("can not get Generic type,because we can not find out entity type by current BaseForm type object from it's @Mapping info ");//无法从BaseForm类型对象的@Mapping注解中获取实体类类型信息，所以就无法提取到泛型类型
				}
				
				Field curField = entityClass.getDeclaredField(curFieldName);
				Type curFieldType = curField.getGenericType();
				if(curFieldType instanceof ParameterizedType){ // 如果是泛型参数的类型   则实例化泛型对象并填值，否则抛异常
			         
	                   ParameterizedType pt = (ParameterizedType) curFieldType;  
	  
	                   Class genericClazz = (Class)pt.getActualTypeArguments()[0]; // 得到泛型里的class类型对象。  
	                   memberObj = genericClazz.newInstance();
	                   
	                   curFieldValueCol.add(memberObj);//将泛型对象存放到集合，使之与entity实体中集合字段做好引用关联
	                   
	            } else {
	            	//跑mk:@MSITStore:D:\资料\API文档\JDK_API_1_6_zh_CN.CHM::/java/lang/Class.html#getDeclaredField(java.lang.String)异常，通知系统当前集合字段未配置泛型，无法自动转换对象
	            	throw new RuntimeException("you need set a generic type that can be instanced to object to your entity collection");
	            }
			}
			
			//删除字段名集合的第一个元素，继续递归循环
            fields.remove(0);
            scanTargetToSetValue(memberObj,fields,value);
			
			
		} else {//如果是非集合对象
			if(curFieldValue == null){
				//判断是否是java基本类型
				if(jugePrimitive(curFieldClazz)){
					if(fields.size() > 1){//如果当前实体中该字段已经是基本类型了，而字段集合中还有大于1个的字段名，则说明@Mapping映射实体字段时有误
						throw new RuntimeException("there are an error aboute your formObj to entityObj mapping,current entityObj field name is " + curFieldName + " , and current formObj mapping Fields is " + fields.toString());
					} else if(fields.size() == 1){//如果已经是基本类型，且已经是fields字段名集合的最后一个字段名
						curFieldValue = ConvertUtils.convert(value, curFieldClazz);//转换值
						PropertyUtils.setSimpleProperty(entity, curFieldName, curFieldValue);
						
					}
				} else {//否则尝试是否可以实例化
					try{
						curFieldValue = curFieldClazz.newInstance();
						fields.remove(0);
						scanTargetToSetValue(curFieldValue,fields,value);
						//最后将值关联到entity实体上
						PropertyUtils.setSimpleProperty(entity, curFieldName, curFieldValue);
					} catch(Exception e){
						throw new RuntimeException("current entity field object can not be instanced",e);//当前实体字段对象无法实例化
					}
				}
				
				
			} 
			
			
			
			
		}
		
		
	}
	
	/**
	 * 批量将空串值转成null，null值将会忽略更新到数据库
	 */
	public void emptyValue2NullValue(){
		Field[] fields = this.getClass().getDeclaredFields();
		try{
			for(Field field : fields){
				//通过修改可访问性，获取该字段对应当前对象的字段值
				field.setAccessible(true);
				
				Object curFieldValueObj = field.get(this);
				if(curFieldValueObj != null && curFieldValueObj instanceof String){//只有当值不为空，且为String时，才置空
					String curFieldValue = (String)field.get(this);
					if("".equals(curFieldValue)){
						field.set(this, null);
					}
					
				} else if(curFieldValueObj != null && curFieldValueObj instanceof Collection){//当值是集合类型
					Collection c = (Collection)curFieldValueObj;
					for(Object o : c){
						if(o instanceof BaseForm){
							BaseForm b = (BaseForm)o;
							b.emptyValue2NullValue();
						}
					}
				} else if(curFieldValueObj != null && curFieldValueObj instanceof BaseForm){
					BaseForm b = (BaseForm)curFieldValueObj;
					b.emptyValue2NullValue();
				}
				
				
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 将form和entity之间的映射表获取提出一个抽象方法，目前，在使用HqlGenerateUtil工具生成子对象
	 * 查询条件时会用到。
	 * @return
	 */
	protected Map<String,String> getFormAndEntityFieldMap(){
		//提取字段映射关系
		Map<String,String> coverMap = new HashMap<String, String>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields){
			Mapping fieldMapping = f.getAnnotation(Mapping.class);
			Begin begin = f.getAnnotation(Begin.class);
			End end = f.getAnnotation(End.class);
			
			String fieldName = f.getName();
			
			if(fieldMapping != null){
				String mappingName = fieldMapping.value();
				
				if(mappingName == null || "".equals(mappingName)){
					coverMap.put(fieldName, fieldName);
				} else {
					coverMap.put(fieldName, mappingName);
				}
				
			} 
//			else {
//				if(begin != null){
//					String mappingName = begin.value();
//					
//					if(mappingName != null && !"".equals(mappingName)){
//						coverMap.put(fieldName, mappingName);
//					} else {
//						throw new RuntimeException(this.getClass().getName()+" field name = " + fieldName + " ,the @Begin anno is not have value");
//					}
//				} else if(end != null){
//					String mappingName = end.value();
//					
//					if(mappingName != null && !"".equals(mappingName)){
//						coverMap.put(fieldName, mappingName);
//					} else {
//						throw new RuntimeException(this.getClass().getName()+" field name = " + fieldName + " ,the @End anno is not have value");
//					}
//				}
//				
//			}
			
			
			
		}
		
		return coverMap;
	}
	
	private boolean jugePrimitive(Class clazz){
		return StringUtil.isJavaClass(clazz);
	}
	
	private void relateParentEntity2SubEntity(Object parent,Object sub){
		Field[] fields = sub.getClass().getDeclaredFields();
		
		for(Field f : fields){
			
			//如果当前form对应的实体类型，即entityClass类型与f字段的类型一致，则说明找到了子实体中的父实体字段，则赋值
			if(f.getType().isAssignableFrom(entityClass)){
				try {
					PropertyUtils.setSimpleProperty(sub, f.getName(), parent);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
