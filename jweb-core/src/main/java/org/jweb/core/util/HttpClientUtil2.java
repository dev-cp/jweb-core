package org.jweb.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jweb.core.bean.NameValueBean;


public class HttpClientUtil2 {
	private static Log log = LogFactory.getLog(HttpClientUtil2.class);

	/**
	 * 
	 * @param url
	 * @param to
	 * @return
	 */
	public static boolean downloadFile(String url, String to) {
		log.info("Go to download file from " + url);
		BufferedInputStream br = null;
		BufferedOutputStream bw = null;
		try {
			File desFile = new File(to);
			URLConnection conn = new URL(url).openConnection();
			br = new BufferedInputStream(conn.getInputStream());
			bw = new BufferedOutputStream(new FileOutputStream(desFile));
			byte[] b = new byte[1024];
			int d;
			while ((d = br.read(b)) != -1) {
				bw.write(b, 0, d);
			}
			log.info("Download completed. Please check " + to);
			return true;
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(bw);
			StreamUtil.close(br);
		}
		return false;
	}

	public static String sendGet(String url, List<NameValueBean> params) {
		return sendGet(url, "UTF-8", "UTF-8", params, false);
	}

	public static String sendGet(String url, String encOut, String encIn, List<NameValueBean> params) {
		return sendGet(url, encOut, encIn, params, false);
	}

	public static String sendPost(String url, List<NameValueBean> params) {
		return sendPost(url, "UTF-8", "UTF-8", params, false, null);
	}

	public static String sendPost(String url, String encOut, String encIn, List<NameValueBean> params) {
		return sendPost(url, encOut, encIn, params, false, null);
	}

	public static String sendGet(String url, String encOut, String encIn, List<NameValueBean> params,
			boolean asyn) {
		return sendGet(url, encOut, encIn, params, asyn, null, null, false);
	}

	public static String sendGet(String url, String encOut, String encIn, List<NameValueBean> params,
			boolean asyn, Map<String, String> headers) {
		return sendGet(url, encOut, encIn, params, asyn, headers, null, false);
	}

	/**
	 * 
	 * @param url
	 * @param encOut
	 * @param encIn
	 * @param params
	 * @param headers
	 * @return
	 */
	public static Map<String, List<String>> sendHead(String url) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setReadTimeout(30000);
			conn.setRequestMethod("HEAD");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			return conn.getHeaderFields();
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	public static String sendGet(String url, String encOut, String encIn, List<NameValueBean> params,
			boolean asyn, Map<String, String> headers, String cookieValue, boolean cookie) {
		StringBuffer sb = new StringBuffer(url);
		BufferedReader br = null;
		try {
			if (params != null && params.size() > 0) {
				sb.append("?");
				boolean b = false;
				for (NameValueBean nv : params) {
					if (b)
						sb.append("&");
					String v = nv.getValue();
					if (nv.isEncode())
						v = URLEncoder.encode(v, encOut);
					sb.append(nv.getName()).append("=").append(v);
					b = true;
				}
			}
			url = sb.toString();
			URLConnection conn = new URL(url).openConnection();
			if (cookie && cookieValue != null)
				conn.setRequestProperty("Cookie", cookieValue);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("contentType ", "application/json;charset=utf-8");
			if (headers != null && headers.size() > 0) {
				for (String h : headers.keySet())
					conn.setRequestProperty(h, headers.get(h));
			}
			conn.connect();
			if (asyn)
				return null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encIn));
			sb.setLength(0);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			log.error("Error on visit " + url, e);
		} finally {
			StreamUtil.close(br);
		}
		return null;
	}

	public static String sendPost(String url, String encOut, String encIn, List<NameValueBean> params,
			boolean asyn, Map<String, String> headers) {
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// conn.setConnectTimeout(30000);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			if (headers != null && headers.size() > 0) {
				for (String h : headers.keySet())
					conn.setRequestProperty(h, headers.get(h));
			}
			StringBuffer sb = new StringBuffer();
			if (params != null && params.size() > 0) {
				out = new PrintWriter(conn.getOutputStream());
				boolean b = false;
				for (NameValueBean nv : params) {
					if (b)
						sb.append("&");
					String v = nv.getValue();
					if (nv.isEncode())
						v = URLEncoder.encode(v, encOut);
					sb.append(nv.getName()).append("=").append(v);
					b = true;
				}
				out.print(sb.toString());
				out.flush();
			}
			if (asyn)
				return null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encIn));
			sb.setLength(0);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(br);
			StreamUtil.close(out);
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param encIn
	 * @param xml
	 *            /json
	 * @return
	 */
	public static String sendPost(String url, String encIn, String str) {
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// conn.setConnectTimeout(30000);
			conn.setRequestProperty("content-type", "text/html");
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(str);
			out.flush();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encIn));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(out);
			StreamUtil.close(br);
		}
		return null;
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readStr(InputStream is) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			StreamUtil.close(br);
		}
	}
}