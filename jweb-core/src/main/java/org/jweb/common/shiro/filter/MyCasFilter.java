package org.jweb.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cas.CasFilter;

public class MyCasFilter extends CasFilter{

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		boolean fag = super.onAccessDenied(request, response);
		
		HttpServletRequest req = (HttpServletRequest)request;
		System.out.println(req.getSession().getId());
		return fag;
	}

	
}
