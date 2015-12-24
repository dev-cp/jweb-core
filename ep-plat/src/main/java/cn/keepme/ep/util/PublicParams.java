package cn.keepme.ep.util;

import java.util.HashMap;
import java.util.Map;

import org.jweb.core.util.PropertiesUtil;



/**
 * 常量
 */
public class PublicParams {

	public static Map<String,String> questionType = new HashMap<String, String>();
	
	static{
		PublicParams.getQuestionType();
	}
//	PublicParams(){
//		PublicParams.getQuestionType();
//	}
	
	public static Map<String,String> getQuestionType() {
		PropertiesUtil util = new PropertiesUtil("PublicParams.properties");
		questionType.put("danxuan", util.readProperty("question.type.danxuan"));
		questionType.put("duoxuan", util.readProperty("question.type.duoxuan"));
		questionType.put("jianda", util.readProperty("question.type.jianda"));
		questionType.put("shangchuan", util.readProperty("question.type.shangchuan"));
		return questionType;
	}
}
