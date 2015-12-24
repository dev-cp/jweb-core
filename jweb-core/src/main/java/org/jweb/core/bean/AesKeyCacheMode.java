package org.jweb.core.bean;

import java.io.Serializable;

/**
 * 对称秘钥的缓存模型，除了存放对称秘钥aesKey外，还提供了用于缓存管理的参数，
 * 允许定时任务根据模型中的时间决定当前缓存模型是否应该从cache中踢出，以免缓存溢出
 * @author wupan
 *
 */
public class AesKeyCacheMode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6458775517575933941L;
	
	/**aesKey存放在cache中的id*/
	private String id;
	/**密码字串，使用base64编码*/
	private String aesKey;
	/**iv字串，使用base64编码*/
	private String iv;
	/**缓存起始时间戳*/
	private long startTimestamp;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public long getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	
	
}
