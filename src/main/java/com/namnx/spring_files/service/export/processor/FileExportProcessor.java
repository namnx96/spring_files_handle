package com.namnx.spring_files.service.export.processor;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

public interface FileExportProcessor<T> {

    void exportWithHeader(HttpServletResponse response,
                          String[] lineHeader,
                          Collection<T> listDataToWrite,
                          Function<T, String[]> convertFunc,
                          String fileName,
                          String charSetEncoding);

    void exportWithoutHeader(HttpServletResponse response,
                             Collection<T> listDataToWrite,
                             Function<T, String[]> convertFunc,
                             String fileName,
                             String charSetEncoding);

    default String generateFileNameWithTime(String fileName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return fileName + "_" + dateFormat.format(new Date());
    }

}
