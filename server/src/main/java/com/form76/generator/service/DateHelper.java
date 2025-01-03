package com.form76.generator.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
  public static final String REPORT_DATA_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";

  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_REPORT_DATA_DATE_TIME = new SimpleDateFormat(REPORT_DATA_DATE_TIME_FORMAT);

  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_YEAR_MONTH_EXTRACT = new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT);


  public static Date parseReportDate(String dateStr) throws ParseException {
    return SIMPLE_DATE_FORMAT_FOR_REPORT_DATA_DATE_TIME.parse(dateStr);
  }

  public static String formatReportDate(Date date) {
    return SIMPLE_DATE_FORMAT_FOR_REPORT_DATA_DATE_TIME.format(date);
  }

  public static String getYearAndMonthFromDateString(String dateStr) throws ParseException {
    return getYearAndMonthFromDate(parseReportDate(dateStr));
  }

  public static String getYearAndMonthFromDate(Date date) {
    return SIMPLE_DATE_FORMAT_FOR_YEAR_MONTH_EXTRACT.format(date);
  }

}
