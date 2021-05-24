package com.elcom.uploadservice.service;

import com.elcom.uploadservice.model.dto.MediaRawDTO;
import com.elcom.uploadservice.model.dto.MediaRawRelationDTO;
import com.elcom.uploadservice.utils.DateUtil;
import com.elcom.uploadservice.utils.StringUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anhdv
 */
public class ExcelService {
    
    static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);
    static final String TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
    
    public static void writeMediaRawToExcel(List<MediaRawDTO> mediaRaws, File excelFile) {
        try {
            if( mediaRaws==null || mediaRaws.isEmpty() )
                return;
            
            Workbook workbook;
            if (excelFile.getName().endsWith("xlsx"))
                workbook = new XSSFWorkbook();
            else // create xls file
                workbook = new HSSFWorkbook();
            
            Sheet sheet = workbook.createSheet();
            
            sheet.autoSizeColumn(0);
//            sheet.setDefaultColumnWidth(11);

            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);
            
            // Tạo header row
            int indexCell = 0;
            Row headerRow = sheet.createRow(0);
            
            Cell cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("STT");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Thời gian");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Loại media");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Loại nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tên nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Loại đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tên đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("IP nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("IP đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("SĐT nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("SĐT đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Port nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Port đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("ID nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("ID đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("File");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Dung lượng");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Nguồn thu");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Chiều dữ liệu");
            
            //  Tạo raws
            int rowCount = 0;
            
            cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            
            for (MediaRawDTO mediaRaw : mediaRaws) {
                Row row = sheet.createRow(++rowCount);
                writeMediaRow(mediaRaw, row, rowCount, cellStyle);
            }

            // Ghi file
            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            }
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
        }
    }
    
    private static void writeMediaRow(MediaRawDTO mediaRaw, Row row, int indexRaw, CellStyle cellStyle) {
        try {
            int indexCell = 0;
            Cell cell = row.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(indexRaw);

            cell = row.createCell(indexCell++);
            cell.setCellValue(DateUtil.toString(new Date(mediaRaw.getEventTime()), TIME_FORMAT));

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getMediaTypeName());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSrcIsHq() == 1 ? "Bờ" : "Trên biển");

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSrcName());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestIsHq() == 1 ? "Bờ" : "Trên biển");

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestName());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourceIp());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestIp());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourcePhone());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestPhone());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourcePort());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestPort());

            cell = row.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(mediaRaw.getSrcId());

            cell = row.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(mediaRaw.getDestId());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getFilePathLocal());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getFileSize());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDataSourceName());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDirection() == 1 ? "Chiều đi" : mediaRaw.getDirection() == 2 ? "Chiều về" : "Không xác định");
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
        }
    }
    
    public static void writeMediaRawRelationToExcel(List<MediaRawRelationDTO> mediaRaws, File excelFile) {
        try {
            if( mediaRaws==null || mediaRaws.isEmpty() )
                return;
            
            Workbook workbook;
            if (excelFile.getName().endsWith("xlsx"))
                workbook = new XSSFWorkbook();
            else // create xls file
                workbook = new HSSFWorkbook();
            
            Sheet sheet = workbook.createSheet();
            
            sheet.autoSizeColumn(0);
//            sheet.setDefaultColumnWidth(11);

            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);
            
            // Tạo header row
            int indexCell = 0;
            Row headerRow = sheet.createRow(0);
            
            Cell cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("STT");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Thời gian");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Loại media");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tên nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tên đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("IP nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("IP đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("SĐT nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("SĐT đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Port nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Port đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("ID nguồn");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("ID đích");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("File");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Dung lượng");
            
            cell = headerRow.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("Nguồn thu");
            
            //  Tạo raws
            int rowCount = 0;
            
            cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setWrapText(true);
            
            for (MediaRawRelationDTO mediaRaw : mediaRaws) {
                Row row = sheet.createRow(++rowCount);
                row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
                writeMediaRowRelation(mediaRaw, row, rowCount, cellStyle, sheet);
            }

            // Ghi file
            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            }
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
        }
    }
    
    private static void writeMediaRowRelation(MediaRawRelationDTO mediaRaw, Row row, int indexRaw, CellStyle cellStyle, Sheet sheet) {
        try {
            int indexCell = 0;
            Cell cell = row.createCell(indexCell++);
            cell.setCellValue(indexRaw);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(DateUtil.toString(new Date(mediaRaw.getEventTimeFrom()), TIME_FORMAT)
                    + " \r\n " + DateUtil.toString(new Date(mediaRaw.getEventTimeTo()), TIME_FORMAT));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getMediaTypeNameFrom() + " \r\n " + mediaRaw.getMediaTypeNameTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSrcNameFrom() + " \r\n " + mediaRaw.getSrcNameTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestNameFrom() + " \r\n " + mediaRaw.getDestNameTo());

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourceIpFrom() + " \r\n " + mediaRaw.getSourceIpTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestIpFrom() + " \r\n " + mediaRaw.getDestIpTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourcePhoneFrom() + " \r\n " + mediaRaw.getSourcePhoneTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestPhoneFrom() + " \r\n " + mediaRaw.getDestPhoneTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getSourcePortFrom() + " \r\n " + mediaRaw.getSourcePortTo());
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDestPortFrom() + " \r\n " + mediaRaw.getDestPortTo());
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(mediaRaw.getSrcIdFrom()!=null?mediaRaw.getSrcIdFrom():0
                                + " \r\n " + mediaRaw.getSrcIdTo()!=null?mediaRaw.getSrcIdTo():0);

            cell = row.createCell(indexCell++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(mediaRaw.getDestIdFrom()!=null?mediaRaw.getDestIdFrom():0
                                + " \r\n " + mediaRaw.getDestIdTo()!=null?mediaRaw.getDestIdTo():0);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getFilePathLocalFrom()+ " \r\n " + mediaRaw.getFilePathLocalTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getFileSizeFrom() + " \r\n " + mediaRaw.getFileSizeTo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(indexCell++);
            cell.setCellValue(mediaRaw.getDataSourceNameFrom() + " \r\n " + mediaRaw.getDataSourceNameTo());
            cell.setCellStyle(cellStyle);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
    }
}
