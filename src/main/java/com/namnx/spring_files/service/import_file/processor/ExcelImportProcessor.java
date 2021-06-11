package com.namnx.spring_files.service.import_file.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class ExcelImportProcessor implements FileImportProcessor {

    @Override
    public List<String[]> readLines(MultipartFile excelFile,
                                    int numberColumnWantToSelect,
                                    Predicate<String[]> ignorePredicate) {

        List<String[]> listResult = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet datatypeSheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> iterator = datatypeSheet.iterator();
            iterator.next();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                String[] rowValues = getRowValues(numberColumnWantToSelect, currentRow, dataFormatter);
                if (!ignorePredicate.test(rowValues)) {
                    listResult.add(rowValues);
                }
            }
            return listResult;
        } catch (IOException | InvalidFormatException e) {
            log.error("exception when reading file excel: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String[] getRowValues(int numberColumnWantToSelect, Row currentRow, DataFormatter dataFormatter) {
        String[] rowValue = new String[numberColumnWantToSelect];
        for (int i = 0; i < numberColumnWantToSelect; i++) {
            Cell cell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            rowValue[i] = dataFormatter.formatCellValue(cell);
        }
        return rowValue;
    }
}
