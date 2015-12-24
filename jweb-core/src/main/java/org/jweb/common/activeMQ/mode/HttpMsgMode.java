package org.jweb.common.activeMQ.mode;

import java.io.Serializable;
import java.util.List;

import org.jweb.core.bean.MultipartNameValuePair;

/**
 * 封装http请求参数的模型，用于将http请求传入消息队列，方便httpClient工具处理
 * @author wupan
 *
 */
public class HttpMsgMode implements Serializable{
	/**用户id,用于指定当前httpMsgMode是属于哪个会员的*/
	private String userId;
	/**http session Id,用于与当前在线用户http session会话关联*/
	private String sessionId;
	/**请求http处理使用的url*/
	private String url;
	/**http请求时传递的参数，允许文件上传*/
	private List<MultipartNameValuePair> mnvps;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MultipartNameValuePair> getMnvps() {
		return mnvps;
	}

	public void setMnvps(List<MultipartNameValuePair> mnvps) {
		this.mnvps = mnvps;
	}

	
	
	
}
