package com.namnx.spring_files.service;

import com.namnx.spring_files.model.UserEntity;
import com.namnx.spring_files.service.export.FileExportFactory;
import com.namnx.spring_files.enums.TypeFile;
import com.namnx.spring_files.service.import_file.FileImportFactory;
import com.namnx.spring_files.service.import_file.processor.FileImportProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class UserService {

    @Autowired
    private FileExportFactory fileExportFactory;

    @Autowired
    private FileImportFactory fileImportFactory;

    private static final List<UserEntity> userEntities = new ArrayList<>();

    @PostConstruct
    void mockData() {
        userEntities.clear();
        UserEntity user1 = new UserEntity(1L, "nam@gmail.com", "nam nx", "2020/12/10");
        UserEntity user2 = new UserEntity(2L, "dsk@gmail.com", "da sun kid", "2010/06/15");
        UserEntity user3 = new UserEntity(3L, "cuong@gmail.com", "johnson", "2010/06/15");
        UserEntity user4 = new UserEntity(4L, "tuan@gmail.com", "ta tuan", "2010/06/15");
        userEntities.add(user1);
        userEntities.add(user2);
        userEntities.add(user3);
        userEntities.add(user4);
    }

    public void exportUser(HttpServletResponse response, TypeFile typeFileExport) {
        //perhaps using a resultSet of users from DB
        String[] header = {"ID", "NAME", "EMAIL", "BIRTHDAY"};
        String fileName = "user_export";
        fileExportFactory
                .<UserEntity>createExportProcessor(typeFileExport)
                .exportWithHeader(
                        response,
                        header,
                        userEntities,
                        this::toStringArray,
                        fileName,
                        StandardCharsets.UTF_8.name()
                );
    }

    private String[] toStringArray(UserEntity u) {
        String[] arr = new String[4];
        arr[0] = u.getId().toString();
        arr[1] = u.getName();
        arr[2] = u.getEmail();
        arr[3] = u.getBirthday();
        return arr;
    }

    //logic import example
    public List<String[]> readFileImportUser(MultipartFile fileImportUser) {
        Optional<FileImportProcessor> fileImportProcessorOptional =
                fileImportFactory.createImportProcessor(fileImportUser);
        if (fileImportProcessorOptional.isEmpty()) {
            return Collections.emptyList();
        }
        int numberColumnWantToSelect = 4;

        //ignore user has id = 4
        Predicate<String[]> ignorePredicate = lineVal -> lineVal[0].equals("4");
        return fileImportProcessorOptional
                .get()
                .readLines(fileImportUser, numberColumnWantToSelect, ignorePredicate);
        //do other stuffs with listLines from file and return another result type.
    }

}
