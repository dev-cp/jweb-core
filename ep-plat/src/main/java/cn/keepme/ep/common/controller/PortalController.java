package cn.keepme.ep.common.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jweb.core.bean.ReplyDataMode;
import org.jweb.core.util.MyBeanUtils;
import org.jweb.core.util.StringUtil;
import org.jweb.core.web.controller.BaseController;
import org.jweb.core.web.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.keepme.ep.common.bean.ReplyDataModeFetchQuestion;
import cn.keepme.ep.common.bean.ReplyDataModeLogin;
import cn.keepme.ep.common.bean.ReplyStatus;
import cn.keepme.ep.common.entity.FAnswerEntity;
import cn.keepme.ep.common.entity.FLogAnswerEntity;
import cn.keepme.ep.common.entity.FQuestionEntity;
import cn.keepme.ep.common.entity.FQuestionOptionEntity;
import cn.keepme.ep.common.entity.FQuizEntity;
import cn.keepme.ep.common.entity.FTagEntity;
import cn.keepme.ep.common.entity.FUserQuizEntity;
import cn.keepme.ep.common.service.SysServiceI;
import cn.keepme.ep.util.BasicStatus;
import cn.keepme.ep.util.ParamsSign;
import cn.keepme.ep.util.PublicParams;

import com.alibaba.fastjson.JSON;

/**
 * test
接口 0.用户登陆接口
	http://192.168.1.72:8080/ep-plat/portal/userLogin.do?p=6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2
接口 1.题目获取接口
	http://192.168.1.72:8081/ep/portal.do?question&p=6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2&qNumber=1
接口 2.答案提交接口
	http://192.168.1.72:8081/ep/portal.do?answer&p=6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2&aId=1&qType=1&answer=
	http://192.168.1.72:8081/ep/portal.do?answerSubmit&p=6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2&aId=1&qType=1&answer=111
接口 3.记录行为接口	
	http://192.168.1.72:8081/ep/portal.do?log&p=6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2&eType=2&aId=1&qType=1&answer=111
接口 4.文件上传接口
	
接口 5.交卷接口
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/portal")
public class PortalController extends BaseController{

	@Autowired
	private SysServiceI sysService;
	
	/**
	 * 接口 0.用户登陆接口
	 * 接口地址：
	 * @param  	p 		用户密钥	用于识别用户身份
	 *****************************************************************************************
	 * @return 	status 		处理结果	1  成功返回 0 错误	
	 *			nickname	用户昵称 	对应user_quiz表中的nickname
	 * 		 	type 		用户类型 	对应user_quiz表中的type 0：应聘者；1：员工
	 * 			qIdList	 	['1'=>['id'=>'100','answer'=>''],'2'=>['id'=>'100','answer'=>'']]	答题卡列表 
	 * @param request
	 * @return
	 */
//	@RequestMapping(params = "userLogin")
	@RequestMapping(value = "/userLogin")
	@ResponseBody
	public ReplyDataModeLogin mLogin(HttpServletRequest request){
		ReplyDataModeLogin j = new ReplyDataModeLogin();
		StringBuilder resultStr = new StringBuilder();
		resultStr.append("{");
		System.out.println("-URL0 " + request.getRequestURL() + "?"+ request.getQueryString());
		String p = request.getParameter("p");  // 用户密钥
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  参数为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  参数有误
		}
		else{
			String pe[] = p.split("_");
			String userid = pe[1];
			String pEmail = pe[0];  // 密文
			String email = ParamsSign.decode(pEmail, userid);  // 明文
			
			// 认证用户  执行查询hql 查用户email
			String hql = "from FUserQuizEntity where id =" + userid;
			List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);
			
			
			// "1": {"id": 101, "answer": ""},
			
			//Map resultMap = new HashMap();
			//List<Map> mapList = new ArrayList<Map>();
			
			for(FUserQuizEntity e : entityList){
				if(!email.equals(e.getEmail())){
					// 无用户信息
					j.setStatus(BasicStatus._no_user);  // 失败
				}else{
					// 命中用户
					j.setStatus(BasicStatus._result_code_success);
					j.setNickname(e.getNickname());
					j.setType(e.getType().toString());
					
					// 查用户已分配的 试题试卷信息
					// 1、按用户id查试卷信息
					String sql = "SELECT id from f_quiz where is_finish=0 and user_quiz_id ='" + userid+"'";
					List<String> quizList =this.sysService.findListbySql(sql);
					String quizId = "";  // 试卷id
					if(!quizList.isEmpty()){
						for(Object o : quizList){						
							quizId = o.toString();
//							System.out.println(quizId);
						}
						// 2、再按试卷id查试卷试题，并排序
						String hql2 = "from FAnswerEntity where quizId =" + quizId +" order by sort asc";
						List<FAnswerEntity> aList = sysService.findByQueryString(hql2);
						
						for(FAnswerEntity a : aList){
							if(!aList.isEmpty()){
								Integer sort = a.getSort();  // 索引是排序值
								String jsonStr = "%s:{\"id\":\"%s\",\"answer\":\"%s\"}";
								jsonStr = String.format(jsonStr, new Object[] { sort, a.getId(), a.getAnswer()});
								System.out.println("#### jsonStr ="+ jsonStr);
								resultStr.append(jsonStr);
								resultStr.append(",");
							}else{
								System.out.println("#### 找不到试卷试题 333");
							}
						}
					}else{
						System.out.println("#### 找不到试卷 ");
					}
					
					if(resultStr.length() > 0){
						resultStr.deleteCharAt(resultStr.length()-1);
					}
				}
			}
			
			//Object obj = JSON.toJSON(resultMap);
			resultStr.append("}");
			Object obj = JSON.parse(resultStr.toString());
			j.setqIdList(obj);
		}
