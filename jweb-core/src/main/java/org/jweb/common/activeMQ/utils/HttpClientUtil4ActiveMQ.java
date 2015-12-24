package org.jweb.common.activeMQ.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jweb.common.activeMQ.mode.HttpMsgMode;
import org.jweb.core.bean.MultipartNameValuePair;
import org.jweb.core.util.StringUtil;

/**
 * 用于处理消息队列中消息的httpClient工具类，该工具类独享固定数量的处理线程，不会被网站并发开启的线程数所影响
 * @author wupan
 *
 */
public class HttpClientUtil4ActiveMQ {
	private static Log log = LogFactory.getLog(HttpClientUtil4ActiveMQ.class);

	// 浏览器Agent
	public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/519 (KHTML, like Gecko) Chrome/0.10151 Safari/519";

	// 创建并配置HttpClient
	
	private static CloseableHttpClient getHttpClient(){
		return  HttpClients
				.custom()
				.setUserAgent(USER_AGENT)
				.setMaxConnPerRoute(50)
				.setMaxConnTotal(50)
				.setDefaultRequestConfig(
						RequestConfig.custom()
								.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
								.build()).build();
	}



	/**
	 * 适用于带上传文件的post提交
	 * 
	 * @param url
	 * @param mnvps
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPost(HttpMsgMode httpMsgMode) throws ClientProtocolException, IOException{
		//做数据校验
		if(httpMsgMode == null){
			return null;
		}
		
		String url = httpMsgMode.getUrl();
		if(StringUtil.isEmpty(url)){
			return null;
		}
		
		List<MultipartNameValuePair> mnvps = httpMsgMode.getMnvps();
		if(mnvps == null){
			mnvps = new ArrayList<MultipartNameValuePair>();
		}
		
		//封装请求方法
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		CloseableHttpResponse response = null;
		StringBuilder result = new StringBuilder();

		try {

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			for (MultipartNameValuePair mnvp : mnvps) {
				String name = mnvp.getName();
				String textValue = mnvp.getTextValue();
				File fileValue = mnvp.getFileValue();

				if (fileValue.exists()) {//如果文件存在，才上传
					builder.addBinaryBody(name, fileValue);
				}
				
				if (textValue != null) {//如果值不为null，允许""空串，才上传
					builder.addTextBody(name, textValue);
					
				} 
			}

			HttpEntity httpEntity = builder.build();

			httpPost.setEntity(httpEntity);
			HttpClientContext context = HttpClientContext.create();

			response = httpClient.execute(httpPost, context);
			return EntityUtils.toString(response.getEntity(),"UTF-8");

		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			httpPost.abort();
			httpPost.releaseConnection();
			httpClient.close();
		}
	}

	

}