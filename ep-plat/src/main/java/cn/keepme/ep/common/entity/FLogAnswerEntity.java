package cn.keepme.ep.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 用户考试行为
 * @author onlineGenerator
 * @date 2015-12-08 18:17:36
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_log_answer", schema = "")
@SuppressWarnings("serial")
public class FLogAnswerEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**事件类型*/
	private java.lang.Integer type;
	/**考试者*/
	private java.lang.Integer userQuizId;
	/**试卷*/
	private java.lang.Integer quizId;
	/**试卷试题*/
	private java.lang.Integer questionId;
	/**考试者答案*/
	private java.lang.String msg;
	/**创建时间*/
	private java.util.Date createDatetime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=10)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  事件类型
	 */
	@Column(name ="TYPE",nullable=false,length=3)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  事件类型
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  考试者
	 */
	@Column(name ="USER_QUIZ_ID",nullable=false,length=10)
	public java.lang.Integer getUserQuizId(){
		return this.userQuizId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  考试者
	 */
	public void setUserQuizId(java.lang.Integer userQuizId){
		this.userQuizId = userQuizId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试卷
	 */
	@Column(name ="QUIZ_ID",nullable=false,length=10)
	public java.lang.Integer getQuizId(){
		return this.quizId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试卷
	 */
	public void setQuizId(java.lang.Integer quizId){
		this.quizId = quizId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试卷试题
	 */
	@Column(name ="QUESTION_ID",nullable=false,length=10)
	public java.lang.Integer getQuestionId(){
		return this.questionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试卷试题
	 */
	public void setQuestionId(java.lang.Integer questionId){
		this.questionId = questionId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  考试者答案
	 */
	@Column(name ="MSG",nullable=false,length=5000)
	public java.lang.String getMsg(){
		return this.msg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  考试者答案
	 */
	public void setMsg(java.lang.String msg){
		this.msg = msg;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_DATETIME",nullable=false)
	public java.util.Date getCreateDatetime(){
		return this.createDatetime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDatetime(java.util.Date createDatetime){
		this.createDatetime = createDatetime;
	}
}