//		j.setSuccess(true);
		return j;
	}
	
	
	/** 
	 * 接口 1.题目获取接口
	 * 接口地址：
	 *			类型	参数名	解释说明
	 * @param 	string 	p 		用户密钥	用于识别用户身份
	 * @param 	string 	qNumber	要获取的题目信息的编号      '题目的编号 对应 answer 表的id字段'
	 *****************************************************************************************
	 * @return 	string 	data 	返回json数据
	 * 			status 	 		处理结果	1  成功返回 0 错误
	 *			qInfo	array 	详情信息
	 *							id 			题目编号 		对应 answer 表的id字段
	 *							sort 		题目序号		对应 answer 表的sort字段
	 *							title 		题目名称 		对应 question 表的 title字段
	 *							type 		题目类型		对应 question 表的 type字段
	 * 							tagId 		题目标签id 	对应 question 表的 tag_id字段
	 *							tagName		题目标签名称	对应 tag 	  表的 name字段
	 *							answer		题目答案 		对应 answer 	  表的 answer字段
	 *							optionList	题目选项数据，若是简单和上传以及判断则为null 否则格式为	
	 *[['id'=>'120','des'=>'我喜欢红色'],['id'=>'122','des'=>'我喜欢蓝色']]	id 	对应 question_option的id字段  des 对应 question_option的des字段
	 *							_children 	子题目数据，不是题组的则为null即可	否则格式为 
	 *[['id'=>'','sort'=>'','title'=>'','type'=>'','tagName'=>'','answer'=>'','optionList'=>[]],['id'=>'','sort'=>'','title'=>'','type'=>'','tagName'=>'','answer'=>'','optionList'=>[]]]
	 * @param request
	 * @return
	 */
