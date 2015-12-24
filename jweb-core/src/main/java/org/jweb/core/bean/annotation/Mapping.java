package org.jweb.core.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @Title: Mapping.java 
* @Package org.jweb.common.annotation.mode 
* @Description: 配置form、vo数据模型字段到entity模型字段的映射关系
* @author wupan  
* @date 2014年10月19日 下午3:55:19 
* @version V1.0
 */
@Target({ ElementType.TYPE,java.lang.annotation.ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Mapping{
	String value() default "";
	Class entityClass() default Object.class;//对应的实体类
	String dateFormat() default "yyyy-MM-dd HH:mm:ss";//时间格式模板，当实体类时间转换到字符串时用到
}
