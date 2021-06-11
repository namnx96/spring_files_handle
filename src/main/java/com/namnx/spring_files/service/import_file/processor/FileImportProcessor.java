package com.namnx.spring_files.service.import_file.processor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Predicate;

public interface FileImportProcessor {

    List<String[]> readLines(MultipartFile importFile,
                             int numberColumnWantToSelect,
                             Predicate<String[]> ignorePredicate);
}