//	@RequestMapping(params = "question")
	@RequestMapping(value = "/question")
	@ResponseBody
	public ReplyDataModeFetchQuestion fetchQuestion(HttpServletRequest request){
		ReplyDataModeFetchQuestion j = new ReplyDataModeFetchQuestion();
		
		System.out.println("-URL1 " + request.getRequestURL() + "?"+ request.getQueryString());
		String p = request.getParameter("p");  // 用户密钥
		String qNumber = request.getParameter("qNumber");  // 要获取的题目信息的编号
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥有误
		}
		else if("".equals(qNumber) || qNumber == null){
			j.setStatus(BasicStatus._no_answer);  // 失败 试卷试题编号有误
		}
		else{
			String pe[] = p.split("_");
			String userid = pe[1];
			String pEmail = pe[0];  // 密文
			String email = ParamsSign.decode(pEmail, userid);  // 明文
			
			// 认证用户  执行查询hql 查用户email
			String hql = "from FUserQuizEntity where id =" + userid;
			List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);
			
			Map resultMap = new HashMap();
			List<Map> optionList = new ArrayList<Map>();

			for(FUserQuizEntity e : entityList){
				if(!email.equals(e.getEmail())){
					// 无用户信息
					j.setStatus(BasicStatus._no_user);  // 失败
				}else{
					// 命中用户
					j.setStatus(BasicStatus._result_code_success);
					
					// 查用户已分配的 试题试卷
					// 1、按qNumber查到试题试卷信息
					String hql2 = "from FAnswerEntity where id =" + qNumber ;
					List<FAnswerEntity> aList = sysService.findByQueryString(hql2);
					if(aList.isEmpty()){
						System.out.println("#### 找不到试卷试题 111");
						j.setStatus(BasicStatus._no_answer);  // 失败
					}
					else{
						for(FAnswerEntity a : aList){
							resultMap.put("id", qNumber);
							resultMap.put("sort", a.getSort());
							resultMap.put("answer", a.getAnswer());
							System.out.println("#### 题目id ="+ a.getQuestionId());
							// 2、查题目信息
							String hqlq = "from FQuestionEntity where id =" + a.getQuestionId();
							List<FQuestionEntity> qList = sysService.findByQueryString(hqlq);
							if(qList.isEmpty()){
								System.out.println("#### 找不到题目试题 222");
								j.setStatus(BasicStatus._no_question);  // 失败
							}else{					
								for(FQuestionEntity q : qList){
									Integer tagId =  q.getTagId();
									Integer type =  q.getType();  // 类型：0单选、1多选、2简答、3上传
									Integer parentId =  q.getParentId();  // 默认0  若为其他值就是儿子
									
									resultMap.put("title", q.getTitle());
									resultMap.put("type", type);
									resultMap.put("tagId", tagId);
									// 3、查题目标签名称	对应 tag 	  表的 name字段
									String tagName = "";
									FTagEntity tag = new FTagEntity();
									tag.setId(tagId);
									if (StringUtil.isNotEmpty(tag.getId())) {
										tag = sysService.getEntity(FTagEntity.class, tag.getId());
										if(tag!=null){
											tagName = tag.getName();
										}else{
											j.setStatus(BasicStatus._no_tag);  // 失败
										}
									}
									resultMap.put("tagName", tagName);
									// 4、查题目选项数据，若是简答题或上传以及判断则为null
									// 4-1、判断题目类型 
//									if(type==0 || type==1){  // 0单选、1多选
									String typeStr =  type.toString();
									if(typeStr.equals(PublicParams.questionType.get("danxuan")) 
										|| typeStr.equals(PublicParams.questionType.get("duoxuan"))){  // 0单选、1多选
									
										String hqlOpt = "from FQuestionOptionEntity where questionId =" + a.getQuestionId();
										List<FQuestionOptionEntity> optList = sysService.findByQueryString(hqlOpt);
										if(optList.isEmpty()){
											System.out.println("#### 找不到题目选项");
											j.setStatus(BasicStatus._no_question_option);  // 失败
										}else{
											// 4-2、查题目选项数据
											for(FQuestionOptionEntity opt : optList){
												Map map =new HashMap();
												map.put("id", opt.getId());
												map.put("des", opt.getDes());
												optionList.add(map);
												resultMap.put("optionList", optionList);  // 题目选项数据
											}
										}
										
									}else{  // 其他类型题目没有Option
										resultMap.put("optionList", null);
									}
									
									/**
									 * "_children": [
							            {
							                "id": "101",
							                "sort": "1",
							                "title": "题目标题",
							                "type": "题目类型",
							                "tagName": "题目名称",
							                "answer": [{"id": 201,"des": "选项描述内容1"},{"id": 202,"des": "选项描述内容2"} ]},
									 */
									// 5、判断题目是否有父亲  
									if(parentId!=0){
										resultMap.remove("optionList");
										resultMap.put("_children", resultMap);
									}
									
									
								}
							
							}
					
						}
					}
				}

			}
			
			Object obj = JSON.toJSON(resultMap);
			j.setqInfo(obj);
		}
		/*
		json数组如下所示
		 * {"status":1,"qInfo":{"id":"101","sort":"1","title":"\u9898\u76ee\u6807\u9898","type":"\u9898\u76ee\u7c7b\u578b","tagName":"\u9898\u76ee\u540d\u79f0","answer":null,"
		 * optionList":[{"id":201,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb91"},{"id":202,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb92"},
		 * {"id":203,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb93"},
		 * {"id":204,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb94"}],
		 * "_children":[{"id":"101","sort":"1","title":"\u9898\u76ee\u6807\u9898","type":"\u9898\u76ee\u7c7b\u578b","tagName":"\u9898\u76ee\u540d\u79f0","answer":[{"id":201,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb91"},{"id":202,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb92"},{"id":203,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb93"},{"id":204,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb94"}]},{"id":"101","sort":"2","title":"\u9898\u76ee\u6807\u9898","type":"\u9898\u76ee\u7c7b\u578b","tagName":"\u9898\u76ee\u540d\u79f0","answer":[{"id":201,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb91"},{"id":202,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb92"},{"id":203,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb93"},{"id":204,"des":"\u9009\u9879\u63cf\u8ff0\u5185\u5bb94"}]}]}}
		*/
		return j;
	}
	
	
	/**
	 * 接口 2.答案提交接口 	【可能会定时保存答案信息】
	 * 接口地址：
	 * 			类型	参数名			解释说明
	 * @param 	string 	p 			用户密钥	用于识别用户身份
	 * @param 	string 	aId 		当前应答题目的编号
	 * @param 	string 	qType		当前应答题目的类型
	 * @param 	string 	answer 		当前应答题目用户输入的答案	
	 * 									判断：0 否 或 1 是; 
	 * 									单选：200选项编号; 
	 * 									多选：[201,202]数组格式; 简答题目：字符串; 
	 * 									上传题目：['test.php','index.php']数组格式
	 * @return {"status":"1"}		0成功  其他失败
	 */
