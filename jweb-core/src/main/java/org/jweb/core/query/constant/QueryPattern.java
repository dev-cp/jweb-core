package org.jweb.core.query.constant;

/**
 * 查询策略枚举常量，配合QueryStrategy注解使用，用于标志字段采取什么样的模糊方式
 * @author wupan
 *
 */
public enum QueryPattern {

	//查询策略
	BEFOREVAGUE,//前置模糊查询
	ENDVAGUE,//后置模糊查询
	FULLVAGUE;//全模糊查询
	
}
