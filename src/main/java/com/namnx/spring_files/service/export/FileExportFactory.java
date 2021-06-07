package com.namnx.spring_files.service.export;

import com.namnx.spring_files.service.export.processor.CSVExportProcessor;
import com.namnx.spring_files.service.export.processor.ExcelExportProcessor;
import com.namnx.spring_files.service.export.processor.FileExportProcessor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class FileExportFactory {

    private final Map<TypeFileExport, FileExportProcessor<?>> exportProcessors = new EnumMap<>(TypeFileExport.class);

    public <T> FileExportProcessor<T> createExportProcessor(TypeFileExport typeFileExport) {
        FileExportProcessor fileExportProcessor = exportProcessors.get(typeFileExport);
        if (fileExportProcessor == null) {
            if (typeFileExport == TypeFileExport.CSV) {
                fileExportProcessor = new CSVExportProcessor<>();
                exportProcessors.put(TypeFileExport.CSV, fileExportProcessor);

            } else if (typeFileExport == TypeFileExport.EXCEL) {
                fileExportProcessor = new ExcelExportProcessor<>();
                exportProcessors.put(TypeFileExport.EXCEL, fileExportProcessor);
            } else {
                throw new IllegalArgumentException("Invalid typeFileImport: " + typeFileExport);
            }
        }
        return fileExportProcessor;

    }
}
