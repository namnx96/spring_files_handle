package com.namnx.spring_files.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;

@UtilityClass
public class ExcelUtils {

    public CellStyle createStyleHeader(Workbook workbook) {
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setWrapText(true);
        styleHeader.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return styleHeader;
    }


    public CellStyle createStyleCell(Workbook workbook) {
        CellStyle styleCell = workbook.createCellStyle();
        styleCell.setWrapText(true);
        return styleCell;
    }

    public Font createFont(Workbook workbook, int fontSize) {
        Font newFont = workbook.createFont();
        newFont.setFontHeightInPoints((short) fontSize);
        return newFont;
    }
}
