package org.jweb.core.bean;

import org.apache.http.NameValuePair;

/**
 * 主要用于http请求的参数传递
 * @author Administrator
 * 
 */

public class NameValueBean implements NameValuePair {


	private String name;//参数名
	private String value;//参数值
	private boolean encode = true;//编码方式

	public NameValueBean(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public NameValueBean(String name, String value, boolean encode) {
		this.name = name;
		this.value = value;
		this.encode = encode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEncode() {
		return encode;
	}

	public void setEncode(boolean encode) {
		this.encode = encode;
	}
}