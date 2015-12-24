package org.jweb.core.query.hibernate.qbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

/**
 * 
 *类描述：CriteriaQuery类是对hibernate QBC查询方法的封装，需要的参数是当前操作的实体类
 *张代浩
 *@date： 日期：2012-12-7 时间：上午10:22:15
 *@version 1.0
 */
@SuppressWarnings({"rawtypes","static-access"})
public class CriteriaQuery implements Serializable {
	public CriteriaQuery() {

	}

	private static final long serialVersionUID = 1L;
	private int curPage = 1;// 当前页
	private int pageSize = 10;// 默认一页条数
	private CriterionList criterionList=new CriterionList();//自定义查询条件集合
	private DetachedCriteria detachedCriteria;
	
	private static Map<String, Object> map;//保存查询条件的map
	private static Map<String, Object> ordermap;//排序字段
	private boolean flag = true;// 对同一字段进行第二次重命名查询时值设置FASLE不保存重命名查询条件
	private String field="";//查询需要显示的字段
	private Class entityClass;//POJO 查询的实体模板
	private List results;// 结果集
	private int total;//总记录数
	private List<String> alias = new ArrayList<String>();//保存创建的aliasName 防止重复创建
	
	

	public CriteriaQuery(Class c) {
		this.detachedCriteria = DetachedCriteria.forClass(c);
		this.map = new HashMap<String, Object>();
		this.ordermap = new HashMap<String, Object>();
	}

	

	public CriteriaQuery(Class entityClass, int curPage) {
		this.curPage = curPage;
		this.detachedCriteria = DetachedCriteria.forClass(entityClass);
		this.map = new HashMap<String, Object>();
	}
	



	/**
	 * 加载条件(条件之间有关联) hql((this_.0 like ? and this_.1 like ?) or this_.2 like ?)
	 * 表示法cq.add(cq.or(cq.and(cq, 0, 1), cq, 2))----- hql2:(this_.0 like ? or
	 * this_.1 like ?) 表示法:cq.add(cq.or(cq, 0, 1));
	 * 例子：cq.in("TBPrjstatus.code", status);
		cq.eq("attn", user.getUserName());
		cq.isNull("attn");
		cq.add(cq.and(cq.or(cq, 1, 2), cq, 0));
	 */
	public void add(Criterion c) {
		detachedCriteria.add(c);
	}

	/**
	 * 加载条件
	 */
	public void add() {
		for (int i = 0; i < getCriterionList().size(); i++) {
			add(getCriterionList().getParas(i));
		}
		getCriterionList().removeAll(getCriterionList());
	}
	

	public void createCriteria(String name) {
		detachedCriteria.createCriteria(name);
	}

	public void createCriteria(String name, String value) {
		detachedCriteria.createCriteria(name, value);
	}

	/**
	 * 创建外键表关联对象
	 * 
	 * @param name外键表实体名
	 * @param value引用名
	 */
	public void createAlias(String name, String value) {
		if(!alias.contains(name)){
			detachedCriteria.createAlias(name, value);
			alias.add(name);
		}
	}

	public void setResultTransformer(Class class1) {
		detachedCriteria.setResultTransformer(Transformers.aliasToBean(class1));
	}

	public void setProjection(Property property) {
		detachedCriteria.setProjection(property);
	}

	/**
	 * 设置条件之间and关系
	 * 
	 * @param query
	 * @param source
	 * @param dest
	 *            hql((this_.0 like ? and this_.1 like ?) or this_.2 like ?)
	 *            表示法cq.add(cq.or(cq.and(cq, 0, 1), cq, 2))
	 * @return
	 */
	public Criterion and(CriteriaQuery query, int source, int dest) {
		return Restrictions.and(query.getCriterionList().getParas(source),
				query.getCriterionList().getParas(dest));
	}

	/**
	 * 设置条件之间and关系
	 * 
	 * @param query
	 * @param source
	 * @param dest
	 *            hql:(this_.0 like ? or this_.1 like ?) 表示法:cq.add(cq.or(cq, 0,
	 *            1));
	 * @return
	 */
	public Criterion and(Criterion c, CriteriaQuery query, int souce) {
		return Restrictions.and(c, query.getCriterionList().getParas(souce));
	}
	
	/**
	 *根据CriterionList组合嵌套条件
	 */
	public Criterion getOrCriterion(CriterionList list) {
		Criterion c1=null;
		Criterion c2=null;
		Criterion c3=null;
		c1=list.getParas(0);
		for (int i = 1; i < list.size(); i++) {
			c2=list.getParas(i);
			c3=getor(c1, c2);
			c1=c3;
		}
		return c3;
	}
	/**
	 * 设置组合后的Criterion OR关系
	 * 
	 * @param query
	 * @param source
	 * @param dest
	 * @return
	 */
	public Criterion getor(Criterion c1,Criterion c2) {
		return Restrictions.or(c1, c2);
	}
	

	/**
	 * 设置条件之间and关系
	 * 
	 * @param query
	 * @param source
	 * @param dest
	 * @return
	 */
	public Criterion and(Criterion c1, Criterion c2)

	{
		return Restrictions.and(c1, c2);
	}

	/**
	 * 设置Or查询
	 * 
	 * @param query
	 * @param source条件1
	 * @param dest条件2
	 * @return
	 */
	public Criterion or(CriteriaQuery query, int source, int dest) {
		return Restrictions.or(query.getCriterionList().getParas(source), query
				.getCriterionList().getParas(dest));
	}

