package org.jweb.core.query.hibernate.utils;


import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.jweb.core.query.annotation.QueryTimeFormat;
import org.jweb.core.query.hibernate.parse.IHqlParse;
import org.jweb.core.query.hibernate.parse.impl.BigDecimalParseImpl;
import org.jweb.core.query.hibernate.parse.impl.DoubleParseImpl;
import org.jweb.core.query.hibernate.parse.impl.FloatParseImpl;
import org.jweb.core.query.hibernate.parse.impl.IntegerParseImpl;
import org.jweb.core.query.hibernate.parse.impl.LongParseImpl;
import org.jweb.core.query.hibernate.parse.impl.ShortParseImpl;
import org.jweb.core.query.hibernate.parse.impl.StringParseImpl;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;
import org.jweb.core.util.StringUtil;

/**
 * 
 * @author  
 *
 */
public class HqlGenerateUtil {

	/** 时间查询符号 */
	private static final String END = "_end";
	private static final String BEGIN = "_begin";

	private static Map<String, IHqlParse> map = new HashMap<String, IHqlParse>();

	private static final SimpleDateFormat time = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat day = new SimpleDateFormat(
			"yyyy-MM-dd");

	static {
		map.put("class java.lang.Integer", new IntegerParseImpl());
		map.put("class java.lang.Short", new ShortParseImpl());
		map.put("class java.lang.String", new StringParseImpl());
		map.put("class java.lang.Long", new LongParseImpl());
		map.put("class java.lang.Double", new DoubleParseImpl());
		map.put("class java.lang.Float", new FloatParseImpl());
		map.put("class java.math.BigDecimal", new BigDecimalParseImpl());
	}

	/**
	 * 自动生成查询条件HQL 模糊查询 不带有日期组合
	 * 
	 * @param hql
	 * @param values
	 * @param searchObj
	 * @throws Exception
	 */
	public static void installHql(CriteriaQuery cq, Object searchObj) {
		installHqlJoinAlias(cq, searchObj,null, "");
		cq.add();
	}

	/**
	 * 自动生成查询条件HQL（扩展区间查询功能）
	 * 
	 * @param cq
	 * @param searchObj
	 * @param parameterMap
	 *            request参数集合，封装了所有查询信息
	 */
	public static void installHql(CriteriaQuery cq, Object searchObj,Map<String, String[]> parameterMap) {
		installHqlJoinAlias(cq, searchObj, parameterMap, "");
		cq.add();
	}
	/**
	 * 添加Alias别名的查询
	 *@date   2014年1月19日
	 *@param cq
	 *@param searchObj
	 *@param parameterMap
	 *@param alias
	 */
	private static void installHqlJoinAlias(CriteriaQuery cq, Object searchObj, Map parameterMap, String alias) {
		
		if(searchObj == null){//如果模板对象为空，则返回
			return;
		}
		if(parameterMap == null ){//不能让parameterMap为空
			parameterMap = new HashMap();
		}
		
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);
		String aliasName, name, type;
		