//	@RequestMapping(params = "answerSubmit")
	@RequestMapping(value = "/answerSubmit")
	@ResponseBody
	public ReplyStatus answerSubmit(HttpServletRequest request){
		ReplyStatus j = new ReplyStatus();
		
		System.out.println("-URL2 " + request.getRequestURL() + "?"+ request.getQueryString());
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		String p = request.getParameter("p");  			// 用户密钥	用于识别用户身份
		String aId = request.getParameter("aId");  		// 当前应答题目的编号
		String qType = request.getParameter("qType");  	// 当前应答题目的类型
		String answer = request.getParameter("answer"); // 当前应答题目用户输入的答案
		// request.getParameter("answer") @RequestMapping(params = "answer") 名字不能一样 否则找不到request.getParameter("answer")
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥有误
		}
		else if("".equals(aId) || aId == null){
			j.setStatus(BasicStatus._no_answer);  // 失败 试卷试题编号有误
		}
		else if("".equals(qType) || qType == null){
			j.setStatus(BasicStatus._no_question_type);  // 失败 题目的类型有误
		}
		else if(answer == null){
			j.setStatus(BasicStatus._answer_is_null);  // 失败 用户输入的答案为null
		}
		else if("".equals(answer)){
			System.out.println("#### answer=\"\" 用户没有输入答案，直接返回成功，不做任何处理");
			j.setStatus(BasicStatus._result_code_success);  // 用户没有输入答案，直接返回成功，不做任何处理
		}
		else{
			String pe[] = p.split("_");
			String userid = pe[1];
			String pEmail = pe[0];  // 密文
			String email = ParamsSign.decode(pEmail, userid);  // 明文
			
			try {
				// 认证用户  执行查询hql 查用户email
				String hql = "from FUserQuizEntity where id =" + userid;
				List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);
				for(FUserQuizEntity e : entityList){
					if(!email.equals(e.getEmail())){
						// 无用户信息
						j.setStatus(BasicStatus._no_user);  // 失败
					}else{
						// 命中用户
						j.setStatus(BasicStatus._result_code_success);
						
						// 查用户已分配的 试题试卷
						// 1、按qNumber查到试题试卷信息
	//					String hql2 = "from FAnswerEntity where id =" + aId ;
	//					List<FAnswerEntity> aList = sysService.findByQueryString(hql2);
						Integer answerId = Integer.parseInt(aId);
						FAnswerEntity fAnswer = new FAnswerEntity();
						fAnswer.setId(answerId);
						FAnswerEntity t = sysService.get(FAnswerEntity.class, fAnswer.getId());
						if(t==null){
							System.out.println("#### 找不到试卷试题 111");
							j.setStatus(BasicStatus._no_answer);  // 失败
						}
						else{
							MyBeanUtils.copyBeanNotNull2Bean(fAnswer, t);
							/** 
							 * 单选：200选项编号; 
							 * 多选：[201,202]数组格式;
							 * 简答题目：字符串; 
							 * 上传题目：['test.php','index.php']数组格式
							 * **/
							// 更新试卷试题答案  根据qType分析答案的类型 做相应保存
							System.out.println("#### answer :"+answer);
							String typeStr =  qType.toString();
							System.out.println("#### 题目类型 : " + typeStr);
							if(typeStr.equals(PublicParams.questionType.get("danxuan"))){
								// 单选  存传过来的选项编号
								t.setAnswer(answer);
								sysService.saveOrUpdate(t);
							}
							else if(typeStr.equals(PublicParams.questionType.get("duoxuan"))){
								// 多选 存传过来的选项编号数组 转成JSON
								String answerJson = JSON.toJSON(answer).toString();
								t.setAnswer(answerJson);
								sysService.saveOrUpdate(t);
							}
							else if(typeStr.equals(PublicParams.questionType.get("jianda"))){
								// 简答  直接存答案
								t.setAnswer(answer);
								sysService.saveOrUpdate(t);
							}
							else if(typeStr.equals(PublicParams.questionType.get("shangchuan"))){
								// 上传  存传过来的数组 转成JSON
								String answerJson = JSON.toJSON(answer).toString();
								t.setAnswer(answerJson);
								sysService.saveOrUpdate(t);
							}
							else{
								
							}
						}
					}
	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}
		return j;
	}
	
	
	/**
	 * 接口 3.记录行为接口	
	 * 接口地址：
	 * 事件类型:0 无、1 上一题、2 下一题、3 点击答题卡、4 点击答题卡中的题号、5 交卷、6 更改选项、7 更新简答框值、8 上传
	 * @param 	p 			用户密钥	用于识别用户身份
	 * @param 	eType 		事件类型
	 * @param 	qType		当前应答题目的类型
	 * @param 	aId 		当前题目编号
	 * @param 	answer		当前应答题目用户输入的答案	判断：0 否 或 1 是; 单选：200选项编号; 多选：[201,202]数组格式; 简答题目：字符串; 上传题目：['test.php','index.php']数组格式
	 * @return {"status":"1"}			1成功 0失败
	 */
