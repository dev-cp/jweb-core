package org.jweb.core.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jweb.core.bean.annotation.Mapping;
import org.jweb.core.query.annotation.Begin;
import org.jweb.core.query.annotation.End;
import org.jweb.core.query.annotation.QueryStrategy;
import org.jweb.core.query.constant.QueryPattern;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;
import org.jweb.core.query.hibernate.utils.HqlGenerateUtil;

/**
 * 查询form表单模型的抽象父类，提供一些查询模型到数据库查询模型的转换方法
 * 
 * 使用说明：1、searchForm类可以使用Mapping注解指定当前类对应的实体类，toEntity()方法是根据该注解来转换实体类的
 * 2、使用Mapping注解为searchForm类的字段指定与实体类字段的映射关系
 * 3、使用Begin和End注解为searchForm类的字段指定该字段是实体类某字段值的起始和结束范围值
 * 4、使用QueryStrategy注解为searchForm类的字段指定查询策略，如全模糊查询、前置模糊查询、后置模糊查询
 * @author wupan
 *
 */
public abstract class SearchForm extends BaseForm{
	private String pageNo;
	private String pageSize;
	/**
	 * 子类实现该方法，以便通过该方法返回form模型需要转换到的目标entity类class
	 * 例如MemberSearchForm模型是用于查询MemberEntity实体数据的查询数据模型，则
	 * 该方法应该返回MemberEntity的class类模板
	 * 
	 * */
	protected Class getEntityClass(){
		Mapping mapping = this.getClass().getAnnotation(Mapping.class);
		if(mapping == null){
			return null;
		}
		
		return mapping.entityClass();

	}

	public CriteriaQuery getCriteriaQuery(){
		//处理查询策略，如果字段有查询策略注解标注，则增加策略通配符
		this.doQueryStrategy();
		
		
		CriteriaQuery cq = new CriteriaQuery(this.getEntityClass());
		
		
		Object targetEntity = this.toEntity();
		Map paramMap = this.getParamMap();//只能在toEntity()方法后执行，这样才能保证通过toEntity()方法将SearchForm中的所有必要属性初始化完，可以参看POISearchForm#toEntity()
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, targetEntity, paramMap);
		