	/**
	 * 设置or(Criterion c, CriteriaQuery query, int source)（或）查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public Criterion or(Criterion c, CriteriaQuery query, int source) {
		return Restrictions.or(c, query.getCriterionList().getParas(source));
	}

	/**
	 * 设置or(Criterion c1, Criterion c2)（或）查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 *            两个条件或查询： Restrictions.or(Restrictions.in("username",list1),
	 *            Restrictions.idEq(1)); 三个或多个条件查询:（使用嵌套方式）
	 *            criteria.add(Restrictions
	 *            .or(Restrictions.in("username",list1),
	 *            Restrictions.or(Restrictions.idEq(3), Restrictions.idEq(4))));
	 */
	public void or(Criterion c1, Criterion c2) {
		this.detachedCriteria.add(Restrictions.or(c1, c2));
	}

	
	
	/**
	 * 创建 alias 
	 * @param entitys
	 * 规则 entitys 为a.b.c 这种将会创建 alias a和alias  b而不会创建c
	 * 因为这样更加容易传值
	 */
	public void judgecreateAlias(String entitys) {
		String[] aliass = entitys.split("\\.");
		for (int i = 0 ;i<aliass.length-1;i++){
			createAlias(aliass[i], aliass[i]);
		}
	}

	

	/**
	 * 设置eq(相等)查询条件
	 * 
	 * @param keyname
	 *            :字段名
	 * @param keyvalue
	 *            ：字段值
	 */
	public void eq(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.eq(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置notEq(不等)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void notEq(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.ne(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置like(模糊)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void like(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			//criterionList.addPara(Restrictions.like(keyname, "%" + keyvalue+ "%"));
			criterionList.addPara(Restrictions.like(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置gt(>)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void gt(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.gt(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置lt(<)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void lt(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.lt(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置le(<=)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void le(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.le(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置ge(>=)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void ge(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			criterionList.addPara(Restrictions.ge(keyname, keyvalue));
			if (flag) {
				this.put(keyname, keyvalue);
			}
			flag = true;
		}
	}

	/**
	 * 设置in(包含)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void in(String keyname, Object[] keyvalue) {
		if (keyvalue != null && keyvalue[0] != "") {
			criterionList.addPara(Restrictions.in(keyname, keyvalue));
		}
	}

	/**
	 * 设置isNull查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void isNull(String keyname) {
		criterionList.addPara(Restrictions.isNull(keyname));
	}

	/**
	 * 设置isNull查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void isNotNull(String keyname) {
		criterionList.addPara(Restrictions.isNotNull(keyname));
	}

	/**
	 * 保存查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void put(String keyname, Object keyvalue) {
		if (keyvalue != null && keyvalue != "") {
			map.put(keyname, keyvalue);
		}
	}

	/**
	 * 设置between(之间)查询条件
	 * 
	 * @param keyname
	 * @param keyvalue1
	 * @param keyvalue2
	 */
	public void between(String keyname, Object keyvalue1, Object keyvalue2) {
		Criterion c = null;// 写入between查询条件

		if (!keyvalue1.equals(null) && !keyvalue2.equals(null)) {
			c = Restrictions.between(keyname, keyvalue1, keyvalue2);
		} else if (!keyvalue1.equals(null)) {
			c = Restrictions.ge(keyname, keyvalue1);
		} else if (!keyvalue2.equals(null)) {
			c = Restrictions.le(keyname, keyvalue2);
		}
		criterionList.add(c);
	}

	public void sql(String sql) {
		Restrictions.sqlRestriction(sql);
	}

	public void sql(String sql, Object[] objects, Type[] type) {
		Restrictions.sqlRestriction(sql, objects, type);
	}

	public void sql(String sql, Object objects, Type type) {
		Restrictions.sqlRestriction(sql, objects, type);
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置分页显示数
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public CriterionList getCriterionList() {
		return criterionList;
	}

	public void setCriterionList(CriterionList criterionList) {
		this.criterionList = criterionList;
	}

	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
		this.detachedCriteria = detachedCriteria;
	}



	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public boolean isFlag() {
		return flag;
	}

	/**
	 * 对同一字段进行第二次重命名查询时值设置FASLE不保存重命名查询条件
	 * 
	 * @param flag
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
	
	public static Map<String, Object> getOrdermap() {
		return ordermap;
	}

	public static void setOrdermap(Map<String, Object> ordermap) {
		CriteriaQuery.ordermap = ordermap;
	}

	/**
	 * 设置order（排序）查询条件
	 * 
	 * @param ordername
	 *            ：排序字段名
	 * @param ordervalue
	 *            ：排序字段值（"asc","desc"）
	 */
	public void addOrder(String ordername, String ordervalue) {
		ordermap.put(ordername,ordervalue);

	}
	/**
	 * 设置order（排序）查询条件
	 * 
	 * @param ordername
	 *            ：排序字段名
	 * @param ordervalue
	 *            ：排序字段值（"asc","desc"）
	 */
	public void setOrder(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			judgecreateAlias(entry.getKey());
			if ("asc".equals(entry.getValue())) {
				detachedCriteria.addOrder(Order.asc(entry.getKey()));
			} else {
				detachedCriteria.addOrder(Order.desc(entry.getKey()));
			}
		}
	}
}