		for (int i = 0; i < origDescriptors.length; i++) {
			
			aliasName = (alias.equals("") ? "" : alias + ".") + origDescriptors[i].getName();
			name = origDescriptors[i].getName();
			type = origDescriptors[i].getPropertyType().toString();
			
			try {
				if (judgedIsUselessField(name)
						|| !PropertyUtils.isReadable(searchObj, name)) {
					continue;
				}
				
				// 添加 判断是否有区间值
				String beginValue = null;
				String endValue = null;
				if (parameterMap !=null && parameterMap.containsKey(name + BEGIN)) {
					beginValue = ((String[])(parameterMap.get(name + BEGIN)))[0].trim();
				}
				if (parameterMap !=null && parameterMap.containsKey(name + END)) {
					endValue = ((String[])(parameterMap.get(name + END)))[0].trim();
				}

				Object value = PropertyUtils.getSimpleProperty(searchObj, name);
				
				// 根据类型分类处理
				if (type.contains("class java.lang")
						|| type.contains("class java.math")) {
					
//					if(value != null){//如果字段值不为空，才根据该字段值查询
//					}
					map.get(type).addCriteria(cq, aliasName, value, beginValue, endValue);//无论beginValue\endValue\value是否是空，都传入该方法，该方法内部会自动判断空值
					
				} else if ("class java.util.Date".equals(type)) {
					
					QueryTimeFormat format = origDescriptors[i].getReadMethod() .getAnnotation(QueryTimeFormat.class);
					
					SimpleDateFormat userDefined = null;
					if (format != null) {
						userDefined = new SimpleDateFormat(format.format());
					}
					
					if (StringUtils.isNotBlank(beginValue)) {
						if (userDefined != null) {
							cq.ge(aliasName, time.parse(beginValue));
						} else if (beginValue.length() == 19) {
							cq.ge(aliasName, time.parse(beginValue));
						} else if (beginValue.length() == 10) {
							cq.ge(aliasName, day.parse(beginValue));
						} else {
							Object convertValue = ConvertUtils.convert(beginValue,Date.class);
							cq.ge(aliasName, convertValue);
						}
					}
					
					if (StringUtils.isNotBlank(endValue)) {
						if (userDefined != null) {
							cq.le(aliasName, time.parse(endValue));
						} else if (endValue.length() == 19) {
							cq.le(aliasName, time.parse(endValue));
						} else if (endValue.length() == 10) {
							// 对于"yyyy-MM-dd"格式日期，因时间默认为0，故此添加" 23:23:59"并使用time解析，以方便查询日期时间数据
							cq.le(aliasName, time.parse(endValue + " 23:23:59"));
						}else {
							Object convertValue = ConvertUtils.convert(endValue,Date.class);
							cq.le(aliasName, convertValue);
						}
					}
					
					if (isNotEmpty(value)) {
						cq.eq(aliasName, value);
					}
					
				} else if (!StringUtil.isJavaClass(origDescriptors[i].getPropertyType())) {
					//判断是否是实体对象
						if (isNotEmpty(value) ) {//如果实体对象不为空，则关联该实体表查询
							// 如果是实体类且不为空,创建别名,继续创建查询条件
							//cq.createAlias(aliasName, aliasName);
							
							DetachedCriteria dc = cq.getDetachedCriteria();
							DetachedCriteria subDC = dc.createCriteria(name);//根据当前遍历的字段名创建子DetachedCriteria
							CriteriaQuery subCQ = new CriteriaQuery();//生成DetachedCriteria的包裹类
							subCQ.setDetachedCriteria(subDC);//对DetachedCriteria进行包裹
							
							/** 2015-03-12 wupan 注释原因，如果value(实体对象下值对象)不为空，但值对象中全部字段为空值，不能排除是做大小范围查询
							if(itIsNotAllEmpty(value)){//如果实体对象中存在不为空的字段，则将字段值作为条件值查询
								
							} **/
							
							if(itIsNotAllEmpty(value)){//如果实体对象中存在不为空的字段，则将字段值作为条件值查询
								//陈德佳改，与jeecg原版相比，新的框架采用SearchForm模型处理，二级对象同样应该是SearchForm，并且有自己专属的parmeterMap
								Map subParamMap = (Map)parameterMap.get(name);
								if(subParamMap == null){
									subParamMap = new HashMap();
								}
								installHqlJoinAlias(subCQ, value,subParamMap,"");
								subCQ.add();//只要当前的CriteriaQuery执行installHqlJoinAlias()完成，就应该将installHqlJoinAlias()过程产生的Restrictions条件加到当前CriteriaQuery对应的DetachedCriteria中，否则就丢失了
							}  else {//判断是否有范围查询值不为空
								Map subParamMap = (Map)parameterMap.get(name);
								if(subParamMap != null){
									installHqlJoinAlias(subCQ, value,subParamMap,"");
									subCQ.add();//只要当前的CriteriaQuery执行installHqlJoinAlias()完成，就应该将installHqlJoinAlias()过程产生的Restrictions条件加到当前CriteriaQuery对应的DetachedCriteria中，否则就丢失了
								}
							}
							
						}
					
				} else if(Collection.class.isAssignableFrom(origDescriptors[i].getPropertyType())){//判断是否是Collection的子类
					
					
					if(value != null){//如果多方集合对象不为空，则关联多方表查询
						DetachedCriteria dc = cq.getDetachedCriteria();
						DetachedCriteria subObjCriteria = dc.createCriteria(name) ;//name就是集合对象在当前主entity:searchObj中的属性名
						CriteriaQuery subcq = new CriteriaQuery();//生成一个新的CriteriaQuery,让新的CriteriaQuery去条件化子对象
						subcq.setDetachedCriteria(subObjCriteria);//通过subObjCriteria对象将父对象的cq与子对象的cq关联上，实际内部就是CriteriaQuery与派生CriteriaQuery的关联关系
					
						//提取集合中的值对象，注意，应该保持集合中只有一个值对象，查询条件可以在值上通过! , * NULL !NULL等方式来标注
						Collection col = (Collection)value;
						
						Iterator it = col.iterator();
						if(it.hasNext()){//如果多方集合存在元素，则将元素中非空字段值作为查询条件
							Object colSubObj = it.next();//获取到第一个集合中的子对象后，循环结束
							
							//生成子对象的参数映射表，用于处理范围查询功能
							Map subParameterMap = (Map)parameterMap.get(name);
							if(subParameterMap == null){
								subParameterMap = new HashMap();
							}
							installHqlJoinAlias(subcq, colSubObj, subParameterMap, "");
							subcq.add();//只要当前的CriteriaQuery执行installHqlJoinAlias()完成，就应该将installHqlJoinAlias()过程产生的Restrictions条件加到当前CriteriaQuery对应的DetachedCriteria中，否则就丢失了
						} 
					} else {
						Map subParamMap = (Map)parameterMap.get(name);
						
						if(subParamMap != null){
							DetachedCriteria dc = cq.getDetachedCriteria();
							DetachedCriteria subObjCriteria = dc.createCriteria(name) ;//name就是集合对象在当前主entity:searchObj中的属性名
							CriteriaQuery subcq = new CriteriaQuery();//生成一个新的CriteriaQuery,让新的CriteriaQuery去条件化子对象
							subcq.setDetachedCriteria(subObjCriteria);//通过subObjCriteria对象将父对象的cq与子对象的cq关联上，实际内部就是CriteriaQuery与派生CriteriaQuery的关联关系
						
							installHqlJoinAlias(subcq, value,subParamMap,"");
							subcq.add();//只要当前的CriteriaQuery执行installHqlJoinAlias()完成，就应该将installHqlJoinAlias()过程产生的Restrictions条件加到当前CriteriaQuery对应的DetachedCriteria中，否则就丢失了
						}
						
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean judgedIsUselessField(String name) {
		return "class".equals(name) || "ids".equals(name)
				|| "page".equals(name) || "rows".equals(name)
				|| "sort".equals(name) || "order".equals(name);
	}

	/**
	 * 判断是不是空
	 */
	public static boolean isNotEmpty(Object value) {
		return value != null && !"".equals(value);
	}

	/**
	 * 判断这个类是不是所有属性都为空
	 * 
	 * @param param
	 * @return
	 */
	private static boolean itIsNotAllEmpty(Object param) {
		boolean isNotEmpty = false;
		try {
			PropertyDescriptor origDescriptors[] = PropertyUtils
					.getPropertyDescriptors(param);
			String name;
			for (int i = 0; i < origDescriptors.length; i++) {
				name = origDescriptors[i].getName();
				if ("class".equals(name)
						|| !PropertyUtils.isReadable(param, name)) {
					continue;
				}
				if (Map.class.isAssignableFrom(origDescriptors[i]
						.getPropertyType())) {
					Map<?, ?> map = (Map<?, ?>) PropertyUtils
							.getSimpleProperty(param, name);
					if (map != null && map.size() > 0) {
						isNotEmpty = true;
						break;
					}
				} else if (Collection.class.isAssignableFrom(origDescriptors[i]
						.getPropertyType())) {
					Collection<?> c = (Collection<?>) PropertyUtils
							.getSimpleProperty(param, name);
					if (c != null && c.size() > 0) {
						isNotEmpty = true;
						break;
					}
				} else if (StringUtil.isNotEmpty(PropertyUtils
						.getSimpleProperty(param, name))) {
					isNotEmpty = true;
					break;
				}
			}
		} catch (Exception e) {

		}
		return isNotEmpty;
	}

	
}
