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
 * @Description: 试题标签
 * @author onlineGenerator
 * @date 2015-12-04 09:48:00
 * @version V1.0   
 *
 */
@Entity
@Table(name = "f_tag", schema = "")
@SuppressWarnings("serial")
public class FTagEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**标签名称*/
	private java.lang.String name;
	/**是否激活*/
	private java.lang.Integer isActive;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=3)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标签名称
	 */
	@Column(name ="NAME",nullable=true,length=500)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标签名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
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
}
