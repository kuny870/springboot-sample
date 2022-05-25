package com.wizvera.templet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Utility class for the date conversion.
 */
public class DateUtils {

	private static final ZoneId		ZONE_UTC			= ZoneId.of("UTC");
	public static final TimeZone	DEFAULT_TIME_ZONE	= TimeZone.getTimeZone(ZoneId.systemDefault());
	public static final String		DATE_PATTERN_14		= "yyyyMMddHHmmss";
	public static final String		DATE_PATTERN_17		= "yyyyMMddHHmmssSSS";

	public static final String DATE_PATTERN_14_WITH_DELIM = "yyyy-MM-dd HH:mm:ss";

	private static final DateTimeFormatter	SDF1	= DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter	SDF2	= DateTimeFormatter.ofPattern("yyyyMMdd");

	private DateUtils() {
	}

	/**
	 * Asia/Seoul time zone에 해당하는 현재 시간을 반환한다.
	 * 
	 * @return 현재 시간
	 */
	public static Date getNow() {
		Calendar cal = Calendar.getInstance(DEFAULT_TIME_ZONE);
		return cal.getTime();
	}

	/**
	 * Asia/Seoul time zone에 해당하는 현재 시간을 전달된 {@resultCode pattern} 형태의 문자열로 변환하여 반환한다.
	 * 
	 * @param pattern
	 *        현재 시간을 변환하고자 하는 PATTERN
	 * @return 특정 PATTERN 형태의 문자열
	 */
	public static String getNow(String pattern) {
		Date now = getNow();

		return dateToString(now, pattern);
	}

	public static Date parseUtcTimeyyyyMMddhhmmss(String utcTime) {
		String coreUtcTime = utcTime;
		if ( StringUtil.isNotBlank(utcTime) ) {
			char ch = utcTime.charAt(utcTime.length() - 1);
			if ( ch == 'z' || ch == 'Z' ) {
				coreUtcTime = utcTime.substring(0, utcTime.length() - 1);
			}
		}

		if ( coreUtcTime == null || coreUtcTime.length() != 14 ) {
			throw new IllegalArgumentException("invalid utcTime '" + utcTime + "'");
		}

		try {
			LocalDateTime localDate = LocalDateTime.parse(coreUtcTime, SDF1);
			Instant instant = localDate.atZone(ZONE_UTC).toInstant();
			return Date.from(instant);
		}
		catch (DateTimeParseException ex) {
			throw new IllegalArgumentException("invalid utcTime '" + utcTime + "': " + ex.getMessage());
		}
	} // method parseUtcTimeyyyyMMddhhmmss

	public static Date parseUtcTimeyyyyMMdd(String utcTime) {
		String coreUtcTime = utcTime;
		if ( StringUtil.isNotBlank(utcTime) ) {
			char ch = utcTime.charAt(utcTime.length() - 1);
			if ( ch == 'z' || ch == 'Z' ) {
				coreUtcTime = utcTime.substring(0, utcTime.length() - 1);
			}
		}

		if ( coreUtcTime == null || coreUtcTime.length() != 8 ) {
			throw new IllegalArgumentException("invalid utcTime '" + utcTime + "'");
		}

		try {
			LocalDateTime localDate = LocalDateTime.parse(coreUtcTime + "000000", SDF1);
			Instant instant = localDate.atZone(ZONE_UTC).toInstant();
			return Date.from(instant);
		}
		catch (DateTimeParseException ex) {
			throw new IllegalArgumentException("invalid utcTime '" + utcTime + "': " + ex.getMessage());
		}
	} // method parseUtcTimeyyyyMMdd

	public static String toUtcTimeyyyyMMddhhmmss(Date utcTime) {
		return SDF1.format(utcTime.toInstant().atZone(ZONE_UTC));
	}

	public static String toUtcTimeyyyyMMdd(Date utcTime) {
		return SDF2.format(utcTime.toInstant().atZone(ZONE_UTC));
	}

	/**
	 * 전달된 Date에 대하여 system의 Zone에 따른 시간을 'yyyyMMddHHmmss' 문자열 형태로 변환하여 반환한다.
	 * 
	 * @param date
	 * @return
	 */
	public static final String toLocalTimeyyyyMMddhhmmss(Date date) {
		Instant instant = date.toInstant();
		LocalDateTime ldt = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		return ldt.format(SDF1);
	}

	/**
	 * 특정 날짜에 year, month, day, hour를 더한 날짜를 반환한다.
	 * 
	 * @param date
	 *        the date, not null
	 * @param year
	 *        the year amount to add
	 * @param month
	 *        the month amount to add
	 * @param day
	 *        the day amount to add
	 * @param hour
	 *        the hour amount to add
	 * @return
	 */
	public static Date addDateLocal(Date date,
									int year,
									int month,
									int day,
									int hour) {
		Calendar ret = Calendar.getInstance(DEFAULT_TIME_ZONE);
		ret.setTime(date);

		ret.add(Calendar.YEAR, year);
		ret.add(Calendar.MONTH, month);
		ret.add(Calendar.DAY_OF_YEAR, day);
		ret.add(Calendar.HOUR_OF_DAY, hour);

		return ret.getTime();
	}

