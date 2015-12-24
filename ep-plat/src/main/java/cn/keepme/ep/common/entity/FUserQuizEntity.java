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
 * @Description: 考试人员信息
 * @author onlineGenerator
 * @date 2015-12-02 16:19:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_user_quiz", schema = "")
@SuppressWarnings("serial")
public class FUserQuizEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**应聘者*/
	private java.lang.Integer type;
	/**登录账户*/
	private java.lang.String email;
	/**昵称*/
	private java.lang.String nickname;
	/**岗位*/
	private java.lang.Integer job;
	/**创建时间*/
	private java.util.Date createDatetime;
	/**有效日期*/
	private java.util.Date validDate;
	/**激活时间*/
	private java.util.Date activeDatetime;
	/**状态*/
	private java.lang.Integer isActive;
	
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
	 *@return: java.lang.Integer  应聘者
	 */
	@Column(name ="TYPE",nullable=false,length=3)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  应聘者
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  登录账户
	 */
	@Column(name ="EMAIL",nullable=false,length=30)
	public java.lang.String getEmail(){
		return this.email;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  登录账户
	 */
	public void setEmail(java.lang.String email){
		this.email = email;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  昵称
	 */
	@Column(name ="NICKNAME",nullable=false,length=30)
	public java.lang.String getNickname(){
		return this.nickname;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  昵称
	 */
	public void setNickname(java.lang.String nickname){
		this.nickname = nickname;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  岗位
	 */
	@Column(name ="JOB",nullable=false,length=3)
	public java.lang.Integer getJob(){
		return this.job;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  岗位
	 */
	public void setJob(java.lang.Integer job){
		this.job = job;
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  有效日期
	 */
	@Column(name ="VALID_DATE",nullable=false)
	public java.util.Date getValidDate(){
		return this.validDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  有效日期
	 */
	public void setValidDate(java.util.Date validDate){
		this.validDate = validDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  激活时间
	 */
	@Column(name ="ACTIVE_DATETIME",nullable=false)
	public java.util.Date getActiveDatetime(){
		return this.activeDatetime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  激活时间
	 */
	public void setActiveDatetime(java.util.Date activeDatetime){
		this.activeDatetime = activeDatetime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="IS_ACTIVE",nullable=false,length=3)
	public java.lang.Integer getIsActive(){
		return this.isActive;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setIsActive(java.lang.Integer isActive){
		this.isActive = isActive;
	}
}
