package org.jweb.core.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jweb.core.bean.FileNameValuePair;
import org.jweb.core.bean.HttpClientResultMode;
import org.jweb.core.bean.MultipartNameValuePair;

public class HttpClientUtil {
	private static Log log = LogFactory.getLog(HttpClientUtil.class);

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


	public static String sendPost(String url, List<NameValuePair> nvps)
			throws ClientProtocolException, IOException {
		if(nvps == null){
			nvps = new ArrayList<NameValuePair>();
		}
		
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		httpPost.setHeader("Accept", "application/xml,text/html,application/json");
		httpPost.setHeader("ContentType", "application/json");
		
		CloseableHttpResponse response = null;
		StringBuilder result = new StringBuilder();

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
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
	
	/**
	 * post请求，允许调用者在已知的上下文环境上发起新的http请求，响应结果转成String类型
	 * @param url
	 * @param nvps
	 * @param context 已知的上下文，允许传入空，如果为空，则新建上下文
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpClientResultMode<String> sendPostString(String url, List<NameValuePair> nvps,HttpClientContext context)
			throws ClientProtocolException, IOException {
		HttpClientResultMode<String> resultMode = new HttpClientResultMode<String>();
		
		if(nvps == null){
			nvps = new ArrayList<NameValuePair>();
		}
		
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		httpPost.setHeader("Accept", "application/xml,text/html,application/json");
		httpPost.setHeader("ContentType", "application/json");
		
		CloseableHttpResponse response = null;
		StringBuilder result = new StringBuilder();
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
			//判断如果上下文为空，则新建上下文
			if(context == null){
				context = HttpClientContext.create();
			}
			
			response = httpClient.execute(httpPost, context);
			String data = EntityUtils.toString(response.getEntity(),"UTF-8");
			
			resultMode.setResult(data);
			resultMode.setContext(context);
			
			return resultMode;
			
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

	/**
	 * 上传带文件和文本键值对的post提交方法
	 * 
	 * @param url
	 * @param nvps
	 *            普通文本键值对
	 * @param fnvps
	 *            文件键值对
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPost(String url, List<NameValuePair> nvps,
			List<FileNameValuePair> fnvps) throws ClientProtocolException,
			IOException {
		if(fnvps == null){
			fnvps = new ArrayList<FileNameValuePair>();
		}
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		CloseableHttpResponse response = null;
		StringBuilder result = new StringBuilder();

		try {

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			for (FileNameValuePair fnvp : fnvps) {
				builder.addBinaryBody(fnvp.getName(), fnvp.getValue());
			}

			for (NameValuePair nvp : nvps) {
				builder.addTextBody(nvp.getName(), nvp.getValue());
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

	/**
	 * 适用于带上传文件的post提交
	 * 
	 * @param url
	 * @param mnvps
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPost2(String url,
			List<MultipartNameValuePair> mnvps) throws ClientProtocolException,
			IOException {
		if(mnvps == null){
			mnvps = new ArrayList<MultipartNameValuePair>();
		}
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

//	public static String sendGet(String url) throws ClientProtocolException,
//			IOException {
//		CloseableHttpClient httpClient1 = getHttpClient();
//		HttpGet httpGet = new HttpGet(url);
//		httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
//		CloseableHttpResponse response = null;
//		try {
//			HttpClientContext context = HttpClientContext.create();
//
//			response = httpClient1.execute(httpGet, context);
//			StatusLine statusLine = response.getStatusLine();
//			if(statusLine.getStatusCode() == 200){
//				HttpEntity entity = response.getEntity();
//				String data = EntityUtils.toString(entity,"UTF-8");
//				return data;
//			} else {
//				return null;
//			}
//
//		} finally {
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			httpGet.abort();
//			httpGet.releaseConnection();
//			httpClient1.close();
//		}
//
//	}
	
	/**
	 * get请求，允许调用者在已知的上下文环境上发起新的http请求，响应结果转成String类型
	 * @param url 请求的url
	 * @param context 已知的上下文，允许传入空，如果为空，则新建上下文
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpClientResultMode<String> sendGetString(String url,HttpClientContext context) throws ClientProtocolException,
	IOException {
		HttpClientResultMode<String> resultMode = new HttpClientResultMode<String>();
		
		CloseableHttpClient httpClient1 = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		CloseableHttpResponse response = null;
		try {
			//如果传入的上下文context是空，则新建一个上下文
			if(context == null){
				context = HttpClientContext.create();
			}
			String data = "";
			
			
			response = httpClient1.execute(httpGet, context);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				data = EntityUtils.toString(entity,"UTF-8");
				
				
			} 
			
			resultMode.setResult(data);
			resultMode.setContext(context);
			return resultMode;
			
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			httpGet.abort();
			httpGet.releaseConnection();
			httpClient1.close();
		}
		
	}

	public static File download(String url, String filePathName) {
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(url);

			// 伪装成google的爬虫JAVA问题查询
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			// Execute HTTP request
			log.info("executing request " + httpget.getURI());
			response = httpClient.execute(httpget);

			File storeFile = new File(filePathName);
			FileOutputStream output = new FileOutputStream(storeFile);

			// 得到网络资源的字节数组,并写入文件
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte b[] = new byte[1024];
					int j = 0;
					while ((j = instream.read(b)) != -1) {
						output.write(b, 0, j);
					}
					output.flush();
					output.close();
					
					return storeFile;
				} catch (IOException ex) {
					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					throw ex;
				} catch (RuntimeException ex) {
					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection immediately.
					httpget.abort();
					throw ex;
				} finally {
					// Closing the input stream will trigger connection release
					try {
						instream.close();
					} catch (Exception ignore) {
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(httpget != null){
				httpget.abort();
				httpget.releaseConnection();
				
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	

}