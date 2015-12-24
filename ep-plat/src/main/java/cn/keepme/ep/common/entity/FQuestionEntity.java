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
 * @Description: 题库
 * @author onlineGenerator
 * @date 2015-12-03 16:34:51
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_question", schema = "")
@SuppressWarnings("serial")
public class FQuestionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**试题类型*/
	private java.lang.Integer type;
	/**试题级别*/
	private java.lang.Integer level;
	/**试题标签*/
	private java.lang.Integer tagId;
	/**父试题*/
	private java.lang.Integer parentId;
	/**题目标题*/
	private java.lang.String title;
	/**试题答案*/
	private java.lang.String answer;
	/**是否激活*/
	private java.lang.Integer isActive;
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
	 *@return: java.lang.Integer  试题类型
	 */
	@Column(name ="TYPE",nullable=false,length=5)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试题类型
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试题级别
	 */
	@Column(name ="LEVEL",nullable=false,length=5)
	public java.lang.Integer getLevel(){
		return this.level;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试题级别
	 */
	public void setLevel(java.lang.Integer level){
		this.level = level;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试题标签
	 */
	@Column(name ="TAG_ID",nullable=false,length=5)
	public java.lang.Integer getTagId(){
		return this.tagId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试题标签
	 */
	public void setTagId(java.lang.Integer tagId){
		this.tagId = tagId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  父试题
	 */
	@Column(name ="PARENT_ID",nullable=false,length=10)
	public java.lang.Integer getParentId(){
		return this.parentId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  父试题
	 */
	public void setParentId(java.lang.Integer parentId){
		this.parentId = parentId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  题目标题
	 */
	@Column(name ="TITLE",nullable=true,length=500)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  题目标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  试题答案
	 */
	@Column(name ="ANSWER",nullable=true,length=2000)
	public java.lang.String getAnswer(){
		return this.answer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  试题答案
	 */
	public void setAnswer(java.lang.String answer){
		this.answer = answer;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否激活
	 */
	@Column(name ="IS_ACTIVE",nullable=false,length=3)
	public java.lang.Integer getIsActive(){
		return this.isActive;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否激活
	 */
	public void setIsActive(java.lang.Integer isActive){
		this.isActive = isActive;
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
