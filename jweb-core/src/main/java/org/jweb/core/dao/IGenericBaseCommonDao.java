package org.jweb.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jweb.core.bean.DBTable;
import org.jweb.core.bean.PageMode;
import org.springframework.dao.DataAccessException;

/**
 * 该接口不特定于具体持久化框架
 * @author wupan
 *
 */
@SuppressWarnings("rawtypes")
public interface IGenericBaseCommonDao{
	
	public List<DBTable> getAllDbTableName();

	public Integer getAllDbTableSize();
	
	
	/**
	 * 保存实体
	 * @param o
	 * @return 返回保存后的实体id
	 */
	public Object save(Object o) ;
	
	/**
	 * 更某实体对象
	 * @param o
	 * @return
	 */
	public boolean update(Object o) ;
	
	public <T> void saveOrUpdate(T entity);
	
	/**
	 * 批量保存
	 * @param lt 待保存数据集合
	 */
	public void batchSave(List lt) ;
	
	/**
	 * 批量更新
	 * @param lt 待更新的数据集合
	 */
	public void batchUpdate(List lt) ;

	
	/**
	 * 删除实体
	 * @param c 实体类型
	 * @param i 实体的主键id
	 * @return 成功返回true
	 */
	public boolean delete(java.lang.Class c, Serializable i) ;

	/**
	 * 批量删除实体
	 * @param c 实体类型
	 * @param dks 待删除的实体id数组
	 * @return
	 */
	public boolean delete(Class c, Serializable[] dks) ;
	
	/**
	 * 批量删除
	 * @param collection
	 * @return
	 */
	public boolean batchDelete(Collection collection);
	
	/**
	 * 删除某实体对象
	 * @param o
	 * @return
	 */
	public boolean delete(Object o) ;

	/**
	 * 获取某个类型实体的总数，注意，假删除数据也会被计数
	 * @param c 实体类型
	 * @return
	 */
	public int getTotal(Class c) ;

	/**
	 * 获取某个条件下的实体总数
	 * @param c
	 * @param example 条件模型
	 * @return
	 */
	public int getTotal(Class c, Object example) ;
	
	/**
	 * 根据id 查询实体类对象
	 * @param c
	 * @param i
	 * @return
	 */
	public Object get(Class c, Serializable i) ;
	

	/**
	 * 获取某一实体类型的全部数据集合
	 * @param c
	 * @return
	 */
	public List list(Class c) ;


	/**
	 * 根据实体类型分页获取实体数据集合
	 * @param c
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List list(Class c, int pageNo, int pageSize) ;
	
	/**
	 * 根据实体类型分页获取实体数据集合
	 * @param c
	 * @param pageNo
	 * @param pageSize
	 * @return 返回分页模型
	 */
	public PageMode list4page(Class c, int pageNo, int pageSize) ;

	/**
	 * 根据实体类型和hql的条件子句查询实体数据集合
	 * @param c
	 * @param wql 以and开头的hql条件子句
	 * @return
	 */
	public List list(Class c, Object example) ;

	/**
	 * 根据实体类型、hql条件子句、分页信息查询实体数据集合
	 * @param c
	 * @param wql 以and开头的hql条件子句
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List list(Class c, Object example, int pageNo, int pageSize) ;
	
	/**
	 * 根据实体类型、hql条件子句、分页信息查询实体数据集合
	 * @param c
	 * @param example
	 * @param pageNo
	 * @param pageSize
	 * @return 返回分页模型
	 */
	public PageMode list4page(Class c, Object example, int pageNo, int pageSize) ;
	
	/**
	 * 根据实体类型、hql条件子句、分页信息查询实体数据集合
	 * 该方法允许查询某个字段在某个范围下的记录，例如，查询20-30岁之间的人，rangeMap中应给有age_begin=20和age_end=30这
	 * 两组键值对，其中age是与example中age字段名对应，"_begin"和"_end"是通过代码拼接上去的
	 * @param c 
	 * @param example
	 * @param rangeMap 范围查询字段定义集合
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageMode list4page(Class c, Object example,Map<String,String[]> rangeMap, int pageNo, int pageSize) ;


	/**
	 * 根据指定参数获取唯一实体对象
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass,String propertyName, Object value);
	
	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass,String propertyName, Object value);

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> loadAll(final Class<T> entityClass);
	
	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql);
	
	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass,String propertyName, Object value, boolean isAsc);
	
	/**
	 * 通过hql查询唯一对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> T singleResult(String hql);
	
	public List findByExample(final String entityName,final Object exampleEntity);
	
	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, List<Object> param);
	
	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, Object... param);
	
	/**
	 * 执行SQL 使用:name占位符
	 */
	public Integer executeSql(String sql, Map<String, Object> param);
	
	/**
	 * 执行SQL 使用:name占位符,并返回插入的主键值
	 */
	public Object executeSqlReturnKey(String sql, Map<String, Object> param);
	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public Map<String, Object> findOneForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long getCountForJdbc(String sql);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	public Long getCountForJdbcParam(String sql, Object[] objs);
}