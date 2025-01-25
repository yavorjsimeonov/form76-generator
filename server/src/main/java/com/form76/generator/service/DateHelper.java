package com.form76.generator.service;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
  public static final String REPORT_DATA_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String REPORT_FILE_NAME_TIMESTAMPS_FORMAT = "yyyyMMddHHmmss";


  public static final DateTimeFormatter REPORT_DATA_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(REPORT_DATA_DATE_TIME_FORMAT);
  public static final DateTimeFormatter YEAR_MONTH_DATE_FORMATTER = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT);

  public static Date parseReportDate(String dateStr) throws ParseException {
    return new SimpleDateFormat(REPORT_DATA_DATE_TIME_FORMAT).parse(dateStr);
  }

  public static String formatReportDate(Date date) {
    return new SimpleDateFormat(REPORT_DATA_DATE_TIME_FORMAT).format(date);
  }

  public static Date parseDate(String dateStr) throws ParseException {
    return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
  }

  public static String toDateString(Date timestamp) {
    return new SimpleDateFormat(DATE_FORMAT).format(DateUtils.truncate(timestamp, Calendar.DATE));
  }

  public static String getYearAndMonthFromDateString(String dateStr) throws ParseException {
    return getYearAndMonthFromDate(parseReportDate(dateStr));
  }

  public static String getYearAndMonthFromDate(Date date) {
    return new SimpleDateFormat(YEAR_MONTH_FORMAT).format(date);
  }

  public static String getTimeStampForReportFile(Date date) {
    return new SimpleDateFormat(REPORT_FILE_NAME_TIMESTAMPS_FORMAT).format(date);
  }
}
