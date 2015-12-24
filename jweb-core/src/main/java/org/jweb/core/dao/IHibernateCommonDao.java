package org.jweb.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.jweb.core.bean.PageMode;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;

/**
 * 针对hibernate 持久化框架而定制的dao接口
 * @author wupan
 *
 */
public interface IHibernateCommonDao extends IGenericBaseCommonDao{
	
	public Session getSession() ;
	/**
	 * 批量更新
	 * @param lt 待更新的数据集合
	 * @param max 以max条数据为单位向数据库写入
	 */
	public void batchUpdate(List lt, int max) ;

	/**
	 * 批量保存
	 * @param lt 待保存数据集合
	 * @param max 以max条数据为单位向数据库写入
	 */
	public void batchSave(List lt, int max) ;

	/**
	 * 批量合并
	 * @param lt 待合并数据集合
	 * @param max以max条数据为单位向数据库写入
	 */
	public void batchMerge(List lt, int max) ;

	/**
	 * 创建条件查询器
	 * @param c
	 * @return
	 */
	public Criteria createCriteria(Class c) ;
	
	/**
	 * 创建条件查询器
	 * @param s
	 * @return
	 */
	public Criteria createCriteria(String s) ;
	
	/**
	 * 创建条件查询器
	 * @param c
	 * @param s
	 * @return
	 */
	public Criteria createCriteria(Class c, String s) ;

	/**
	 * 创建条件查询器
	 * @param s1
	 * @param s2
	 * @return
	 */
	public Criteria createCriteria(String s1, String s2) ;

	/**
	 * 根据id 查询实体类对象
	 * @param c
	 * @param i
	 * @return
	 */
	public Object get(Class c, Serializable i) ;

	/**
	 * 根据hql的查询条件查询实体类集合数据
	 * @param c
	 * @param wql where字句中的条件表达式语句，注意应该以and开头
	 * @return
	 * @throws NonUniqueResultException
	 */
	public Object find(Class c, String wql) throws NonUniqueResultException ;

	/**
	 * 保存实体
	 * @param o
	 * @return 返回保存后的实体id
	 */
	public Object save(Object o) ;

	/**
	 * 删除实体
	 * @param c 实体类型
	 * @param i 实体的主键id
	 * @return 成功返回true
	 */
	public boolean delete(java.lang.Class c, Serializable i) ;

	/**
	 * 执行hql语句
	 * @param hql
	 */
	public void executeHql(String hql) ;

	/**
	 * 执行sql语句
	 * @param sql
	 * @return
	 */
	public int executeSql(String sql) ;

	/**
	 * 批量删除实体
	 * @param c 实体类型
	 * @param dks 待删除的实体id数组
	 * @return
	 */
	public boolean delete(Class c, Serializable[] dks) ;

	/**
	 * 根据hql语句获取唯一实体对象，只返回一个实体对象，注意hql应该具有唯一值
	 * @param hql
	 * @return
	 */
	public Object getValue(String hql);

	/**
	 * 获取某个类型实体的总数，注意，假删除数据也会被计数
	 * @param c 实体类型
	 * @return
	 */
	public int getTotal(Class c) ;

	/**
	 * 获取某个条件下的实体总数
	 * @param c
	 * @param wql hql语句的where子句下的条件表达式，注意以and开头
	 * @return
	 */
	public int getTotal(Class c, String wql) ;

	/**
	 * 获取某一实体类型的全部数据集合
	 * @param c
	 * @return
	 */
	public List list(Class c) ;

	/**
	 * 根据Hql查询语句获取实体数据集合
	 * @param hql
	 * @return
	 */
	public List list(String hql) ;

	/**
	 * 根据hql查询语句分页获取实体数据集合
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List list(String hql, int pageNo, int pageSize);
	
	/**
	 * 根据hql查询语句分页获取实体数据集合
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @return 返回分页模型
	 */
	public PageMode list4page(String hql, int pageNo, int pageSize);

