package org.jweb.core.cipher.exception;

/**
 * 
* @Title: LackRequestParameterException.java 
* @Package org.jweb.common.exception 
* @Description: 缺少查询参数异常，如当前requst请求的响应方法中缺少了一个HttpServletRequest入参，业务方法，或者拦截方法确实需要该入参，就可以抛出该异常，
* @author wupan  
* @date 2014年10月23日 下午4:46:03 
* @version V1.0
 */
public class LackRequestParameterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8767451980628728234L;
	
	public LackRequestParameterException(){
		super();
	}
	public LackRequestParameterException(String msg){
		super(msg);
	}
	public LackRequestParameterException(Throwable throwable){
		super(throwable);
	}
	public LackRequestParameterException(String msg,Throwable throwable){
		super(msg, throwable);
	}
}