//	@RequestMapping(params = "log")
	@RequestMapping(value = "/log")
	@ResponseBody
	public ReplyStatus log(HttpServletRequest request){
		ReplyStatus j = new ReplyStatus();
		
		System.out.println("-URL3 " + request.getRequestURL() + "?"+ request.getQueryString());
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		String p = request.getParameter("p");  			// 用户密钥	用于识别用户身份
		String eType = request.getParameter("eType");  	// 事件类型 
		String qType = request.getParameter("qType");  	// 当前应答题目的类型
		String aId = request.getParameter("aId");  		// 当前题目编号
		String answer = request.getParameter("answer"); // 当前应答题目用户输入的答案
		// request.getParameter("answer") @RequestMapping(params = "answer") 名字不能一样 否则找不到request.getParameter("answer")
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥有误
		}
		else if("".equals(aId) || aId == null){
			j.setStatus(BasicStatus._no_answer);  // 失败 试卷试题编号有误
		}
		else if("".equals(eType) || eType == null){
			j.setStatus(BasicStatus._no_event_type);  // 失败 无事件类型
		}
		else if("".equals(qType) || qType == null){
			j.setStatus(BasicStatus._no_question_type);  // 失败 题目的类型有误
		}
		else if(answer == null){
			j.setStatus(BasicStatus._answer_is_null);  // 失败 用户输入的答案为null
		}
		else if("".equals(answer)){
			System.out.println("#### answer=\"\" 用户没有输入答案，直接返回成功，不做任何处理");
			j.setStatus(BasicStatus._result_code_success);  // 用户没有输入答案，直接返回成功，不做任何处理
		}
		else{
			String pe[] = p.split("_");
			String userid = pe[1];
			String pEmail = pe[0];  // 密文
			String email = ParamsSign.decode(pEmail, userid);  // 明文
			
			try {
				// 认证用户  执行查询hql 查用户email
				String hql = "from FUserQuizEntity where id =" + userid;
				List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);
				for(FUserQuizEntity e : entityList){
					if(!email.equals(e.getEmail())){
						// 无用户信息
						j.setStatus(BasicStatus._no_user);  // 失败
					}else{
						// 命中用户
						j.setStatus(BasicStatus._result_code_success);
						
						// 查用户已分配的 试题试卷
						// 1、按qNumber查到试题试卷信息
						Integer answerId = Integer.parseInt(aId);
						FAnswerEntity fAnswer = new FAnswerEntity();
						fAnswer.setId(answerId);
						FAnswerEntity t = sysService.get(FAnswerEntity.class, fAnswer.getId());
						if(t==null){
							System.out.println("#### 找不到试卷试题 111");
							j.setStatus(BasicStatus._no_answer);  // 失败
						}
						else{
							MyBeanUtils.copyBeanNotNull2Bean(fAnswer, t);
							FLogAnswerEntity fLogAnswer = new FLogAnswerEntity();
							fLogAnswer.setQuestionId(t.getQuestionId());
							fLogAnswer.setQuizId(t.getQuizId());
							fLogAnswer.setType(Integer.parseInt(qType));
							fLogAnswer.setUserQuizId(Integer.parseInt(userid));
							fLogAnswer.setCreateDatetime(new Date());
							/** 
							 * 单选：200选项编号; 
							 * 多选：[201,202]数组格式;
							 * 简答题目：字符串; 
							 * 上传题目：['test.php','index.php']数组格式
							 * **/
							// 更新试卷试题答案  根据qType分析答案的类型 做相应保存
							System.out.println("#### answer :"+answer);
							String typeStr =  qType.toString();
							System.out.println("#### 题目类型 : " + typeStr);
							
							if(typeStr.equals(PublicParams.questionType.get("danxuan"))){
								// 单选  存传过来的选项编号
								fLogAnswer.setMsg(answer);
								sysService.save(fLogAnswer);
							}
							else if(typeStr.equals(PublicParams.questionType.get("duoxuan"))){
								// 多选 存传过来的选项编号数组 转成JSON
								String answerJson = JSON.toJSON(answer).toString();
								fLogAnswer.setMsg(answerJson);
								sysService.save(fLogAnswer);
							}
							else if(typeStr.equals(PublicParams.questionType.get("jianda"))){
								// 简答  直接存答案
								fLogAnswer.setMsg(answer);
								sysService.save(fLogAnswer);
							}
							else if(typeStr.equals(PublicParams.questionType.get("shangchuan"))){
								// 上传  存传过来的数组 转成JSON
								String answerJson = JSON.toJSON(answer).toString();
								fLogAnswer.setMsg(answerJson);
								sysService.save(fLogAnswer);
							}
							else{
								
							}
						}
					}
	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}
		return j;
	}
	
	
	
	/**
	 * 接口 4.文件上传接口
	 * 接口地址：
	 * 事件类型:0 无、1 上一题、2 下一题、3 点击答题卡、4 点击答题卡中的题号、5 交卷、6 更改选项、7 更新简答框值、8 上传
	 * @param 	p 			用户密钥	用于识别用户身份
	 * @param 	eType 		事件类型
	 * @param 	qType		当前应答题目的类型
	 * @param 	aId 		当前题目编号
	 * @param 	answer		当前应答题目用户输入的答案	判断：0 否 或 1 是; 单选：200选项编号; 多选：[201,202]数组格式; 简答题目：字符串; 上传题目：['test.php','index.php']数组格式
	 * @return {"status":"1"}			1成功 0失败
	 */
