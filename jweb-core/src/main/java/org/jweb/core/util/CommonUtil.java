package org.jweb.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {
	private static Log log = LogFactory.getLog(CommonUtil.class);
	public static final String STDDF = "yyyy-MM-dd";
	public static final String STDDTF = "yyyy-MM-dd HH:mm:ss";

	/**
	 * �?equals 方法判断对象是否在数组中
	 * 
	 * @param o
	 * @param arr
	 * @return
	 */
	public static int inArray(Object o, Object[] arr) {
		if (arr == null || arr.length < 1)
			return -1;
		for (int i = 0; i < arr.length; i++) {
			if (o == null) {
				if (arr[i] == null)
					return i;
			} else if (o.equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 数组转字符串
	 * 
	 * @param arr
	 * @param sp
	 * @return
	 */
	public static String array2str(Object[] arr, String sp) {
		if (arr == null || arr.length == 0)
			return "";
		String result = String.valueOf(arr[0]);
		for (int i = 1; i < arr.length; i++)
			result = result + sp + String.valueOf(arr[i]);
		return result;
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param sf
	 * @return
	 */
	public static String date2str(Date date, String sf) {
		SimpleDateFormat f = new SimpleDateFormat(sf);
		return f.format(date);
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String date2str(Date date) {
		SimpleDateFormat f = new SimpleDateFormat(STDDF);
		return f.format(date);
	}

	/**
	 * 获取当前日期并转为标准格式字符串
	 * 
	 * @return
	 */
	public static String getStdfDate() {
		return CommonUtil.date2str(new Date());
	}

	/**
	 * 获取当前时间并转为标准格式字符串
	 * 
	 * @return
	 */
	public static String getStdfDateTime() {
		return CommonUtil.date2str(new Date(), STDDTF);
	}

	/**
	 * 字符串转日期
	 * 
	 * @param s
	 * @return
	 */
	public static Date str2date(String s) {
		SimpleDateFormat f1 = new SimpleDateFormat(STDDF);
		try {
			return f1.parse(s);
		} catch (ParseException e) {
			log.error(e, e);
			return null;
		}
	}

	/**
	 * 字符串转日期2
	 * 
	 * @param s
	 * @param sf
	 * @return
	 */
	public static Date str2date(String s, String sf) {
		if (sf == null || "".equals(sf)) {
			sf = STDDF;
		}
		SimpleDateFormat f1 = new SimpleDateFormat(sf);
		try {
			return f1.parse(s);
		} catch (ParseException e) {
			log.error(e, e);
			return null;
		}
	}

	/**
	 * 将日期从�?��格式转为另一种格�?
	 * 
	 * @param sf
	 * @param sv
	 * @param df
	 * @return
	 * @throws ParseException
	 */
	public static String formatDateString(String sf, String sv, String df)
			throws ParseException {
		SimpleDateFormat f1 = new SimpleDateFormat(sf);
		Date d = f1.parse(sv);
		SimpleDateFormat f2 = new SimpleDateFormat(df);
		return f2.format(d);
	}

	/**
	 * 字符串安全转整型，用第二个参数指定转换失败时的返回�?
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static int str2int(String s, int r) {
		if (s == null || "null".equals(s) || "".equals(s)) {
			return r;
		} else {
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
				log.error(e, e);
				return r;
			}
		}
	}

	/**
	 * 对象安全转整型，用第二个参数指定转换失败时的返回�?
	 * 
	 * @param o
	 * @param r
	 * @return
	 */
	public static int object2int(Object o, int r) {
		return CommonUtil.str2int(String.valueOf(o), r);
	}

	/**
	 * 字符串是否为�?
	 * 
	 * @param s
	 * @return
	 */
	public static boolean strIsEmpty(String s) {
		return (s == null || "".equals(s));
	}

	/**
	 * 字符串不�?
	 * 
	 * @param s
	 * @return
	 */
	public static boolean strIsNotEmpty(String s) {
		return (s != null && !"".equals(s));
	}

	/**
	 * Unicode字符->GBK字符
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String utf2gbk(String str) throws Exception {
		if (str != null) {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} else {
			return null;
		}
	}

	/**
	 * 字符串安全转长整型，用第二个参数指定转换失败时的返回�?
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static long str2long(String s, long r) {
		if (s == null || "null".equals(s) || "".equals(s)) {
			return r;
		} else {
			try {
				return Long.parseLong(s);
			} catch (Exception e) {
				log.error(e, e);
				return r;
			}
		}
	}

	/**
	 * 对象安全转长整型，用第二个参数指定转换失败时的返回�?
	 * 
	 * @param o
	 * @param r
	 * @return
	 */
	public static long object2long(Object o, long r) {
		return CommonUtil.str2long(String.valueOf(o), r);
	}

	/**
	 * 字符串安全转Double
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static double str2double(String s, double r) {
		if (s == null || "null".equals(s) || "".equals(s)) {
			return r;
		} else {
			try {
				return Double.parseDouble(s);
			} catch (Exception e) {
				log.error(e, e);
				return r;
			}
		}
	}

	/**
	 * 对象安全转double
	 * 
	 * @param o
	 * @param r
	 * @return
	 */
	public static double object2double(Object o, double r) {
		return CommonUtil.str2double(String.valueOf(o), r);
	}

	/**
	 * 格式化日�?
	 * 
	 * @param fmt
	 * @return
	 */
	public static String date2str(String fmt) {
		return CommonUtil.date2str(new Date(), fmt);
	}
}
