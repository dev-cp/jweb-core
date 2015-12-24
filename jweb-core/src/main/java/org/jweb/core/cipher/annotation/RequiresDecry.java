package org.jweb.core.cipher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段解密注解，通过标注该注解，告诉解密spring aop增强需要为当前字段值进行解密
 * 解密手段由拦截器决定
 * @author wupan
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequiresDecry {

}
