package cn.keepme.ep.common.bean;

import java.io.Serializable;

/***
 * 返回信息的格式，用于rest 或 webservice服务方法返回统一的数据格式
 * @author Administrator
 * 
 * @param <T>
 */
public class ReplyDataModeLogin implements Serializable {
	private static final long serialVersionUID = 1L;
//	private boolean success;
	/**服务状态码，可以通过状态码表查询返回的状态码对应的值，以便进一步了解服务处理情况*/
	private String status;
	private String nickname;
	private String type;
	/**服务处理后的结果数据*/
	private Object qIdList = null;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getqIdList() {
		return qIdList;
	}
	public void setqIdList(Object qIdList) {
		this.qIdList = qIdList;
	}
//	public boolean isSuccess() {
//		return success;
//	}
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
	
}