	/**
	 * 특정 날짜에 year, month, day, hour, minute를 더한 날짜를 반환한다.
	 * 
	 * @param date
	 *        the date, not null
	 * @param year
	 *        the year amount to add
	 * @param month
	 *        the month amount to add
	 * @param day
	 *        the day amount to add
	 * @param hour
	 *        the hour amount to add
	 * @param minute
	 *        the minute amount to add
	 * @return
	 */
	public static Date addDateLocal(Date date,
									int year,
									int month,
									int day,
									int hour,
									int minute) {
		Calendar ret = Calendar.getInstance(DEFAULT_TIME_ZONE);
		ret.setTime(date);

		ret.add(Calendar.YEAR, year);
		ret.add(Calendar.MONTH, month);
		ret.add(Calendar.DAY_OF_YEAR, day);
		ret.add(Calendar.HOUR_OF_DAY, hour);
		ret.add(Calendar.MINUTE, minute);

		return ret.getTime();
	}

	/**
	 * 특정 날짜의 종료 날짜(Hour는 23, Minute, Second는 59, Millisecond의 값은 9999)를 반환한다.
	 * 
	 * @param date
	 *        종료 날짜를 얻고자 하는 대상 날짜
	 * @return
	 */
	public static final Date endOfDayLocal(Date date) {
		Calendar cal = Calendar.getInstance(DEFAULT_TIME_ZONE);
		cal.setTime(date);
		setEndOfDay(cal);
		return cal.getTime();
	}

	/**
	 * 특정 날짜의 종료 날짜(Hour는 23, Minute, Second는 59, Millisecond의 값은 9999)를 반환한다.
	 * 
	 * @param calender
	 *        종료 날짜를 얻고자 하는 대상 날짜
	 * @return
	 */
	public static final Date endOfDayLocal(Calendar calender) {
		setEndOfDay(calender);
		return calender.getTime();
	}

	/**
	 * {@resultCode Date} 를 "yyyyMMddHHmmss" 패턴의 날짜 형태로 변환하여 반환한다.
	 * 
	 * @param date
	 *        변환하고자 하는 date
	 * @return
	 */
	public static final String dateToString(Date date) {
		SimpleDateFormat transFormat = new SimpleDateFormat(DATE_PATTERN_14);
		return transFormat.format(date);
	}

	/**
	 * {@resultCode date} 를 전달된 패턴 날짜 형태로 변환하여 반환한다.
	 * 
	 * @param date
	 *        변환하고자 하는 date
	 * @param pattern
	 *        변환 포맷 패턴
	 * @return
	 */
	public static final String dateToString(Date date,
											String pattern) {
		SimpleDateFormat transFormat = new SimpleDateFormat(pattern);
		return transFormat.format(date);
	}

	private static void setEndOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	/**
	 * {@resultCode pattern} 형태로 구성된 문자열 형태의 날짜를 {@link Date} 형태로 변환하여 반환한다.
	 * 
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static final Date stringToDate(	String strDate,
											String pattern)
			throws ParseException {
		SimpleDateFormat transFormat = new SimpleDateFormat(pattern);
		Date date = transFormat.parse(strDate);
		return date;
	}

	/**
	 * 'yyyyMMddHHmmss' 형태로 구성된 문자열 형태의 날짜를 {@link Date} 형태로 변환하여 반환한다.
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static final Date stringToDate(String strDate) throws ParseException {
		SimpleDateFormat transFormat = new SimpleDateFormat(DATE_PATTERN_14);
		Date date = transFormat.parse(strDate);
		return date;
	}

	/**
	 * 주어진 시간에 대한 어제 시간을 구해 반환한다.
	 * <p>
	 * {@resultCode timeZone} 이 null인 경우 시스템의 timezone으로 설정된다.
	 * 
	 * @param date
	 *        어제 시간을 구하기 위한 상대적인 현재 시간
	 * @param timeZone
	 * @param setMidnight
	 *        자정으로의 변환 여부
	 * @return
	 */
	public static final Date getYesterDay(	Date date,
											TimeZone timeZone,
											boolean setMidnight) {
		if ( timeZone == null ) {
			timeZone = DEFAULT_TIME_ZONE;
		}
		Calendar cal = new GregorianCalendar(DEFAULT_TIME_ZONE);
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		if ( setMidnight ) {
			return getMidnight(cal.getTime(), timeZone);
		}
		return cal.getTime();
	}

	/**
	 * 주어진 시간에 대한 내일 시간을 구해 반환한다.
	 * <p>
	 * {@resultCode timeZone} 이 null인 경우 시스템의 timezone으로 설정된다.
	 * 
	 * @param date
	 * @param timeZone
	 * @param setMidnight
	 *        자정으로의 변환 여부
	 * @return
	 */
	public static final Date getTommorow(	Date date,
											TimeZone timeZone,
											boolean setMidnight) {
		if ( timeZone == null ) {
			timeZone = DEFAULT_TIME_ZONE;
		}
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.add(Calendar.DATE, +1);
		if ( setMidnight ) {
			return getMidnight(cal.getTime(), timeZone);
		}
		return cal.getTime();
	}

	public static final Date getMidnight(	Date date,
											TimeZone timeZone) {
		if ( timeZone == null ) {
			timeZone = DEFAULT_TIME_ZONE;
		}
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}