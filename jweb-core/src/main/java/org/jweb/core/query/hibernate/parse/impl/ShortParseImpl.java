package org.jweb.core.query.hibernate.parse.impl;

import org.jweb.core.query.hibernate.parse.IHqlParse;
import org.jweb.core.query.hibernate.qbc.CriteriaQuery;
import org.jweb.core.query.hibernate.utils.HqlGenerateUtil;

public class ShortParseImpl implements IHqlParse {

	
	public void addCriteria(CriteriaQuery cq, String name, Object value) {
		if (HqlGenerateUtil.isNotEmpty(value))
			cq.eq(name, value);
	}

	
	public void addCriteria(CriteriaQuery cq, String name, Object value,
			String beginValue, String endValue) {
		if (HqlGenerateUtil.isNotEmpty(beginValue)) {
			cq.ge(name, Short.parseShort(beginValue));
		}
		if (HqlGenerateUtil.isNotEmpty(endValue)) {
			cq.le(name, Short.parseShort(endValue));
		}
		if (HqlGenerateUtil.isNotEmpty(value)) {
			cq.eq(name, value);
		}
	}

}
