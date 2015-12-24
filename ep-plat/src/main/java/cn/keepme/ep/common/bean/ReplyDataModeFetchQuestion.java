package cn.keepme.ep.common.bean;

import java.io.Serializable;

/***
 * 返回信息的格式，用于rest 或 webservice服务方法返回统一的数据格式
 * @author Administrator
 * 
 * @param <T>
 */
public class ReplyDataModeFetchQuestion implements Serializable {
	private static final long serialVersionUID = 1L;
//	private boolean success;
	/**服务状态码，可以通过状态码表查询返回的状态码对应的值，以便进一步了解服务处理情况*/
	private String status;
	
	/**服务处理后的结果数据*/
	private Object qInfo = null;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getqInfo() {
		return qInfo;
	}
	public void setqInfo(Object qInfo) {
		this.qInfo = qInfo;
	}
	
}