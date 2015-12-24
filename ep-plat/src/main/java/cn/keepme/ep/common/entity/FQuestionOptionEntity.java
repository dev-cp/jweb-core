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
 * @Description: 试题选项
 * @author onlineGenerator
 * @date 2015-12-03 19:12:51
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_question_option", schema = "")
@SuppressWarnings("serial")
public class FQuestionOptionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**试题id*/
	private java.lang.Integer questionId;
	/**选项描述*/
	private java.lang.String des;
	
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
	 *@return: java.lang.String  选项描述
	 */
	@Column(name ="DES",nullable=true,length=2000)
	public java.lang.String getDes(){
		return this.des;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  选项描述
	 */
	public void setDes(java.lang.String des){
		this.des = des;
	}
}