		return cq;
	}
	
	/**
	 * 对当前searchForm对象处理查询策略，如果字段有查询策略注解标注，则增加策略通配符
	 */
	private void doQueryStrategy(){
		Field[] fields = this.getClass().getDeclaredFields();
		try{
			for(Field field : fields){
				QueryStrategy queryStrategy = field.getAnnotation(QueryStrategy.class);
				if(queryStrategy != null){
					//通过修改可访问性，获取该字段对应当前对象的字段值
					field.setAccessible(true);
					Object curFieldValueObj = field.get(this);
					if(curFieldValueObj == null ){
						continue;
					} else if(curFieldValueObj instanceof String){
						if("".equals(curFieldValueObj)){
							continue;
						} else {
							String curFieldValue = (String)curFieldValueObj;
							StringBuffer sb = new StringBuffer();
							
							QueryPattern queryPattern = queryStrategy.value();
							switch(queryPattern){
							case BEFOREVAGUE:
								sb.append("*").append(curFieldValue);
								break;
							case ENDVAGUE:
								sb.append(curFieldValue).append("*");
								break;
							case FULLVAGUE:
								sb.append("*").append(curFieldValue).append("*");
								break;
							}
							
							field.set(this, sb.toString());
						}
						
					} 
				}
				
				
				String fieldName = field.getName();//获取属性变量名，用于作子参数映射表在父参数映射表中的健
				
				
				
				
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
	}
	
	/**
	 * 生成查询对象的参数映射表，主要是生成区间查询参数
	 * 子参数映射表在父参数映射表中的健名就是在父对象中的属性名
	 * 注意，此处的父对象并不是extends的父对象，而是组合聚合的载体对象
	 * @return
	 */
	public Map getParamMap(){
		Map paramMap = new HashMap();
		Field[] fields = this.getClass().getDeclaredFields();
		try{
			for(Field field : fields){
				String fieldName = field.getName();//获取属性变量名，用于作子参数映射表在父参数映射表中的健
				
				//通过修改可访问性，获取该字段对应当前对象的字段值
				field.setAccessible(true);
				Object curFieldValueObj = field.get(this);
				if(curFieldValueObj == null ){
					continue;
				} else if(curFieldValueObj instanceof String){
					String curFieldValue = (String)curFieldValueObj;
					
					
					Begin begin = field.getAnnotation(Begin.class);
					End end = field.getAnnotation(End.class);
					if(begin != null){
						String targetFieldName = begin.value();
						String[] targetFieldNameArr = targetFieldName.split("\\.");
						
						recursionInitParamMap(paramMap, targetFieldNameArr, 0, targetFieldNameArr.length-1, curFieldValue,"_begin");
//						paramMap.put(targetFieldName+"_begin", new String[]{curFieldValue});
					}
					
					if(end != null){
						String targetFieldName = end.value();
						String[] targetFieldNameArr = targetFieldName.split("\\.");
						recursionInitParamMap(paramMap, targetFieldNameArr, 0, targetFieldNameArr.length-1, curFieldValue,"_end");
//						paramMap.put(targetFieldName+"_end", new String[]{curFieldValue});
					}
				} else if(curFieldValueObj instanceof SearchForm){//如果查询对象的属性也是SearchForm,则开始递归生成子paramMap
					SearchForm subSearchForm = (SearchForm) curFieldValueObj ;
					Map<String,String[]> subParamMap = subSearchForm.getParamMap();
					
					Map<String,String> formEntityFieldMap = this.getFormAndEntityFieldMap();
					String entityFieldName = formEntityFieldMap.get(fieldName);
					paramMap.put(entityFieldName, subParamMap);//将子对象参数映射放到父对象参数映射中，依次一级一级的放置成树,注意，健名是实体对象中的属性名
					
				} else if(curFieldValueObj instanceof Collection){//如果查询对象的属性是集合，则提取集合的第一个子元素，递归生成子paramMap
					Collection subCol = (Collection)curFieldValueObj;
					Iterator it = subCol.iterator();
					while(it.hasNext()){
						Object subObj = it.next();
						if(subObj instanceof SearchForm){
							SearchForm subSearchForm = (SearchForm) subObj ;
							Map<String,String[]> subParamMap = subSearchForm.getParamMap();
							
							Map<String,String> formEntityFieldMap = this.getFormAndEntityFieldMap();
							String entityFieldName = formEntityFieldMap.get(fieldName);
							paramMap.put(entityFieldName, subParamMap);//将子对象参数映射放到父对象参数映射中，依次一级一级的放置成树
							
						}
						break;//只允许查询对象的集合属性循环一次，所以应该保持集合属性中只存放一个子查询对象，多了不利于使用! * , NULL !NULL等适配符
					}
				}
				
				
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return paramMap;
	}
	
	/**
	 * 递归将Begin、End注解的值构建到查询参数map中
	 * @param map 当前待设置值得map
	 * @param fields 被“.”号分隔的层级字段名，转换后的数组，只有最后一个元素是真正的字段名，前面的值都是对象在父对象下的字段名
	 * @param curIndex 当前循环的数组索引
	 * @param endIndex 当curIndex==endIndex值，表示递归结束
	 * @param value 待设置的值
	 * @param suffix 最后一个字段名在设置入map键值时的后缀，这里有两个值“_begin”,"_end",表示判断起始还是结束的比对
	 */
	private void recursionInitParamMap(Map<String,Object> map,String[] fields,int curIndex,int endIndex,String value,String suffix){
		if(curIndex == endIndex){
			map.put(fields[curIndex]+suffix, new String[]{value});
		} else {
			Map<String,Object> subMap = (Map<String,Object>)map.get(fields[curIndex]);
			if(subMap == null){
				subMap = new HashMap<String,Object>();
				map.put(fields[curIndex], subMap);
				
				recursionInitParamMap(subMap,fields,curIndex+1,endIndex,value,suffix);
			} else {
				recursionInitParamMap(subMap,fields,curIndex+1,endIndex,value,suffix);
			}
			
		}
	}
	
	/**
	 * 用于判断对象所有字段是否都为空值，所有字段都为空值，则不应该将该对象实例化做关联查询
	 * @return
	 */
	protected boolean judgeNullValue(Object obj){
		PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(obj);
		Field[] fields = obj.getClass().getDeclaredFields();
		
		
		if(origDescriptors != null){
			for(Field f : fields){
				String fieldName = f.getName();
				try {
					Object value = PropertyUtils.getSimpleProperty(obj, fieldName);
					
					if(value != null){
						return false;
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
				}
			}
			
			
		}
		
		
		return true;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

}
