package org.jweb.core.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jweb.core.query.constant.RegionPattern;

/**
 * 查询字段范围结束注解，当一个数据库查询示例模型从页面封装到查询条件数据并提交到服务器后，
 * 服务器可以根据查询form表单模型中的注解信息将form表单模型转换成数据库能够识别的查询条件
 * 模型
 * 
 * 该注解指定被修饰的字段作为某个待查字段的开始范围条件，例如，查询age在20-30之间的记录，
 * 页面需要提交一个age下限值，用ageEnd字段表示，我们用@End注解标注ageEnd字段后，服务器
 * 就会根据@End注解自动封装一个ageEnd >= age 的条件查询出来。
 * @author wupan
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface End {

	/**关联字段名，这里全指javaBean模型的数据名，而不是数据库字段名*/
	String value() ;
	/**区间模式，指定是否包含起始值，与数学中坐标区间概念一样*/
	RegionPattern regionPattern() default RegionPattern.CLOSEREGION;
}
