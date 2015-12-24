package org.jweb.core.cipher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段加密注解，通过标注该注解，告诉加密拦截器需要为当前字段值进行加密
 * 加密手段由拦截器决定
 * @author wupan
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequiresEncry {

}
