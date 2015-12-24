package org.jweb.core.bean;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/***
 * 返回信息的格式，用于rest 或 webservice服务方法返回统一的数据格式
 * @author Administrator
 * 
 * @param <T>
 */
@XmlType(name = "ReplyDataMode")
public class ReplyDataMode4JAXB implements Serializable {
	private static final long serialVersionUID = 1L;
	/**服务状态码，可以通过状态码表查询返回的状态码对应的值，以便进一步了解服务处理情况*/
	private int statusCode;
	/**服务处理后的结果数据*/
	private Object data = null;
	/**本次服务的相关参数键值对*/
	private Map<String,Object> attribute;
	
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Map<String, Object> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	
}