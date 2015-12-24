package org.jweb.core.bean;

import java.io.Serializable;

/***
 * 返回信息的格式
 * @author Administrator
 * 
 * @param <T>
 */
public class ReplyInfo<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean success = true;
	private T data = null;
	private String[] params;

	public ReplyInfo() {
		this.success = true;
	}

	public ReplyInfo(T data) {
		this.success = false;
		this.data = data;
	}

	public ReplyInfo(T data, String[] params) {
		this.success = false;
		this.data = data;
		this.params = params;
	}

	public ReplyInfo(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public ReplyInfo(boolean success, T data, String[] params) {
		this.success = success;
		this.data = data;
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}