package cn.keepme.ep.util;

/**
 * 常量  状态码
 */
public class BasicStatus {

	/**状态码 0表示成功**/
	public static final String _result_code_success = "0";
	/**状态码 失败**/
	public static final String _result_code_fail = "1";
	/**接收参数有误【必须带的参数未带】**/
	public static final String _invalid_parameter = "-1";
	
	/**无用户信息**/
	public static final String _no_user = "-5";
	
	/**无试卷试题，f_question 表id匹配失败**/
	public static final String _no_question = "-11";
	/**无题目类型，f_question 表type匹配失败**/
	public static final String _no_question_type = "-12";
	/**无题目标签，f_tag 表id匹配失败**/
	public static final String _no_tag = "-13";
	/**无题目选项，f_question_option 表id匹配失败**/
	public static final String _no_question_option = "-15";
	
	/**无试卷试题，f_answer 表id匹配失败**/
	public static final String _no_answer = "-20";
	/**无试卷试题答案，f_answer 表id匹配失败**/
	public static final String _answer_is_null = "-21";
	
	/**无事件类型  f_log_answer.type **/
	public static final String _no_event_type = "-30";
}
