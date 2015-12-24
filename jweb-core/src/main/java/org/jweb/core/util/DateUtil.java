package org.jweb.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	// 各种时间格式
	public static final SimpleDateFormat date_sdf = new SimpleDateFormat(
			"yyyy-MM-dd");
	// 各种时间格式
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(
			"yyyyMMdd");
	// 各种时间格式
	public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat(
			"yyyy年MM月dd日");
	public static final SimpleDateFormat time_sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat(
	"yyyyMMddHHmmss");
	public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat(
			"HH:mm");
	public static final  SimpleDateFormat datetimeFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	public enum DatePart {
		YEAR, MONTH, WEEK, DAY
	}

	private DateUtil() {
	}

	/**
	 * 计算日期差�?
	 * 
	 * @param startDate
	 * @param endDate
	 * @param datePart 计算天数差距（传DateUtil.DatePart.DAY） 星期差距（传DateUtil.DatePart.WEEK） 月份差距（传DateUtil.DatePart.MONTH）
	 * @return
	 */
	@SuppressWarnings("incomplete-switch")
	public static int dateDiff(Date startDate, Date endDate, DatePart datePart) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endDate);
		int d = getDaysBetween(c1.getTime(), c2.getTime());
		switch (datePart) {
			case DAY:
				return d;
			case WEEK:
				int w1 = c1.get(Calendar.DAY_OF_WEEK);
				int w2 = c2.get(Calendar.DAY_OF_WEEK);
				int w = d / 7;
				return (w1 > w2) ? w + 1 : w;
			case MONTH:
				return Math.round(d / 30.4375f);
		}
		return 0;
	}
	
	/**
	 * 计算日期差�?
	 * 
	 * @param sf 时间格式，如yyyy-MM-dd
	 * @param startDate
	 * @param endDate
	 * @param datePart 计算天数差距（传DateUtil.DatePart.DAY） 星期差距（传DateUtil.DatePart.WEEK） 月份差距（传DateUtil.DatePart.MONTH）
	 * @return
	 */
	public static int dateDiff(String sf, String startDate, String endDate, DatePart datePart) {
		return dateDiff(CommonUtil.str2date(startDate, sf), CommonUtil.str2date(endDate, sf), datePart);
	}

	/**
	 * 当年是否闰年
	 * 
	 * @return
	 */
	public static synchronized boolean isLeapYear() {
		return isLeapYear(Calendar.getInstance().get(Calendar.YEAR));
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 * @return
	 */
	public static synchronized boolean isLeapYear(int year) {
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 取得指定日期的上�?��月的第一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfPrevMonth(Date date) {
		return DateUtil.getFirstDayOfMonth(DateUtil.addMonth(date, -1));
	}

	/**
	 * 取得指定日期的上�?��月的�?���?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfPrevMonth(Date date) {
		return DateUtil.getLastDayOfMonth(DateUtil.addMonth(date, -1));
	}

	/**
	 * 取得指定日期的下�?��月的第一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfNextMonth(Date date) {
		return DateUtil.getFirstDayOfMonth(DateUtil.addMonth(date, 1));
	}

	/**
	 * 取得指定日期的下�?��月的�?���?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfNextMonth(Date date) {
		return DateUtil.getLastDayOfMonth(DateUtil.addMonth(date, 1));
	}

	/**
	 * 取得指定日期的上�?��星期的第�?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfPrevWeek(Date date) {
		return DateUtil.getFirstDayOfWeek(DateUtil.addWeek(date, -1));
	}

	/**
	 * 取得指定日期的上�?��星期的最后一�?
	 * 
	 * @param date
	 *            指定日期�?
	 * @return 指定日期的下�?��星期的最后一�?
	 */
	public static synchronized Date getLastDayOfPrevWeek(Date date) {
		return DateUtil.getLastDayOfWeek(DateUtil.addWeek(date, -1));
	}

	/**
	 * 取得指定日期的下�?��星期的第�?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfNextWeek(Date date) {
		return DateUtil.getFirstDayOfWeek(DateUtil.addWeek(date, 1));
	}

	/**
	 * 取得指定日期的下�?��星期的最后一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfNextWeek(Date date) {
		return DateUtil.getLastDayOfWeek(DateUtil.addWeek(date, 1));
	}

	/**
	 * 取得指定日期的所处星期的�?���?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfWeek(Date date) {
		/**
		 * 详细设计�?1.如果date是星期日，则�?�?2.如果date是星期一，则�?�?3.如果date是星期二，则�?�?
		 * 4.如果date是星期三，则�?�?5.如果date是星期四，则�?�?6.如果date是星期五，则�?�?
		 * 7.如果date是星期六，则�?�?
		 */
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 6);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, 5);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, 4);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, 2);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 1);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 0);
				break;
		}
		return gc.getTime();
	}

	/**
	 * 取得指定日期的所处星期的第一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfWeek(Date date) {
		/**
		 * 详细设计�?1.如果date是星期日，则�?�?2.如果date是星期一，则�?�?3.如果date是星期二，则�?�?
		 * 4.如果date是星期三，则�?�?5.如果date是星期四，则�?�?6.如果date是星期五，则�?�?
		 * 7.如果date是星期六，则�?�?
		 */
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 0);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -1);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, -2);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, -4);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, -5);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, -6);
				break;
		}
		return gc.getTime();
	}

	/**
	 * 取得指定日期的所处月份的�?���?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfMonth(Date date) {
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, getMaxDayOfMonth(date));
		// �?��闰年
		if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY) && (isLeapYear(gc.get(Calendar.YEAR))))
			gc.set(Calendar.DAY_OF_MONTH, 29);
		return gc.getTime();
	}

	/**
	 * 取得指定日期的所处月份的第一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfMonth(Date date) {
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return gc.getTime();
	}

	/**
	 * 取得指定日期的所处年的最后一�?
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getLastDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, 12);
		c.set(Calendar.DATE, 31);
		return c.getTime();
	}

	/**
	 * 取得指定日期的所处年的第�?��
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, 1);
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}

	// 获取日期间隔天数
	public static synchronized int getDaysBetween(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		// swap dates so that d1 is start and d2 is end
		if (d1.after(d2)) {
			Calendar swap = c1;
			c1 = c2;
			c2 = swap;
		}
		int days = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		int y2 = c2.get(Calendar.YEAR);
		if (c1.get(Calendar.YEAR) != y2) {
			c1 = (Calendar) c1.clone();
			do {
				days += c1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天�?
				c1.add(Calendar.YEAR, 1);
			} while (c1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	public static synchronized int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(7);
	}

	public static synchronized int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(5);
	}

	/**
	 * 当月�?��天数
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized int getMaxDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(5);
	}

	public static synchronized int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(6);
	}
	
	public static synchronized Date addSecond(Date date, long count) {
		return new Date(date.getTime() + 1000L * count);
	}

	public static synchronized Date addMinute(Date date, int count) {
		return new Date(date.getTime() + 60000L * (long) count);
	}

	public static synchronized Date addHour(Date date, int count) {
		return new Date(date.getTime() + 0x36ee80L * (long) count);
	}

	public static synchronized Date addDay(Date date, int count) {
		return new Date(date.getTime() + 0x5265c00L * (long) count);
	}

	public static synchronized Date addWeek(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(3, count);
		return c.getTime();
	}

	public static synchronized Date addMonth(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(2, count);
		return c.getTime();
	}

	public static synchronized Date addYear(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(1, count);
		return c.getTime();
	}

	/**
	 * 指定日期是否在本周范围内
	 * 
	 * @param d
	 * @return
	 */
	public static boolean inCurrentWeek(Date d) {
		Date today = new Date();
		return between(d, getFirstDayOfWeek(today), getLastDayOfWeek(today));
	}

	/**
	 * 指定日期是否某日期范围内
	 * 
	 * @param d
	 * @return
	 */
	public static boolean between(Date date, Date date1, Date date2) {
		if (date1.after(date2)) {
			Date swap = date1;
			date1 = date2;
			date2 = swap;
		}
		return date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0;
	}

	/**
	 * 获取当前日期,同时丢弃时间
	 * 
	 * @return
	 */
	public static synchronized Date getCurrentDate() {
		return CommonUtil.str2date(CommonUtil.getStdfDate());
	}
	
	/**
	 * 获取时间字符串
	 */
	public static String getDataString(SimpleDateFormat formatstr) {
		return formatstr.format(getCalendar().getTime());
	}
	/**
	 * 当前日历，这里用中国时间表示
	 * 
	 * @return 以当地时区表示的系统当前日历
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}
}
