package com.form76.generator.service;

import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.Employee;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static com.form76.generator.service.Form76ReportService.xlsxFile;

public class Form76XlsxReportBuilder {
  private final static Logger logger = LoggerFactory.getLogger(Form76XlsxReportBuilder.class.getName());

  public static final String FONT_NAME = "Arial";
  public static final String DATE_FORMAT = "dd-MM-yyyy";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String WORKED_HOURS_FORMAT = "%dh %dm";
  public static final short DEFAULT_FONT_HEIGHT = 10;
  public static final short SMALL_FONT_HEIGHT = 9;
  private static final String SHEET_NAME = "Форма 76";

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private CellStyle defaultStyle, boldStyle, alignRightStyle, dateStyle, dateTimeStyle,
      sheetHeaderCenterStyle, sheetHeaderStyle, dataTableStyle, dataTableHeaderStyle, dataTableHeaderRotatedStyle;

  private Workbook workbook;

  private ExcelSheet form76Sheet;

  private Map<String, Map<String, Employee>> employeesData = null;

  public void setEmployeesData(Map<String, Map<String, Employee>> employeesData) {
    this.employeesData = employeesData;
  }

  public Form76XlsxReportBuilder build() {
    createWorkbook();
    populateWorkbook();
    return this;
  }


  byte[] asByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    workbook.write(byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  FileOutputStream asFileOutputStream(String outputFileName) throws IOException {
    FileOutputStream reportFileOutputStream = new FileOutputStream(outputFileName);
    workbook.write(reportFileOutputStream);
    return reportFileOutputStream;
  }

  Workbook asWorkbook() {
    return workbook;
  }

  protected void createWorkbook() {
    if(xlsxFile){
      workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
    } else {
      workbook = new HSSFWorkbook();
    }

    createStyles();
  }

  protected void populateWorkbook() {
    List<Map<String, Employee>> list = employeesData.values().stream().toList();

    for(Map<String, Employee> map : list){
      List<Employee> empList = map.values().stream().toList();
      Date date = empList.get(0).doorEvents.get(0).timestamp;

      String monthYearStr = getMonthAndYerStrFromDate(date);

//      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//      calendar.setTime(date);
//      int monthNumber = calendar.get(Calendar.MONTH);
//
//      String monthName = switch (monthNumber) {
//        case 0 -> "Януари";
//        case 1 -> "Февруари";
//        case 2 -> "Март";
//        case 3 -> "Април";
//        case 4 -> "Май";
//        case 5 -> "Юни";
//        case 6 -> "Юли";
//        case 7 -> "Август";
//        case 8 -> "Септември";
//        case 9 -> "Октомври";
//        case 10 -> "Ноември";
//        case 11 -> "Декември";
//        default -> "Invalid month number";
//      };

      String sheetName = SHEET_NAME + " - " + monthYearStr;
      form76Sheet = createSheet(sheetName);
      populateForma76Sheet(map);
      form76Sheet.autoSizeColumns();

    }


  }

  private void populateForma76Sheet(Map<String, Employee> map) {
    int row = 0;
    int column = 0;

    form76Sheet.createCell(0, 0, "ПРИЛОЖЕНИЕ 1", boldStyle);
    form76Sheet.createEmptyCells(0, 1, 47, defaultStyle);

    form76Sheet.createCell(1, 0, "Предприятие:", defaultStyle);
    form76Sheet.createEmptyCells(1, 1, 4, defaultStyle);
    form76Sheet.createCell(1, 5, "ТАБЛИЦА", sheetHeaderCenterStyle);
    form76Sheet.createEmptyCells(1, 6, 33, defaultStyle);
    form76Sheet.mergeCells(new CellRangeAddress(1, 1, 5, 33));
    form76Sheet.createEmptyCells(1, 34, 37, defaultStyle);
    form76Sheet.createCell(1, 38, "Съставил:", defaultStyle);
    form76Sheet.createEmptyCells(1, 39, 47, defaultStyle);

    form76Sheet.createEmptyCells(2, 0, 4, defaultStyle);

    //prepared month & year string and use it on the row bellow

    //ArrayList<> arrayList = new ArrayList<>(employeesData.values());

    String monthYearStr = "................. г.";

    Iterator<Employee> employeeIterator = map.values().iterator();
    if (employeeIterator.hasNext()) {
      Employee firstEmployee = employeeIterator.next();

      DoorEvent firstDoorEvent = firstEmployee.getDoorEvents().get(0);
      Date timestamp = firstDoorEvent.timestamp;

      monthYearStr = getMonthAndYerStrFromDate(timestamp);
    }

    form76Sheet.createCell(2, 5, "за отчитане явяването/неявяването на работа през месец " + monthYearStr, sheetHeaderCenterStyle);
    form76Sheet.createEmptyCells(2, 6, 33, defaultStyle);
    form76Sheet.mergeCells(new CellRangeAddress(2, 2, 5, 33));

    form76Sheet.createCell(3, 0, "Адрес:", defaultStyle);
    form76Sheet.createEmptyCells(3, 1, 4, defaultStyle);
    form76Sheet.createCell(3, 5, "отдел:", sheetHeaderStyle);
    form76Sheet.createEmptyCells(3, 6, 33, defaultStyle);
    form76Sheet.mergeCells(new CellRangeAddress(3, 3, 5, 33));
    form76Sheet.createEmptyCells(3, 34, 47, defaultStyle);

    form76Sheet.createEmptyCells(4, 0, 4, defaultStyle);
    form76Sheet.createCell(4, 5, "дирекция:", sheetHeaderStyle);
    form76Sheet.createEmptyCells(4, 6, 33, defaultStyle);
    form76Sheet.mergeCells(new CellRangeAddress(4, 4, 5, 33));
    form76Sheet.createEmptyCells(4, 34, 47, defaultStyle);

    form76Sheet.createEmptyCells(5, 0, 47, defaultStyle);

    //actual data table
    form76Sheet.createCell(6, 0, "№ по ред", dataTableHeaderStyle);
    form76Sheet.createCell(6, 1, "Трите имена по документ за самоличност", dataTableHeaderStyle);
    form76Sheet.createCell(6, 2, "Длъжност", dataTableHeaderStyle);
    form76Sheet.createCell(6, 3, "ДНИ НА МЕСЕЦА", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(6, 4, 34, dataTableStyle);
    form76Sheet.createCell(6, 34, "Явяване на работа", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(6, 35, 35, dataTableStyle);
    form76Sheet.createCell(6, 36, "Неявявяния", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(6, 37, 42, dataTableStyle);
    form76Sheet.createCell(6, 43, "Отработени човекочасове", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(6, 44, 44, dataTableStyle);
    form76Sheet.createCell(6, 45, "Неотработени човекочасове", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(6, 46, 47, dataTableStyle);

    form76Sheet.createEmptyCells(7, 0, 2, dataTableStyle);
    IntStream.range(1, 32).forEachOrdered(n -> {
      form76Sheet.createCell(7, 2 + n, "" + n, dataTableHeaderStyle);
    });
    form76Sheet.createCell(7, 34, "Всичко човекодни", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 35, "в т.ч. целодневен престой", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 36, "Редовен отпуск", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 37, "Отпуск по майчинство", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 38, "Отпуск по болест", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 39, "Изп. държ. задължения", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 40, "С разреш. на администр.", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 41, "Самоотлъчка", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 42, "Празнични и почивни дни", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 43, "Всичко", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 44, "в т.ч. извънредни", dataTableHeaderRotatedStyle);
    form76Sheet.createCell(7, 45, "Частични", dataTableHeaderRotatedStyle);
    form76Sheet.createEmptyCells(7, 46, 47, dataTableStyle);

    form76Sheet.createEmptyCells(8, 0, 35, dataTableStyle);
    form76Sheet.createCell(8, 36, "О", dataTableHeaderStyle);
    form76Sheet.createCell(8, 37, "М", dataTableHeaderStyle);
    form76Sheet.createCell(8, 38, "Б", dataTableHeaderStyle);
    form76Sheet.createCell(8, 39, "Д", dataTableHeaderStyle);
    form76Sheet.createCell(8, 40, "А", dataTableHeaderStyle);
    form76Sheet.createCell(8, 41, "С", dataTableHeaderStyle);
    form76Sheet.createCell(8, 42, "Н", dataTableHeaderStyle);
    form76Sheet.createEmptyCells(8, 43, 47, dataTableStyle);

    form76Sheet.mergeCells(new CellRangeAddress(6, 6, 3, 33));
    form76Sheet.mergeCells(new CellRangeAddress(6, 6, 34, 35));
    form76Sheet.mergeCells(new CellRangeAddress(6, 6, 36, 42));
    form76Sheet.mergeCells(new CellRangeAddress(6, 6, 43, 44));
    form76Sheet.mergeCells(new CellRangeAddress(6, 6, 45, 47));

    form76Sheet.mergeCells(new CellRangeAddress(6, 8, 0, 0));
    form76Sheet.mergeCells(new CellRangeAddress(6, 8, 1, 1));
    form76Sheet.mergeCells(new CellRangeAddress(6, 8, 2, 2));
    IntStream.range(3, 36).forEachOrdered(n -> {
      form76Sheet.mergeCells(new CellRangeAddress(7, 8, n, n));
    });
    IntStream.range(43, 48).forEachOrdered(n -> {
      form76Sheet.mergeCells(new CellRangeAddress(7, 8, n, n));
    });

    List<Employee> orderedEmployees = new ArrayList<>(map.values());
    orderedEmployees.sort(Comparator.comparing(empl -> empl.id));

    row = 9;
    for (int i = 0; i < orderedEmployees.size(); i++) {
      addEmployeeRow(row, i, orderedEmployees.get(i));
      row++;
    }

    row++;
    form76Sheet.createEmptyCells(row, 0, 0, defaultStyle);
    form76Sheet.createCell(row, 1, "Дата: ", defaultStyle);
    form76Sheet.createEmptyCells(row, 2, 47, defaultStyle);

  }


  private void addEmployeeRow(int row, int indx, Employee employee) {
    form76Sheet.createCell(row, 0, "" + (indx + 1), dataTableHeaderStyle);
    form76Sheet.createCell(row, 1, /*employee.id + " " +*/ employee.names, dataTableStyle);
    form76Sheet.createEmptyCells(row, 2, 2, dataTableStyle);
    int colShift = 2;
    Map<String, Long> workedHoursPerDate = employee.workedHoursPerDate;
    for (int d = 1; d <= 31; d++) {
      Long workedHoursAsMillis = null;
      for (String dateStr : workedHoursPerDate.keySet()) {
        if (LocalDate.parse(dateStr).getDayOfMonth() == d) {
          workedHoursAsMillis = workedHoursPerDate.get(dateStr);
          break;
        }
      }

      String workedHoursValue = workedHoursAsMillis != null ? getDurationString(workedHoursAsMillis) : "0h 0m";
      form76Sheet.createCell(row, colShift + d, workedHoursValue, dataTableStyle);
    }
    form76Sheet.createEmptyCells(row, 34, 47, dataTableStyle);

  }

  private void createStyles() {
    Font defaultFont = workbook.createFont();
    defaultFont.setFontHeightInPoints(DEFAULT_FONT_HEIGHT);
    defaultFont.setFontName(FONT_NAME);

    Font smallFont = workbook.createFont();
    smallFont.setFontHeightInPoints(SMALL_FONT_HEIGHT);
    smallFont.setFontName(FONT_NAME);

    Font boldFont = workbook.createFont();
    boldFont.setFontHeightInPoints(DEFAULT_FONT_HEIGHT);
    boldFont.setFontName(FONT_NAME);
    boldFont.setBold(true);

    Font smallBoldFont = workbook.createFont();
    smallBoldFont.setFontHeightInPoints(SMALL_FONT_HEIGHT);
    smallBoldFont.setFontName(FONT_NAME);
    smallBoldFont.setBold(true);

    defaultStyle = workbook.createCellStyle();
    defaultStyle.setFont(defaultFont);

    dataTableStyle = workbook.createCellStyle();
    dataTableStyle.setFont(smallFont);
    dataTableStyle.setBorderTop(BorderStyle.THIN);
    dataTableStyle.setBorderRight(BorderStyle.THIN);
    dataTableStyle.setBorderBottom(BorderStyle.THIN);
    dataTableStyle.setBorderLeft(BorderStyle.THIN);
    dataTableStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    dataTableStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    dataTableStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    dataTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

    dataTableHeaderStyle = workbook.createCellStyle();
    dataTableHeaderStyle.setFont(smallBoldFont);
    dataTableHeaderStyle.setBorderTop(BorderStyle.THIN);
    dataTableHeaderStyle.setBorderRight(BorderStyle.THIN);
    dataTableHeaderStyle.setBorderBottom(BorderStyle.THIN);
    dataTableHeaderStyle.setBorderLeft(BorderStyle.THIN);
    dataTableHeaderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

    dataTableHeaderRotatedStyle = workbook.createCellStyle();
    dataTableHeaderRotatedStyle.setFont(smallBoldFont);
    dataTableHeaderRotatedStyle.setBorderTop(BorderStyle.THIN);
    dataTableHeaderRotatedStyle.setBorderRight(BorderStyle.THIN);
    dataTableHeaderRotatedStyle.setBorderBottom(BorderStyle.THIN);
    dataTableHeaderRotatedStyle.setBorderLeft(BorderStyle.THIN);
    dataTableHeaderRotatedStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderRotatedStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderRotatedStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderRotatedStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    dataTableHeaderRotatedStyle.setRotation((short)90);

    boldStyle = workbook.createCellStyle();
    boldStyle.setFont(boldFont);


    dateStyle = workbook.createCellStyle();
    dateStyle.setFont(defaultFont);
    dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(DATE_FORMAT));


    dateTimeStyle = workbook.createCellStyle();
    dateTimeStyle.setFont(defaultFont);
    dateTimeStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(DATE_TIME_FORMAT));


    alignRightStyle = workbook.createCellStyle();
    alignRightStyle.setFont(defaultFont);
    alignRightStyle.setAlignment(HorizontalAlignment.RIGHT);


    sheetHeaderCenterStyle = workbook.createCellStyle();
    sheetHeaderCenterStyle.setFont(boldFont);
    sheetHeaderCenterStyle.setAlignment(HorizontalAlignment.CENTER);

    sheetHeaderStyle = workbook.createCellStyle();
    sheetHeaderStyle.setFont(boldFont);
    sheetHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
    //tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    //tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  }

  private String getDurationString(Long durationMillis) {
    Duration duration = Duration.ofSeconds(durationMillis / 1000);
    return String.format(WORKED_HOURS_FORMAT, duration.toHoursPart(), duration.toMinutesPart());

  }

  protected ExcelSheet createSheet(String sheetName) {
    return new ExcelSheet((XSSFSheet) workbook.createSheet(sheetName));
  }


  // utility wrapper class to simplify sheet manipulation and cells creation;
  protected class ExcelSheet {
    final XSSFSheet sheet;

    ExcelSheet(XSSFSheet sheet) {
      this.sheet = sheet;
    }

    void createCell(int rowNumber, int columnNumber, Object value, CellStyle overrideStyle) {
      Row row = sheet.getRow(rowNumber);
      if (row == null) {
        row = sheet.createRow(rowNumber);
      }

      Cell cell = row.createCell(columnNumber);
      if (value != null) {
        if (value instanceof String) {
          cell.setCellValue(value.toString());
          cell.setCellStyle(overrideStyle != null ? overrideStyle : defaultStyle);
        } else if (value instanceof BigDecimal) {
          cell.setCellValue(((BigDecimal)value).doubleValue());
          cell.setCellStyle(overrideStyle != null ? overrideStyle : defaultStyle);
        } else if (value instanceof Date) {
          cell.setCellValue(DateUtils.truncate(((Date)value), Calendar.DATE));
          cell.setCellStyle(overrideStyle != null ? overrideStyle : dateStyle);

        }
      }
    }

    void createEmptyCells(final int row, final int firstCol, final int lastCol, final CellStyle style) {
      IntStream.range(firstCol, lastCol + 1).forEachOrdered(n -> {
        form76Sheet.createCell(row, n, "    ", style);
      });
    }

    void autoSizeColumns() {
      for (int i = 0; i < 78; i++) {
        sheet.autoSizeColumn(i, true);
      }
    }

    void mergeCells(CellRangeAddress region) {
      sheet.addMergedRegion(region);
    }

  }

  private String getMonthAndYerStrFromDate(Date timestamp) {
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.setTime(timestamp);

    int yearNumber = calendar.get(Calendar.YEAR);
    int monthNumber = calendar.get(Calendar.MONTH);

    String monthName = switch (monthNumber) {
      case 0 -> "Януари";
      case 1 -> "Февруари";
      case 2 -> "Март";
      case 3 -> "Април";
      case 4 -> "Май";
      case 5 -> "Юни";
      case 6 -> "Юли";
      case 7 -> "Август";
      case 8 -> "Септември";
      case 9 -> "Октомври";
      case 10 -> "Ноември";
      case 11 -> "Декември";
      default -> "Invalid month number";
    };
    return monthName + " " + yearNumber;
  }
}
