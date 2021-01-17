
package com.zitlab.palmyra.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeParser {
	
	public static String defaultDateFormat = "yyyy-MM-dd";
	public static String defaultDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static String defaultTimeFormat = "HH:mm:ss";

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(defaultDateTimeFormat);
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(defaultTimeFormat);
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(defaultDateFormat);
	
	public static Date parseDate(String date) {
		LocalDate ldt = LocalDate.from(dateFormatter.parse(date));
		return Date.valueOf(ldt);
	}

	public static Timestamp parseDateTime(String date){
		LocalDateTime ldt = LocalDateTime.from(dateTimeFormatter.parse(date));
		return Timestamp.valueOf(ldt);
	}

	public static Time parseTime(String time) {
		LocalTime ldt = LocalTime.from(timeFormatter.parse(time));
		return Time.valueOf(ldt);
	}

	public static TemporalAccessor getTemporalAccessorTime(String value) {
		return timeFormatter.parse(value);
	}
	
	public static TemporalAccessor getTemporalAccessorDate(String value) {
		return dateFormatter.parse(value);
	}
	
	public static TemporalAccessor getTemporalAccessorDateTime(String value) {
		return dateTimeFormatter.parse(value);
	}
}
