package org.jweb.core.dao.util;

import java.text.MessageFormat;

import org.jweb.core.util.ResourceUtil;

public class DaoUtil {
	/**
	 * 数据库类型
	 */
	public static final String DATABSE_TYPE_MYSQL ="mysql";
	public static final String DATABSE_TYPE_POSTGRE ="postgresql";
	public static final String DATABSE_TYPE_ORACLE ="oracle";
	public static final String DATABSE_TYPE_SQLSERVER ="sqlserver";
	public static final String DATABSE_TYPE_SQLSERVER2000 ="sqlserver2000";
	/**
	 * 分页SQL
	 */
	public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
	public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";//postgresql
	public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; //oracle
	public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver
	public static final String SQLSERVER_SQL2000 = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver
	
	/**
	 * 按照数据库类型，封装SQL
	 */
	public static String createPageSql(String sql, int page, int rows){
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum+"";
		sqlParam[2] = rows+"";
		if(ResourceUtil.getJdbcUrl().indexOf(DATABSE_TYPE_MYSQL)!=-1){
			sql = MessageFormat.format(MYSQL_SQL, sqlParam);
		}else if(ResourceUtil.getJdbcUrl().indexOf(DATABSE_TYPE_POSTGRE)!=-1){
			sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
		}else {
			int beginIndex = (page-1)*rows;
			int endIndex = beginIndex+rows;
			sqlParam[2] = Integer.toString(beginIndex);
			sqlParam[1] = Integer.toString(endIndex);
			if(ResourceUtil.getJdbcUrl().indexOf(DATABSE_TYPE_ORACLE)!=-1) {
				sql = MessageFormat.format(ORACLE_SQL, sqlParam);
			} else if(ResourceUtil.getJdbcUrl().indexOf(DATABSE_TYPE_SQLSERVER)!=-1) {
				sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
				sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
			}
		}
		return sql;
	}
	private static int getAfterSelectInsertPoint(String sql) {
	    int selectIndex = sql.toLowerCase().indexOf("select");
	    int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
	    return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }
}