//	@RequestMapping(params = "upload")
	@RequestMapping(value = "/upload")
	@ResponseBody
	public ReplyStatus upload(HttpServletRequest request){
		ReplyStatus j = new ReplyStatus();
		
		System.out.println("-URL4 " + request.getRequestURL() + "?"+ request.getQueryString());
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		String p = request.getParameter("p");  			// 用户密钥	用于识别用户身份
		String eType = request.getParameter("eType");  	// 事件类型 
		String qType = request.getParameter("qType");  	// 当前应答题目的类型
		String aId = request.getParameter("aId");  		// 当前题目编号
		String answer = request.getParameter("answer"); // 当前应答题目用户输入的答案
		// request.getParameter("answer") @RequestMapping(params = "answer") 名字不能一样 否则找不到request.getParameter("answer")
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥有误
		}
		else if("".equals(aId) || aId == null){
			j.setStatus(BasicStatus._no_answer);  // 失败 试卷试题编号有误
		}
		else if("".equals(eType) || eType == null){
			j.setStatus(BasicStatus._no_event_type);  // 失败 无事件类型
		}
		else if("".equals(qType) || qType == null){
			j.setStatus(BasicStatus._no_question_type);  // 失败 题目的类型有误
		}
		else if(answer == null){
			j.setStatus(BasicStatus._answer_is_null);  // 失败 用户输入的答案为null
		}
		else if("".equals(answer)){
			System.out.println("#### answer=\"\" 用户没有输入答案，直接返回成功，不做任何处理");
			j.setStatus(BasicStatus._result_code_success);  // 用户没有输入答案，直接返回成功，不做任何处理
		}
		else{
			
		}
		return j;
	}
	
	/**
	 * 接口 5.交卷接口
	 * @param 	p 			用户密钥	用于识别用户身份
	 * @return {"status":"1"}			1成功 0失败
	 */
