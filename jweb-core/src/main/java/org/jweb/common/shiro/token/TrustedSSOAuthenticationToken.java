package org.jweb.common.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class TrustedSSOAuthenticationToken implements AuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8183860499762494414L;
	
	private String username;
	
	public TrustedSSOAuthenticationToken(){
		
	}
	
	public TrustedSSOAuthenticationToken(String username){
		this.username = username;
	}
	
	@Override
	public Object getPrincipal() {
		
		return this.username;
	}

	@Override
	public Object getCredentials() {
		
		return null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void clear(){
		this.username = null;
	}
	
	public String toString(){
		return "username = " + this.username;
	}
}
