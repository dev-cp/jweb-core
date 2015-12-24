package org.jweb.common.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.jweb.common.shiro.token.TrustedSSOAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroSSORealm extends AuthorizingRealm{
	
	@Autowired
//	private AccountManager accountManager;
	
	public ShiroSSORealm(){
		super.setCredentialsMatcher(new AllowAllCredentialsMatcher());
		super.setAuthenticationTokenClass(TrustedSSOAuthenticationToken.class);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//获取登录用户名
		String loginName = (String)principals.fromRealm(getName()).iterator().next();
		//根据登录用户名从数据库中查找用户信息
		//...
		
		Set<String> roleNames = new HashSet<String>();
	    Set<String> permissions = new HashSet<String>();
	  //根据用户信息获取用户角色
	    //..
	    //将用户角色名添加到roleNames集合中
	    roleNames.add("admin");
	    permissions.add("*:*:*");//会员查询权限
	    permissions.add("members:query");//会员查询权限
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
	    info.setStringPermissions(permissions);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		TrustedSSOAuthenticationToken token = (TrustedSSOAuthenticationToken)authcToken;
		Object username = token.getPrincipal();
		
		if(username == null || "".equals(username)){
			throw new RuntimeException("username is null");
		}
		
		//因为是在单点登录器上登录的，故这里没有进行数据库用户名密码查询操作，甚至
		//token.getCredentials()方法返回的都是空值
		return new SimpleAuthenticationInfo(token.getPrincipal(),
				token.getCredentials(), getName());
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}
	
	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo(){
		Cache<Object,AuthorizationInfo> cache = getAuthorizationCache();
		if(cache != null){
			for(Object key : cache.keys()){
				cache.remove(key);
			}
		}
	}
}