//	@RequestMapping(params = "quiz")
	@RequestMapping(value = "/quiz")
	@ResponseBody
	public ReplyStatus quiz(HttpServletRequest request){
		ReplyStatus j = new ReplyStatus();
		
		System.out.println("-URL5 " + request.getRequestURL() + "?"+ request.getQueryString());
//		String p = "6addc316e649955dd9f712f2491f846e9bf1ab766262c9c1_2";  // zhangsan@163.com
		String p = request.getParameter("p");  			// 用户密钥	用于识别用户身份
		
		// 参数校验
		if("".equals(p) || p == null){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥为空
		}
		else if(!p.contains("_")){
			j.setStatus(BasicStatus._no_user);  // 失败  用户密钥有误
		}
		else{
			String pe[] = p.split("_");
			String userid = pe[1];
			String pEmail = pe[0];  // 密文
			String email = ParamsSign.decode(pEmail, userid);  // 明文
			
			try {
				// 认证用户  执行查询hql 查用户email
				String hql = "from FUserQuizEntity where id =" + userid;
				List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);
				for(FUserQuizEntity e : entityList){
					if(!email.equals(e.getEmail())){
						// 无用户信息
						j.setStatus(BasicStatus._no_user);  // 失败
					}else{
						// 命中用户
					
						// 1、根据 用户id 和 是否完成  两个参数 user_quiz_id和 is_finish 匹配唯一试卷
						// (有可能不唯一，给用户分配多套试卷，再按is_finish查出这个用户未完成的试卷)
						String sql = "SELECT id from f_quiz where is_finish=0 and user_quiz_id ='" + userid+"'";
						List<String> quizList =this.sysService.findListbySql(sql);
						String quizId = "";  // 试卷id
						if(!quizList.isEmpty()){
							for(Object o : quizList){						
								quizId = o.toString();  // 取出该用户第一个 未完成的 试卷 ID
//								System.out.println(quizId);
							}
							
							// 更新考试人员信息
							// 更新试卷信息							
							FQuizEntity fQuiz = new FQuizEntity();
							fQuiz.setId(Integer.parseInt(quizId));
							FQuizEntity t = sysService.get(FQuizEntity.class, fQuiz.getId());
		
							MyBeanUtils.copyBeanNotNull2Bean(fQuiz, t);
							t.setIsFinish(1); // 完成 1
							t.setFinishDatetime(new Date());  // 完成时间
							sysService.saveOrUpdate(t);
							// ############ 更新完成  返回成功 0 ###########
							j.setStatus(BasicStatus._result_code_success);
						}else{
							System.out.println("#### 找不到试卷 ");
						}
					}
	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}
		return j;
	}
	
	
	/**
	 * json demo
	 * @param req
	 * @return
	 */
//	@RequestMapping(params = "test")
	@RequestMapping(value = "/test")
	@ResponseBody
	public ReplyDataMode tradeHList(HttpServletRequest req){
		ReplyDataMode j = new ReplyDataMode();
		
		// 执行查询 hql 查所有交易所
		String hql = "from FUserQuizEntity";
		List<FUserQuizEntity> entityList = sysService.findByQueryString(hql);

		List<Map> mapList = new ArrayList<Map>();
		Map map = null;
		String url = "1234";
		for(FUserQuizEntity e : entityList){
			map=new HashMap();
			map.put("text", e.getNickname());
			map.put("href", url);
			mapList.add(map);
		}
		Map resultMap = new HashMap();
		resultMap.put("list", mapList);
		Object obj = JSON.toJSON(resultMap);
		
		j.setData(obj);
		j.setSuccess(true);
		return j;
	}
}
