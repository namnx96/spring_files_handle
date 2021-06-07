package com.namnx.spring_files.service.export.processor;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

import static com.opencsv.ICSVWriter.*;

@Slf4j
public class CSVExportProcessor<T> implements FileExportProcessor<T> {

    private CSVWriter getWriter(HttpServletResponse response,
                                String fileName,
                                String charSetEncoding) throws IOException {
        response.setContentType("application/CSV");
        response.setCharacterEncoding(charSetEncoding);
        String headerForFile = "attachment; filename=\"" + generateFileNameWithTime(fileName) + ".csv\"";
        response.setHeader("Content-Disposition", headerForFile);
        response.setHeader("Content-type", "application/csv; charset=utf-8");

        // Tell excel read csv as UTF-8
        response.getWriter().write('\ufeff');
        return new CSVWriter(
                response.getWriter(),
                DEFAULT_SEPARATOR,
                NO_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER,
                DEFAULT_LINE_END
        );
    }

    @Override
    public void exportWithHeader(HttpServletResponse response,
                                 String[] lineHeader,
                                 Collection<T> listDataToWrite,
                                 Function<T, String[]> convertFunc,
                                 String fileName,
                                 String charSetEncoding) {
        try {
            CSVWriter csvWriter = getWriter(response, fileName, charSetEncoding);
            csvWriter.writeNext(lineHeader);
            writeToCSV(csvWriter, listDataToWrite, convertFunc);
        } catch (IOException e) {
            log.error("ERROR when export csv : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void exportWithoutHeader(HttpServletResponse response,
                                    Collection<T> listDataToWrite,
                                    Function<T, String[]> convertFunc,
                                    String fileName,
                                    String charSetEncoding) {
        try {
            CSVWriter csvWriter = getWriter(response, fileName, charSetEncoding);
            writeToCSV(csvWriter, listDataToWrite, convertFunc);
        } catch (IOException e) {
            log.error("ERROR when export csv : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void writeToCSV(CSVWriter csvWriter,
                            Collection<T> listDataToWrite,
                            Function<T, String[]> convertFunc) throws IOException {
        listDataToWrite
                .stream()
                .map(convertFunc)
                .forEach(csvWriter::writeNext);
        csvWriter.close();
    }
}
