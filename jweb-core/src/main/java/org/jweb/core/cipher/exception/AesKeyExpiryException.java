package org.jweb.core.cipher.exception;

/**
 * aesKey失效异常，当客户端向服务端传递了加密数据，或者服务端准备对返回数据进行加密时，
 * 服务端根据客户端指定的aesKeyId从cache中提取aesKey时，如果aesKey为空，或者失效，
 * 则会抛出该异常
 * @author wupan
 *
 */
public class AesKeyExpiryException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1513383802728013883L;
	
	
	public AesKeyExpiryException(){
		super();
	}
	public AesKeyExpiryException(String msg){
		super(msg);
	}
	public AesKeyExpiryException(Throwable throwable){
		super(throwable);
	}
	public AesKeyExpiryException(String msg,Throwable throwable){
		super(msg, throwable);
	}
}
