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
 * @Description: 试卷试题
 * @author onlineGenerator
 * @date 2015-12-03 18:06:56
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_answer", schema = "")
@SuppressWarnings("serial")
public class FAnswerEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**试题在试卷中的序号*/
	private java.lang.Integer sort;
	/**试卷id*/
	private java.lang.Integer quizId;
	/**试题id*/
	private java.lang.Integer questionId;
	/**答题人答案内容*/
	private java.lang.String answer;
	
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
	 *@return: java.lang.Integer  试题在试卷中的序号
	 */
	@Column(name ="SORT",nullable=false,length=3)
	public java.lang.Integer getSort(){
		return this.sort;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试题在试卷中的序号
	 */
	public void setSort(java.lang.Integer sort){
		this.sort = sort;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试卷id
	 */
	@Column(name ="QUIZ_ID",nullable=false,length=10)
	public java.lang.Integer getQuizId(){
		return this.quizId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试卷id
	 */
	public void setQuizId(java.lang.Integer quizId){
		this.quizId = quizId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试题id
	 */
	@Column(name ="QUESTION_ID",nullable=false,length=10)
	public java.lang.Integer getQuestionId(){
		return this.questionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试题id
	 */
	public void setQuestionId(java.lang.Integer questionId){
		this.questionId = questionId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  答题人答案内容
	 */
	@Column(name ="ANSWER",nullable=true,length=5000)
	public java.lang.String getAnswer(){
		return this.answer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  答题人答案内容
	 */
	public void setAnswer(java.lang.String answer){
		this.answer = answer;
	}
}
