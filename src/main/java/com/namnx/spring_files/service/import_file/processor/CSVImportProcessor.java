package com.namnx.spring_files.service.import_file.processor;

import com.namnx.spring_files.util.EncodingUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class CSVImportProcessor implements FileImportProcessor {

    @Override
    public List<String[]> readLines(MultipartFile csvFile,
                                    int numberColumnWantToSelect,
                                    Predicate<String[]> ignorePredicate) {
        try {
            List<String[]> listResult = new ArrayList<>();
            String charsetStr = EncodingUtils.detectEncoding(csvFile.getInputStream());
            Charset charset = charsetStr == null ? StandardCharsets.UTF_8 : Charset.forName(charsetStr);

            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream(), charset))
                    .withSkipLines(1)
                    .build();

            String[] lineValue;
            while ((lineValue = reader.readNext()) != null) {
                if (!ignorePredicate.test(lineValue)) {
                    String[] validLineValue = autoFillArrayIfNotFitSize(numberColumnWantToSelect, lineValue);
                    listResult.add(validLineValue);
                }
            }
            reader.close();
            return listResult;
        } catch (IOException | CsvValidationException e) {
            log.error("exception when reading csv file: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String[] autoFillArrayIfNotFitSize(int numberColumnWantToSelect,
                                               String[] arrayValue) {
        int sizeOfOriginalArray = arrayValue.length;
        if (sizeOfOriginalArray == numberColumnWantToSelect) {
            return arrayValue;
        }
        String[] result = new String[numberColumnWantToSelect];
        for (int i = 0; i < numberColumnWantToSelect; i++) {
            if (i >= sizeOfOriginalArray) {
                result[i] = "";
            } else {
                result[i] = arrayValue[i];
            }
        }
        return result;
    }
}
