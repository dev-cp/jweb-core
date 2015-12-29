<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.jweb.core.web.listener.AppInitListener" %>

<%@ page import="org.jweb.core.util.ResourceUtil" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%!	
//数据库检测
boolean dbCheck(){
	// System.out.println("数据库类型:"+ResourceUtil.getJdbcUrl());
	
	ApplicationContext ctx = AppInitListener.getCtx();
	System.out.println("ctx="+ctx);
	if (ctx==null) {
		return false;
	}
	//System.out.println("####"+AppInitListener.getDataSource());
	
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	try {
		// con = ApplicationContextUtil.getProxoolDataSource().getConnection(); 
		
		con = AppInitListener.getDataSource().getConnection();
		final String sql = "select CURRENT_DATE";
		st = con.createStatement();
		rs = st.executeQuery(sql);
		rs.next();
		rs.getString(1);
		
		return true;
	}  catch (SQLException e) {
		return false;
	} finally {
		try {
			if(st != null)
				st.close();
			st = null;
			if(null != rs)
				rs.close();
			rs = null;
			if(null != con)
				con.close();
			con = null;
		} catch (SQLException e) {
		}
	}
	/* 
	*/
}
%>
<%
	boolean dbFlag = dbCheck();
	/* out.println("MySql: " + (dbFlag ? "SC:=OK" : "SC:=FIELD") + "</br>"); */
	out.println(ResourceUtil.getJdbcUrl()+": " + (dbFlag ? "SC:=OK" : "SC:=FIELD") + "</br>");
%>
