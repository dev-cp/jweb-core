package org.jweb.core.bean;

import java.io.File;

/**
 * 能存放普通值和上传文件file的键值对模型对象，在使用HttpClientUtil工具httpclient上传文件时，优先使用文本类型值，如果文本类型值
 * 为空，则会尝试使用文件类型值
 * 可以使用JavaBeanUtil工具中的方法批量转换JavaBean对象中的字段和值为List<MultipartNameValuePair>
 * @author wupan
 *
 */
public class MultipartNameValuePair {

	/**键名*/
	private String name;
	/**文本值*/
	private String textValue;
	/**文件值*/
	private File fileValue;
	
	public MultipartNameValuePair(){}
	
	public MultipartNameValuePair(String name,String textValue){
		this.textValue = textValue;
		this.name = name;
	}
	public MultipartNameValuePair(String name,File fileValue){
		this.name = name;
		this.fileValue = fileValue;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public File getFileValue() {
		return fileValue;
	}
	public void setFileValue(File fileValue) {
		this.fileValue = fileValue;
	}
	
	
	
}
