<%@ page language="java" pageEncoding="UTF-8"%>

<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="java.sql.Statement"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<jsp:directive.page import="java.sql.ResultSetMetaData"/>

<jsp:directive.page import="org.jweb.core.web.listener.AppInitListener"/>
<jsp:directive.page import="org.jweb.core.util.StringUtil"/>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
String sql = request.getParameter("sql");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="StyleSheet" href="${pageContext.request.contextPath}/css/common.css" type="text/css" />
  </head>
  <body>
    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  		<tr><td align="left" class="font_blue_14px_bold">当前位置：快捷查询</td></tr>
		<tr><td height="2" bgcolor="#EBEBED"></td></tr>
		<tr><td height="10"></td></tr>
  	</table>
  	<form action="${pageContext.request.contextPath}/u/sql.jsp" method="post">
  	<table width="100%"  align="center" border="0" cellpadding="0" cellspacing="0">
  		<tr>
  			<td align="center">
  				<textarea rows="5" cols="200" id="sql" name="sql" ><%=sql == null ? "" : sql %></textarea><br />
  				<input type="submit" value="查询"/><font color="red">仅适用select, desc语句</font>
  			</td>
  		</tr>
  	</table>
  	</form>
  	<br />
	<%
	if(sql != null && !"".equals(sql)){
		boolean isExecute = sql.trim().toLowerCase().startsWith("select ") || sql.trim().toLowerCase().startsWith("desc ") || sql.trim().toLowerCase().startsWith("update ");
		if(isExecute){
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
			try{
				con = AppInitListener.getDataSource().getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
	    		int columnCount = rsmd.getColumnCount();
	%>
    <table width="80%" align="center" border="1" cellpadding="0" cellspacing="0" id="chx_panel" class="DoublecolorTable">
    	<tr class="bluetrbg">
	    	<%
	    		for(int i = 1;i <= columnCount; i++){
	    		String colName = rsmd.getColumnLabel(i);
	    		colName = StringUtil.isNullValue(colName) ? rsmd.getColumnName(i) : colName;
	    	%>
	    		<th><%=StringUtil.filterNullValue(colName) %></th>
	    	<%}%>
    	</tr>
   		<%while(rs.next()){%>
	   		<tr>
	   			<%for(int i = 1;i <= columnCount; i++){%>
	   				<td align="center"><%=rs.getString(i) %></td>
	   			<%}%>
	   		</tr>
   		<%}%>
    </table>
    <%
	   		}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(st != null){
					st.close();
					st = null;
				}
				if(con != null){
					con.close();
					con = null;
				}
			}
    	}
    }
    %>
  </body>
</html>
