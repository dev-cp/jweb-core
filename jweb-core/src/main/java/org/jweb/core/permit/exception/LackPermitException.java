package org.jweb.core.permit.exception;

/**
 * 权限不足异常，当进行权限校验时，权限不足则手动抛出该异常，方便系统对异常进行集中处理
 * @author wupan
 *
 */
public class LackPermitException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 23709329049987080L;
	
	public LackPermitException(){
		super();
	}
	public LackPermitException(String msg){
		super(msg);
	}
	public LackPermitException(Throwable throwable){
		super(throwable);
	}
	public LackPermitException(String msg,Throwable throwable){
		super(msg, throwable);
	}
	
	
}
