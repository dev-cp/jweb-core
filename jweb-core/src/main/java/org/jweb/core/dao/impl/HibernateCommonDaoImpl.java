package org.jweb.core.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.Transformers;
import org.jweb.core.bean.DBTable;
import org.jweb.core.bean.PageMode;
import org.jweb.core.bean.annotation.JwebEntityTitle;
import org.jweb.core.dao.IHibernateCommonDao;
import org.jweb.core.dao.util.DaoUtil;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;
import org.jweb.core.query.hibernate.utils.HqlGenerateUtil;
import org.jweb.core.util.CommonUtil;
import org.jweb.core.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
@Component("hibernateCommonDao")
public class HibernateCommonDaoImpl extends AbstractGenericBaseCommonDao implements IHibernateCommonDao{
	private static final Logger logger = Logger
			.getLogger(HibernateCommonDaoImpl.class);

	/**
	 * hibernate session工厂
	 */
	@Autowired
	private SessionFactory sessionFactory;
//	private LocalSessionFactoryBean sessionFactory;
	
	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		List<DBTable> resultList = new ArrayList<DBTable>();
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		for (String key : (Set<String>) metaMap.keySet()) {
			DBTable dbTable = new DBTable();
			AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap
					.get(key);
			dbTable.setTableName(classMetadata.getTableName());
			dbTable.setEntityName(classMetadata.getEntityName());
			Class<?> c;
			try {
				c = Class.forName(key);
				JwebEntityTitle t = c.getAnnotation(JwebEntityTitle.class);
				dbTable.setTableTitle(t != null ? t.name() : "");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			resultList.add(dbTable);
		}
		return resultList;
	}

	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public Integer getAllDbTableSize() {
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		return metaMap.size();
	}
	
	
	
	/********************** IGenericBaseCommonDao接口实现部分 **************************/
	public Object save(Object o) {
		return sessionFactory.getCurrentSession().save(o);
	}

	public boolean update(Object o) {
//		sessionFactory.getCurrentSession().update(o);
		sessionFactory.getCurrentSession().saveOrUpdate(o);
		return true;
	}
	
	/**
	 * 根据传入的实体添加或更新对象
	 * 
	 * @param <T>
	 * 
	 * @param entity
	 */

	public <T> void saveOrUpdate(T entity) {
		try {
			getSession().saveOrUpdate(entity);
			getSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("添加或更新成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("添加或更新异常", e);
			throw e;
		}
	}

	@Override
	public void batchSave(List lt) {
		this.batchSave(lt, lt.size());
		
	}

	@Override
	public void batchUpdate(List lt) {
		this.batchUpdate(lt, lt.size());
		
	}

	public boolean delete(java.lang.Class c, Serializable i) {
		Object o = get(c, i);
		if (o != null) {
			this.delete(o);
			return true;
		}
		return false;
	}

	public boolean delete(Class c, Serializable[] dks) {
		if (dks == null)
			return false;
		Session ss = sessionFactory.getCurrentSession();
		for (int i = 0; i < dks.length; i++) {
			Object o = ss.get(c, dks[i]);
			if (o != null) {
				ss.delete(o);
				if ((i + 1) % 50 == 0) {
					ss.flush();
					ss.clear();
				}
			}
		}
		return true;
	}

	public boolean delete(Object o) {
		getSession().delete(o);
		getSession().flush();
		return true;
	}
	
	public boolean batchDelete(Collection collection){
		if(collection != null){
			Iterator it = collection.iterator();
			while(it.hasNext()){
				Object obj = it.next();
				this.delete(obj);
			}
		}
		
		return true;
	}

	public int getTotal(Class c) {
		return CommonUtil.object2int(
				this.getValue("select count(*) from " + c.getName()), -1);
	}

	@Override
	public int getTotal(Class c, Object example) {
		StringBuffer sb = new StringBuffer("select count(*) from " + c.getName() + " 1=1 ");
		
		Map<String ,Object> nvps = super.getJavaBeanNameValuePairMap(example);
		Set<Entry<String,Object>> entrySet = nvps.entrySet();
		for(Entry<String,Object> entry : entrySet){
			sb.append(" and " + entry.getKey() + "= ? ");
		}
		String hql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(Entry<String,Object> entry : entrySet){
			query.setParameter(entry.getKey(), entry.getValue());
		}
		Object result = query.uniqueResult();
		return CommonUtil.object2int(result,-1);
		
	}

	public Object get(Class c, Serializable i) {
		return sessionFactory.getCurrentSession().get(c, i);
	}

	public List list(Class c) {
		List lt = sessionFactory.getCurrentSession()
				.createQuery("from " + c.getName()).list();
		return lt;
	}

	public List list(Class c, int pageNo, int pageSize) {
		return this.list("from " + c.getName(), pageNo, pageSize);
	}
	
	public PageMode list4page(Class c, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		int count = this.getTotal(c);
		
		List list = this.list("from " + c.getName(), pageNo, pageSize);
		
		int pageCount = count/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(count);
		
		return page;
		
	}

	@Override
	public List list(Class c, Object example) {
		CriteriaQuery cq = new CriteriaQuery(c);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, example);
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();

		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		List list = criteria.list();
		
		
//		StringBuffer sb = new StringBuffer("select * from " + c.getName() + " 1=1 ");
//		
//		Map<String ,Object> nvps = super.getJavaBeanNameValuePairMap(example);
//		Set<Entry<String,Object>> entrySet = nvps.entrySet();
//		for(Entry<String,Object> entry : entrySet){
//			sb.append(" and " + entry.getKey() + "= ? ");
//		}
//		String hql = sb.toString();
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		for(Entry<String,Object> entry : entrySet){
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//		
//		return query.list();
		
		return list;
		
	}

	@Override
	public List list(Class c, Object example, int pageNo, int pageSize) {
		CriteriaQuery cq = new CriteriaQuery(c);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, example);
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}

		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);

		
		List list = criteria.list();
		
		
