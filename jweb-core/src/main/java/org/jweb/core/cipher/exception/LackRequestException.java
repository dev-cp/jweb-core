package org.jweb.core.cipher.exception;

/**
 * 
* @Title: LackRequestException.java 
* @Package org.jweb.common.exception 
* @Description: 如果请求方法中未定义任何入参，而该请求方法的拦截器确实需要一些入参做处理，则抛出该异常
* @author wupan  
* @date 2014年10月23日 下午4:49:01 
* @version V1.0
 */
public class LackRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8767451980628728234L;
	
	public LackRequestException(){
		super();
	}
	public LackRequestException(String msg){
		super(msg);
	}
	public LackRequestException(Throwable throwable){
		super(throwable);
	}
	public LackRequestException(String msg,Throwable throwable){
		super(msg, throwable);
	}
}
