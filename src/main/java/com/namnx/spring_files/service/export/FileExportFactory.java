package com.namnx.spring_files.service.export;

import com.namnx.spring_files.enums.TypeFile;
import com.namnx.spring_files.service.export.processor.CSVExportProcessor;
import com.namnx.spring_files.service.export.processor.ExcelExportProcessor;
import com.namnx.spring_files.service.export.processor.FileExportProcessor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class FileExportFactory {

    private final Map<TypeFile, FileExportProcessor<?>> exportProcessors = new EnumMap<>(TypeFile.class);

    public <T> FileExportProcessor<T> createExportProcessor(TypeFile typeFileExport) {
        FileExportProcessor fileExportProcessor = exportProcessors.get(typeFileExport);
        if (fileExportProcessor == null) {
            if (typeFileExport == TypeFile.CSV) {
                fileExportProcessor = new CSVExportProcessor<>();
                exportProcessors.put(TypeFile.CSV, fileExportProcessor);

            } else if (typeFileExport == TypeFile.EXCEL) {
                fileExportProcessor = new ExcelExportProcessor<>();
                exportProcessors.put(TypeFile.EXCEL, fileExportProcessor);
            } else {
                throw new IllegalArgumentException("Invalid typeFileExport: " + typeFileExport);
            }
        }
        return fileExportProcessor;

    }
}
