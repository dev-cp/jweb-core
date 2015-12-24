package org.jweb.core.permit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限验证注解，用于通过spring aop进行cxf服务方法的访问权限验证
 * @author wupan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permit {
	String value() default "*:*:*";
}
