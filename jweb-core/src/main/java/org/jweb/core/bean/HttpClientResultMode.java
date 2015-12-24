package org.jweb.core.bean;

import org.apache.http.client.protocol.HttpClientContext;

/**
 * httpClient结果模型，除了封装http响应结果外，还将http请求上下文环境封装起来
 * @author wupan
 *
 */
public class HttpClientResultMode<T> {

	/**响应结果，可能是String,可能是二进制流，可能是其他类型数据，与具体的httpClient工具方法有关*/
	private T result;
	/**httpClient上下文环境*/
	private HttpClientContext context;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public HttpClientContext getContext() {
		return context;
	}

	public void setContext(HttpClientContext context) {
		this.context = context;
	}
	
	
}
