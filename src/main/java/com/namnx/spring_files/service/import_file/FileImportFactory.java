package com.namnx.spring_files.service.import_file;

import com.namnx.spring_files.enums.TypeFile;

import com.namnx.spring_files.service.import_file.processor.CSVImportProcessor;
import com.namnx.spring_files.service.import_file.processor.ExcelImportProcessor;
import com.namnx.spring_files.service.import_file.processor.FileImportProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class FileImportFactory {


    private final Map<TypeFile, FileImportProcessor> importProcessors = new EnumMap<>(TypeFile.class);

    public Optional<FileImportProcessor> createImportProcessor(MultipartFile importFile) {
        String fileName = importFile.getOriginalFilename();
        if (fileName == null) {
            log.error("file import is null name");
            return Optional.empty();
        }
        Optional<TypeFile> typeFileOptional = fromFileName(fileName);
        if (typeFileOptional.isEmpty()) {
            log.error("Invalid typeFile with fileName: {}", fileName);
            return Optional.empty();
        }

        TypeFile typeFileImport = typeFileOptional.get();
        FileImportProcessor importProcessor = importProcessors.get(typeFileImport);
        if (importProcessor == null) {
            if (typeFileImport == TypeFile.CSV) {
                importProcessor = new CSVImportProcessor();
                importProcessors.put(TypeFile.CSV, importProcessor);

            } else {
                importProcessor = new ExcelImportProcessor();
                importProcessors.put(TypeFile.EXCEL, importProcessor);
            }
        }
        return Optional.of(importProcessor);

    }

    private Optional<TypeFile> fromFileName(String fileName) {
        if (fileName.endsWith(".csv")) {
            return Optional.of(TypeFile.CSV);
        }
        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return Optional.of(TypeFile.EXCEL);
        }
        return Optional.empty();
    }
}
