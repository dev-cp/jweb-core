package cn.keepme.ep.common.bean;

import java.io.Serializable;
import java.util.Map;

/***
 * 返回信息的格式，用于rest 或 webservice服务方法返回统一的数据格式
 * @author Administrator
 * 
 * @param <T>
 */
public class ReplyDataMode implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean success;
	/**服务状态码，可以通过状态码表查询返回的状态码对应的值，以便进一步了解服务处理情况*/
	private String statusCode;
	/**服务处理后的结果数据*/
	private Object data = null;
	/**本次服务的相关参数键值对*/
	private Map<String,Object> attribute;
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
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
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	
}