//		StringBuffer sb = new StringBuffer("select * from " + c.getName() + " 1=1 ");
//		
//		Map<String ,Object> nvps = super.getJavaBeanNameValuePairMap(example);
//		Set<Entry<String,Object>> entrySet = nvps.entrySet();
//		for(Entry<String,Object> entry : entrySet){
//			sb.append(" and " + entry.getKey() + "= ? ");
//		}
//		String hql = sb.toString();
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		for(Entry<String,Object> entry : entrySet){
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//		int start = (pageNo - 1) * pageSize;
//		return query.setMaxResults(pageSize).setFirstResult(start).list();
		
		return list;
		
	}
	
	public PageMode list4page(Class c, Object example, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		CriteriaQuery cq = new CriteriaQuery(c);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, example);
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}

		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);

		
		List list = criteria.list();
		
		
//		int count = this.getTotal(c, example);
//		
//		StringBuffer sb = new StringBuffer("select * from " + c.getName() + " 1=1 ");
//		
//		Map<String ,Object> nvps = super.getJavaBeanNameValuePairMap(example);
//		Set<Entry<String,Object>> entrySet = nvps.entrySet();
//		for(Entry<String,Object> entry : entrySet){
//			sb.append(" and " + entry.getKey() + "= ? ");
//		}
//		String hql = sb.toString();
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		for(Entry<String,Object> entry : entrySet){
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//		int start = (pageNo - 1) * pageSize;
//		List list = query.setMaxResults(pageSize).setFirstResult(start).list();
		
		int pageCount = allCounts/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(allCounts);
		
		return page;
		
	}
	
	
	public PageMode list4page(Class c, Object example, Map<String,String[]> rangeMap,int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		CriteriaQuery cq = new CriteriaQuery(c);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, example,rangeMap);
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}
		
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);
		
		
		List list = criteria.list();
		
		
