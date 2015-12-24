package org.jweb.core.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jweb.core.query.constant.QueryPattern;

/**
 * 查询策略，通过不同的查询策略参数，来告诉查询引擎，当前注解标注的字段使用的是什么查询策略
 * 查询策略有 前模糊、后模糊、全模糊
 * @author wupan
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface QueryStrategy {

	/**默认全模糊查询*/
	QueryPattern value() default QueryPattern.FULLVAGUE;
	
}
