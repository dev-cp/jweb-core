package org.jweb.common.activeMQ.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 存放通过消息队列异步处理http请求结果的Http响应数据库模型
 * 消息监听器从消息队列中取出一个需要异步请求的http请求后，调用线程池中线程方法，通过异步
 * 请求进行http访问处理，在异步线程处理完成后，将处理结果封装成本模型，并将本模型数据存放到
 * 门户网站的数据库中，前端浏览器用户可以通过发起数据库查询操作，从数据库中获取本模型对应的
 * 结果
 * @author wupan
 *
 */
@Entity
@Table(name = "TISDWEB_ACTIVEMQ_HTTP_RESULT", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class HttpResponseEntity {
	/**实体类id*/
	private String id;
	/**处理结果对应的http请求会话的id*/
	private String sessionId;
	/**登录用户的id*/
	private String userId;
	/**发起http请求用的url*/
	private String url;
	/**http请求的响应结果*/
	private String result;
	/**记录创建时间*/
	private Date createDate;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name ="SESSION_ID",nullable=false,length=50)
	public String getSessionId() {
		return sessionId;
	}

	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name ="USER_ID",nullable=true,length=32)
	public String getUserId() {
		return userId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name ="URL",nullable=false,length=300)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name ="RESULT",nullable=true)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
