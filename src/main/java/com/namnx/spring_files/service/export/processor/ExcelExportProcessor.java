package com.namnx.spring_files.service.export.processor;

import com.namnx.spring_files.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

@Slf4j
public class ExcelExportProcessor<T> implements FileExportProcessor<T> {

    private static final int DEFAULT_COLUMN_WIDTH = 30;
    private static final int DEFAULT_FONT_HEIGHT = 72;
    private static final int DEFAULT_MULTIPLES_FOR_HEADER_HEIGHT = 1;

    @Override
    public void exportWithHeader(HttpServletResponse response,
                                 String[] lineHeader,
                                 Collection<T> listDataToWrite,
                                 Function<T, String[]> convertFunc,
                                 String fileName,
                                 String charSetEncoding) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            sheet.setDefaultColumnWidth(DEFAULT_COLUMN_WIDTH);
            CellStyle styleHeader = ExcelUtils.createStyleHeader(workbook);
            CellStyle styleCell = ExcelUtils.createStyleCell(workbook);
            styleCell.setFont(ExcelUtils.createFont(workbook, DEFAULT_FONT_HEIGHT));
            Row headerRow = sheet.createRow(0);
            headerRow.setHeight((short) (headerRow.getHeight() * DEFAULT_MULTIPLES_FOR_HEADER_HEIGHT));

            //write header
            for (int i = 0; i < lineHeader.length; i++) {
                Cell cellHeader = headerRow.createCell(i);
                cellHeader.setCellStyle(styleHeader);
                cellHeader.setCellValue(lineHeader[i]);
            }
            AtomicInteger numberOfRow = new AtomicInteger(1);
            listDataToWrite
                    .stream()
                    .map(convertFunc)
                    .forEach(lineData -> {
                        Row row = sheet.createRow(numberOfRow.get());
                        IntStream.range(0, lineData.length).forEach(i -> {
                            Cell cellHeader = row.createCell(i);
                            cellHeader.setCellValue(lineData[i]);
                        });
                        numberOfRow.getAndIncrement();
                    });

            response.setContentType("application/ms-excel; charset=UTF-8");
            response.setCharacterEncoding(charSetEncoding);
            String headerForFileName = "attachment; filename=" + generateFileNameWithTime(fileName) + ".xlsx";
            response.setHeader("Content-Disposition", headerForFileName);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("Error when export excel: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void exportWithoutHeader(HttpServletResponse response,
                                    Collection<T> listDataToWrite,
                                    Function<T, String[]> convertFunc,
                                    String fileName,
                                    String charSetEncoding) {
        //custom handle here
        throw new UnsupportedOperationException();
    }
}
