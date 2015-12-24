package org.jweb.core.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.jweb.core.bean.Autocomplete;
import org.jweb.core.bean.DBTable;
import org.jweb.core.bean.PageMode;
import org.jweb.core.dao.IHibernateCommonDao;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;
import org.jweb.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonServiceImpl implements CommonService{
	@Autowired
	private IHibernateCommonDao hibernateCommonDao;//该dao提供一些不依赖于具体前端js框架的查询
	
	@Override
	public List<DBTable> getAllDbTableName() {
		return this.hibernateCommonDao.getAllDbTableName();
	}

	@Override
	public Integer getAllDbTableSize() {
		return this.hibernateCommonDao.getAllDbTableSize();
	}

	@Override
	public  Object save(Object entity) {
		return this.hibernateCommonDao.save(entity);
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		this.hibernateCommonDao.saveOrUpdate(entity);
	}

	@Override
	public <T> void delete(T entity) {
		this.hibernateCommonDao.delete(entity);
		
	}

	@Override
	public <T> void batchSave(List<T> entitys) {
		this.hibernateCommonDao.batchSave(entitys);
		
	}

	@Override
	public <T> T get(Class<T> class1, Serializable id) {
		return (T)this.hibernateCommonDao.get(class1, id);
	}

	@Override
	public <T> T getEntity(Class entityName, Serializable id) {
		return (T)this.hibernateCommonDao.get(entityName, id);
	}

	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		return hibernateCommonDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	@Override
	public <T> List<T> findByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		return hibernateCommonDao.findByProperty(entityClass, propertyName, value);
	}

	@Override
	public <T> List<T> loadAll(Class<T> entityClass) {
		return hibernateCommonDao.loadAll(entityClass);
	}

	@Override
	public <T> void deleteEntityById(Class entityName, Serializable id) {
		this.hibernateCommonDao.delete(entityName, id);
		
	}

	@Override
	public <T> void deleteAllEntitie(Collection<T> entities) {
		this.hibernateCommonDao.batchDelete(entities);
		
	}

	@Override
	public <T> void updateEntitie(T pojo) {
		this.hibernateCommonDao.update(pojo);
		
	}

	@Override
	public <T> List<T> findByQueryString(String hql) {
		return this.hibernateCommonDao.list(hql);
	}

	@Override
	public int updateBySqlString(String sql) {
		return hibernateCommonDao.updateBySqlString(sql);
	}

	@Override
	public <T> List<T> findListbySql(String query) {
		return this.hibernateCommonDao.listBySQL(query);
	}

	@Override
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc) {
		return hibernateCommonDao.findByPropertyisOrder(entityClass, propertyName, value, isAsc);
	}

	@Override
	public <T> List<T> getList(Class clas) {
		return this.hibernateCommonDao.list(clas);
	}

	@Override
	public <T> T singleResult(String hql) {
		return hibernateCommonDao.singleResult(hql);
	}

	@Override
	public Session getSession() {
		return this.hibernateCommonDao.getSession();
	}

	@Override
	public List findByExample(String entityName, Object exampleEntity) {
		return hibernateCommonDao.findByExample(entityName, exampleEntity);
	}

	@Override
	public <T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage) {
		if(ispage){
			int pageSize = cq.getPageSize();
			int pageNo = cq.getCurPage();
			PageMode<T> page = this.hibernateCommonDao.list4page(cq, pageNo, pageSize);
			return page.getResult();
		} else {
			return this.hibernateCommonDao.list(cq);
		}
	}

	@Override
	public <T> List<T> getAutoList(Autocomplete autocomplete) {
		StringBuffer sb = new StringBuffer("");
		for (String searchField : autocomplete.getSearchField().split(",")) {
			sb.append("  or " + searchField + " like '%"
					+ autocomplete.getTrem() + "%' ");
		}
		String hql = "from " + autocomplete.getEntityName() + " where 1!=1 "
				+ sb.toString();
		return hibernateCommonDao.getSession().createQuery(hql)
				.setFirstResult(autocomplete.getCurPage() - 1)
				.setMaxResults(autocomplete.getMaxRows()).list();
	}

	@Override
	public Integer executeSql(String sql, List<Object> param) {
		return hibernateCommonDao.executeSql(sql, param);
	}

	@Override
	public Integer executeSql(String sql, Object... param) {
		return hibernateCommonDao.executeSql(sql, param);
	}

	@Override
	public Integer executeSql(String sql, Map<String, Object> param) {
		return hibernateCommonDao.executeSql(sql, param);
	}

	@Override
	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
		return hibernateCommonDao.executeSqlReturnKey(sql, param);
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.hibernateCommonDao.findForJdbc(sql, objs);
	}

	@Override
	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		return this.hibernateCommonDao.findOneForJdbc(sql, objs);
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		return this.hibernateCommonDao.findForJdbc(sql, page, rows);
	}

	@Override
	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz) {
		return this.hibernateCommonDao.findObjForJdbc(sql, page, rows, clazz);
	}

	@Override
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs) {
		return this.hibernateCommonDao.findForJdbcParam(sql, page, rows, objs);
	}

	@Override
	public Long getCountForJdbc(String sql) {
		return this.hibernateCommonDao.getCountForJdbc(sql);
	}

	@Override
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return this.hibernateCommonDao.getCountForJdbcParam(sql, objs);
	}

	@Override
	public <T> List<T> findHql(String hql, Object... param) {
		return this.hibernateCommonDao.findHql(hql, param);
	}

	@Override
	public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult) {
		return this.hibernateCommonDao.pageList(dc, firstResult, maxResult);
	}

	@Override
	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return this.hibernateCommonDao.findByDetached(dc);
	}

	@Override
	public <T> PageMode<T> list4page(CriteriaQuery cq, int pageNo, int pageSize) {
		return this.hibernateCommonDao.list4page(cq, pageNo, pageSize);
	}

	@Override
	public <T> List<T> list(CriteriaQuery cq) {
		return this.hibernateCommonDao.list(cq);
	}

}
