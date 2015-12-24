package org.jweb.common.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class JZWYCasRealm extends CasRealm{
	    
	    // 获取授权信息
	    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	    	//获取登录用户名
			String loginName = (String)principals.fromRealm(getName()).iterator().next();
			//根据登录用户名从数据库中查找用户信息
			//...
			
	    	Set<String> roleNames = new HashSet<String>();
		    Set<String> permissions = new HashSet<String>();
		    
		    //根据用户信息获取用户角色
		    //..
		    String roleName = "admin";
		    
		    //将用户角色名添加到roleNames集合中
		    roleNames.add(roleName);
		    permissions.add("*:*:*");//会员查询权限
//		    permissions.add("members:query");//会员查询权限
//		    permissions.add("members:regist");//会员注册权限
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		    info.setStringPermissions(permissions);
			return info;
	    }
	    
	    /**
	     * 返回cas服务器地址,一般通过配置文件读取
	     */
	    public String getCasServerUrlPrefix() {
	       return "http://localhost:8081/sso";
	    }
	    
	    /**
	     * 返回客户端处理地址，一般通过配置文件读取
	     */
	    public String getCasService() {
	    	/**实际"http://www.tisdsp.com:8080/tisdspAPI/shiro_cas"这个地址是虚拟的，在项目中根本没有定义该地址对应的处理方法
	    	*该地址只是为了在sso服务器重定向到本系统时，本系统可以根据该地址进行shiro的过滤器处理，在shiro的过滤器中完成对服务票
	    	*的校验
	    	*/
	       return "http://www.xshop.com:8080/xshop/shiro_cas.do";
	    }
}