	/**
	 * 根据实体类型分页获取实体数据集合
	 * @param c
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List list(Class c, int pageNo, int pageSize) ;
	
	

	/**
	 * 根据实体类型和hql的条件子句查询实体数据集合
	 * @param c
	 * @param wql 以and开头的hql条件子句
	 * @return
	 */
	public List list(Class c, String wql) ;

	/**
	 * 根据实体类型、hql条件子句、分页信息查询实体数据集合
	 * @param c
	 * @param wql 以and开头的hql条件子句
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List list(Class c, String wql, int pageNo, int pageSize) ;
	
	/**
	 * 根据封装条件cq查询全部列表信息，cq可以通过HqlGenerateUtil工具封装出多种查询模式效果，功能强大
	 * @param cq
	 * @return
	 */
	public List list(CriteriaQuery cq) ;
	
	/**
	 * 根据实体类型、hql条件子句、分页信息查询实体数据集合
	 * @param c
	 * @param wql
	 * @param pageNo
	 * @param pageSize
	 * @return 返回分页模型
	 */
	public PageMode list4page(Class c, String wql, int pageNo, int pageSize) ;
	
	/**
	 * 根据查询器进行分页查询
	 * @param cq 包裹hibernate Creiteria而编写的自定义查询器，该查询器组装了目标模型中的查询参数
	 * 				和查询方式等信息，可以根据查询参数和查询方式从数据库中查询数据，例如，查询参数有
	 * 				某个字段等于某值，某字段值应该在某范围值内查询，使用or还是and连接查询条件、排序、
	 * 				分组、表关联、组合聚合等多种信息
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageMode list4page(CriteriaQuery cq, int pageNo, int pageSize) ;
	
	/**
	 * 不显示指出分页信息，分页信息从cq中提取
	 * @param cq
	 * @return
	 */
	public PageMode list4page(CriteriaQuery cq) ;

	/**
	 * 根据hql语句进行更新
	 * @param hql
	 * @return
	 */
	public int update(String hql) ;

	/**
	 * 更某实体对象
	 * @param o
	 * @return
	 */
	public boolean update(Object o) ;

	/**
	 * 合并某对象
	 * @param o
	 * @return
	 */
	public boolean merge(Object o) ;

	/**
	 * 根据sql语句查询实体数据集合
	 * @param sql
	 * @return
	 */
	public List listBySQL(String sql) ;

	/**
	 * 根据sql语句和分页信息查询实体数据集合
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List listBySQL(String sql, int pageNo, int pageSize) ;
	
	/**
	 * 根据sql语句和分页信息查询实体数据集合
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return 返回分页模型
	 */
	public PageMode list4pageBySQL(String sql, int pageNo, int pageSize) ;

	/**
	 * 根据sql语句获取一个实体数据对象，注意是获取唯一实体对象
	 * @param sql
	 * @return
	 */
	public Object getValueBySQL(String sql) ;

	/**
	 * 删除某实体对象
	 * @param o
	 * @return
	 */
	public boolean delete(Object o) ;

	/**
	 * 根据hql语句查找一个实体对象
	 * @param hql
	 * @return
	 */
	public Object find(String hql) ;
	
	public <T> List<T> findHql(String hql, Object... param);

	/**
	 * 刷新并清理当前持久化框架session
	 */
	public void flushAndClear() ;

	/**
	 * 根据hql语句创建一个hibernate sql查询器
	 * @param hql
	 * @return
	 */
	public Query createSQLQuery(String hql) ;

	/**
	 * 根据命名sql语句创建一个hibernate 命名查询器
	 * @param NamedSql
	 * @return
	 */
	public Query createNamedSQLQuery(String NamedSql);

	public PageMode listMapPageBySQL(String querySql, int page, int rows);

	public PageMode list4pageorderby(CriteriaQuery cq, int pageNo, int pageSize,
			Map<String, String> map);

	public PageMode list4page(CriteriaQuery cq, int pageNo, int pageSize,
			Map<String, String> map);
	
	public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult);

	public <T> List<T> findByDetached(DetachedCriteria dc);
}
