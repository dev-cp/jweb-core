package org.jweb.core.bean;

import java.io.File;

/**
 * 文件上传键值对模型，类似于apache httpclient包中的NameValuePair类型，主要是方便使用
 * httpclient上传文件
 * @author wupan
 *
 */
public class FileNameValuePair {

	/**字段名*/
	private String name;
	/**对应的值*/
	private  File value;
	
	public FileNameValuePair(){}
	public FileNameValuePair(String name,File value){
		this.name = name;
		this.value = value;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getValue() {
		return value;
	}
	public void setValue(File value) {
		this.value = value;
	}
	
	
}
