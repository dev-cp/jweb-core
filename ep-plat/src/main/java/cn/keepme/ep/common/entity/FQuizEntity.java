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
 * @Description: 试卷信息
 * @author onlineGenerator
 * @date 2015-12-03 18:27:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_quiz", schema = "")
@SuppressWarnings("serial")
public class FQuizEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**考试者id*/
	private java.lang.Integer userQuizId;
	/**管理者即判卷人*/
	private java.lang.Integer userAdminId;
	/**试卷得分*/
	private java.lang.Integer score;
	/**试卷评语*/
	private java.lang.String comment;
	/**是否交卷*/
	private java.lang.Integer isFinish;
	/**交卷时间*/
	private java.util.Date finishDatetime;
	
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
	 *@return: java.lang.Integer  考试者id
	 */
	@Column(name ="USER_QUIZ_ID",nullable=false,length=10)
	public java.lang.Integer getUserQuizId(){
		return this.userQuizId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  考试者id
	 */
	public void setUserQuizId(java.lang.Integer userQuizId){
		this.userQuizId = userQuizId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  管理者即判卷人
	 */
	@Column(name ="USER_ADMIN_ID",nullable=true,length=5)
	public java.lang.Integer getUserAdminId(){
		return this.userAdminId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  管理者即判卷人
	 */
	public void setUserAdminId(java.lang.Integer userAdminId){
		this.userAdminId = userAdminId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试卷得分
	 */
	@Column(name ="SCORE",nullable=false,length=3)
	public java.lang.Integer getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试卷得分
	 */
	public void setScore(java.lang.Integer score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  试卷评语
	 */
	@Column(name ="COMMENT",nullable=true,length=500)
	public java.lang.String getComment(){
		return this.comment;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  试卷评语
	 */
	public void setComment(java.lang.String comment){
		this.comment = comment;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否交卷
	 */
	@Column(name ="IS_FINISH",nullable=false,length=2)
	public java.lang.Integer getIsFinish(){
		return this.isFinish;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否交卷
	 */
	public void setIsFinish(java.lang.Integer isFinish){
		this.isFinish = isFinish;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  交卷时间
	 */
	@Column(name ="FINISH_DATETIME",nullable=true)
	public java.util.Date getFinishDatetime(){
		return this.finishDatetime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  交卷时间
	 */
	public void setFinishDatetime(java.util.Date finishDatetime){
		this.finishDatetime = finishDatetime;
	}
}
