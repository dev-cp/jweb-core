package org.jweb.core.cipher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解密拦截器、过滤器注解，该注解需要标注在用于加密的拦截器、过滤器类上，同时还
 * 需将拦截器、过滤器作为provider配置到cxf配置文件上，在需要拦截或者过滤的资源类、资源方法
 * 上也需要使用该注解，这样才能让cxf知道什么资源需要什么拦截器、过滤器，所以该注解其实是
 * 拦截目标或过滤目标与拦截器或过滤器之间的纽带
 * @author wupan
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)

public @interface Decryption {

}