//		int count = this.getTotal(c, example);
//		
//		StringBuffer sb = new StringBuffer("select * from " + c.getName() + " 1=1 ");
//		
//		Map<String ,Object> nvps = super.getJavaBeanNameValuePairMap(example);
//		Set<Entry<String,Object>> entrySet = nvps.entrySet();
//		for(Entry<String,Object> entry : entrySet){
//			sb.append(" and " + entry.getKey() + "= ? ");
//		}
//		String hql = sb.toString();
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		for(Entry<String,Object> entry : entrySet){
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//		int start = (pageNo - 1) * pageSize;
//		List list = query.setMaxResults(pageSize).setFirstResult(start).list();
		
		int pageCount = allCounts/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(allCounts);
		
		return page;
		
	}

	
	/******************* IHibernateCommonDao实现部分 *************************/

	

	public void batchUpdate(List lt, int max) {
		Session ss = sessionFactory.getCurrentSession();
		int c = 0;
		for (Object o : lt) {
			ss.update(o);
			if (++c % max == 0) {
				ss.flush();
				ss.clear();
			}
		}
	}

	public void batchSave(List lt, int max) {
		Session ss = sessionFactory.getCurrentSession();
		int c = 0;
		for (Object o : lt) {
			ss.save(o);
			if (++c % max == 0) {
				ss.flush();
				ss.clear();
			}
		}
	}

	public void batchMerge(List lt, int max) {
		Session ss = sessionFactory.getCurrentSession();
		int c = 0;
		for (Object o : lt) {
			ss.merge(o);
			if (++c % max == 0) {
				ss.flush();
				ss.clear();
			}
		}
	}

	public Criteria createCriteria(Class c) {
		return sessionFactory.getCurrentSession().createCriteria(c);
	}

	public Criteria createCriteria(String s) {
		return sessionFactory.getCurrentSession().createCriteria(s);
	}

	public Criteria createCriteria(Class c, String s) {
		return sessionFactory.getCurrentSession().createCriteria(c, s);
	}

	public Criteria createCriteria(String s1, String s2) {
		return sessionFactory.getCurrentSession().createCriteria(s1, s2);
	}


	public Object find(Class c, String wql) throws NonUniqueResultException {
		return sessionFactory.getCurrentSession()
				.createQuery("from " + c.getName() + " where 1=1 " + wql)
				.uniqueResult();
	}



	public void executeHql(String hql) {
		sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
	}

	public int executeSql(String sql) {
		return sessionFactory.getCurrentSession().createSQLQuery(sql)
				.executeUpdate();
	}


	public Object getValue(String hql) {
		return sessionFactory.getCurrentSession().createQuery(hql)
				.uniqueResult();
	}


	public int getTotal(Class c, String wql) {
		return CommonUtil.object2int(
				this.getValue("select count(*) from " + c.getName()
						+ " where 1=1 " + wql), -1);
	}


	public List list(String hql) {
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	public List list(String hql, int pageNo, int pageSize) {
		Session ss = sessionFactory.getCurrentSession();
		Query qq = ss.createQuery(hql);
		int start = (pageNo - 1) * pageSize;
		return qq.setMaxResults(pageSize).setFirstResult(start).list();
	}
	
	public PageMode list4page(String hql, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		Session ss = sessionFactory.getCurrentSession();
		
		//查询总记录数
		String countHql = "select count(*) from ( "+ hql +" ) as t ";
		int count = CommonUtil.object2int(ss.createQuery(countHql).uniqueResult(),-1);
		
		//查询结果集
		Query qq = ss.createQuery(hql);
		int start = (pageNo - 1) * pageSize;
		List list = qq.setMaxResults(pageSize).setFirstResult(start).list();
		
		//设置分页信息
		int pageCount = count/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotal(count);
		page.setResult(list);
		
		return page;
	}


	public List list(Class c, String wql) {
		List lt = sessionFactory.getCurrentSession()
				.createQuery("from " + c.getName() + " where 1=1 " + wql)
				.list();
		return lt;
	}

	public List list(Class c, String wql, int pageNo, int pageSize) {
		return this.list("from " + c.getName() + " where 1=1 " + wql, pageNo,
				pageSize);
	}
	
	
	public PageMode list4page(Class c, String wql, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		int count = this.getTotal(c, wql);
		
		List list = this.list("from " + c.getName() + " where 1=1 " + wql, pageNo,
				pageSize);
		
		int pageCount = count/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(count);
		
		return page;
	}

	public int update(String hql) {
		return sessionFactory.getCurrentSession().createQuery(hql)
				.executeUpdate();
	}

	

	public boolean merge(Object o) {
		sessionFactory.getCurrentSession().merge(o);
		return true;
	}

	public List listBySQL(String sql) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List lt = query.list();
		return lt;
	}

	public List listBySQL(String sql, int pageNo, int pageSize) {
		Session ss = sessionFactory.getCurrentSession();
		Query qq = ss.createSQLQuery(sql);
		int start = (pageNo - 1) * pageSize;
		return qq.setMaxResults(pageSize).setFirstResult(start).list();
	}
	
	public PageMode list4pageBySQL(String sql, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		
		Session ss = sessionFactory.getCurrentSession();
		
		String countSql = "select count(*) from ( "+ sql +" ) as t ";
		int count = CommonUtil.object2int(ss.createSQLQuery(countSql).uniqueResult(),-1);
		
		
		Query qq = ss.createSQLQuery(sql);
		int start = (pageNo - 1) * pageSize;
		List list = qq.setMaxResults(pageSize).setFirstResult(start).list();
		
		int pageCount = count/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(count);
		
		return page;
	}
	
	public PageMode listMapPageBySQL(String sql, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		Session ss = sessionFactory.getCurrentSession();
		String countSql = "select count(*) from ( "+ sql +" ) as t ";
		int count = CommonUtil.object2int(ss.createSQLQuery(countSql).uniqueResult(),-1);
		SQLQuery qq = ss.createSQLQuery(sql);
		int start = (pageNo - 1) * pageSize;
		qq.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list  = qq.setMaxResults(pageSize).setFirstResult(start).list();
		int pageCount = count/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(qq.list());
		page.setTotal(count);
		
		return page;
	}

	public Object getValueBySQL(String sql) {
		return sessionFactory.getCurrentSession().createSQLQuery(sql)
				.uniqueResult();
	}

	

	public Object find(String hql) {
		return sessionFactory.getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	public void flushAndClear() {
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	public Query createSQLQuery(String hql) {
		return sessionFactory.getCurrentSession().createSQLQuery(hql);
	}

	public Query createNamedSQLQuery(String NamedSql) {
		return sessionFactory.getCurrentSession().getNamedQuery(NamedSql);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public PageMode list4page(CriteriaQuery cq, int pageNo, int pageSize,Map<String,String> map) {
		PageMode page = new PageMode();
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				this.sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}
		
		DetachedCriteria dc = cq.getDetachedCriteria();
		for(String key:map.keySet()){
			String filed = key;
			String type = map.get(key);
			if(type.equals("desc")){
				dc.addOrder(Order.desc(filed));
			}
			if(type.equals("asc")){
				dc.addOrder(Order.asc(filed));
			}
			
		}
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);
		
		List list = criteria.list();
		
		int pageCount = allCounts/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(allCounts);
		
		return page;
	}
	@Override
	public PageMode list4pageorderby(CriteriaQuery cq, int pageNo, int pageSize,Map<String,String> map) {
		PageMode page = new PageMode();
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				this.sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}
		
		DetachedCriteria dc = cq.getDetachedCriteria();
		for(String key:map.keySet()){
			String filed = key;
			String type = map.get(key);
			if(type.equals("desc")){
				dc.addOrder(Order.desc(filed));
			}
			if(type.equals("asc")){
				dc.addOrder(Order.asc(filed));
			}
			
		}
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);
		
		List list = criteria.list();
		
		int pageCount = allCounts/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(allCounts);
		
		return page;
	}
	@Override
	public PageMode list4page(CriteriaQuery cq, int pageNo, int pageSize) {
		PageMode page = new PageMode();
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				this.sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		Object b = criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		final int allCounts;
		if (b != null) {
			allCounts = ((Long) b).intValue();
		} else {
			allCounts = 0;
		}
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		// 判断是否有排序字段
		if (!cq.getOrdermap().isEmpty()) {
			cq.setOrder(cq.getOrdermap());
		}

		int start = (pageNo - 1) * pageSize;
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);
		
		List list = criteria.list();
		
		int pageCount = allCounts/pageSize + 1;
		page.setPageCount(pageCount);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setResult(list);
		page.setTotal(allCounts);
		
		return page;
	}

	@Override
	public PageMode list4page(CriteriaQuery cq) {
		int pageSize= cq.getPageSize();
		int pageNo = cq.getCurPage();
		
		// 判断是否有排序字段
		if (!cq.getOrdermap().isEmpty()) {
			cq.setOrder(cq.getOrdermap());
		}
		
		return this.list4page(cq, pageNo, pageSize);
	}

	@Override
	public List list(CriteriaQuery cq) {
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				this.sessionFactory.getCurrentSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		// 判断是否有排序字段
		if (!cq.getOrdermap().isEmpty()) {
			cq.setOrder(cq.getOrdermap());
		}
		
		List list = criteria.list();
		return list;
	}
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).uniqueResult();
	}
	
	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (List<T>) createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).list();
	}
	
	public <T> List<T> loadAll(final Class<T> entityClass) {
		Criteria criteria = createCriteria(entityClass);
		return criteria.list();
	}
	
	/**
	 * 通过sql更新记录
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public int updateBySqlString(final String query) {

		Query querys = getSession().createSQLQuery(query);
		return querys.executeUpdate();
	}
	
	/**
	 * 根据属性名和属性值查询. 有排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, isAsc,
				Restrictions.eq(propertyName, value)).list();
	}
	
	/**
	 * 通过hql查询唯一对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> T singleResult(String hql) {
		T t = null;
		Query queryObject = getSession().createQuery(hql);
		List<T> list = queryObject.list();
		if (list.size() == 1) {
			getSession().flush();
			t = list.get(0);
		} else if (list.size() > 0) {
			throw new RuntimeException("查询结果数:" + list.size() + "大于1");
		}
		return t;
	}
	
	/**
	 * 根据实体模版查找
	 * 
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */

	public List findByExample(final String entityName,
			final Object exampleEntity) {
		Assert.notNull(exampleEntity, "Example entity must not be null");
		Criteria executableCriteria = (entityName != null ? getSession()
				.createCriteria(entityName) : getSession().createCriteria(
				exampleEntity.getClass()));
		executableCriteria.add(Example.create(exampleEntity));
		return executableCriteria.list();
	}
	
	public Integer executeSql(String sql, List<Object> param) {
		return this.jdbcTemplate.update(sql, param);
	}
	
	public Integer executeSql(String sql, Object... param) {
		return this.jdbcTemplate.update(sql, param);
	}
	
	public Integer executeSql(String sql, Map<String, Object> param) {
		return this.namedParameterJdbcTemplate.update(sql, param);
	}
	
	public Object executeSqlReturnKey(final String sql, Map<String, Object> param) {
		Object keyValue = null;
		try{
			KeyHolder keyHolder = new GeneratedKeyHolder(); 
			SqlParameterSource sqlp  = new MapSqlParameterSource(param);
			this.namedParameterJdbcTemplate.update(sql,sqlp, keyHolder);
			keyValue = keyHolder.getKey().longValue();
		}catch (Exception e) {
			keyValue = null;
		}
		return keyValue;
	}
	
	
	
	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		// 封装分页SQL
		sql = DaoUtil.createPageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		try {
			return this.jdbcTemplate.queryForMap(sql, objs);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 创建Criteria对象，有排序功能。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param orderBy
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass, boolean isAsc,
			Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		if (isAsc) {
			criteria.addOrder(Order.asc("asc"));
		} else {
			criteria.addOrder(Order.desc("desc"));
		}
		return criteria;
	}
	
	/**
	 * 创建Criteria对象带属性比较
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	@Override
	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz) {
		List<T> rsList = new ArrayList<T>();
		// 封装分页SQL
		sql = DaoUtil.createPageSql(sql, page, rows);
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

		T po = null;
		for (Map<String, Object> m : mapList) {
			try {
				po = clazz.newInstance();
				MyBeanUtils.copyMap2Bean_Nobig(po, m);
				rsList.add(po);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rsList;
	}

	@Override
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs) {
		// 封装分页SQL
		sql = DaoUtil.createPageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	@Override
	public Long getCountForJdbc(String sql) {
		return this.jdbcTemplate.queryForLong(sql);
	}

	@Override
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return this.jdbcTemplate.queryForLong(sql, objs);
	}
	
	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHql(String hql, Object... param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	
	public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult) {
		Criteria criteria = dc.getExecutableCriteria(getSession());
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		return criteria.list();
	}

	/**
	 * 离线查询
	 */
	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return dc.getExecutableCriteria(getSession()).list();
	}
}
