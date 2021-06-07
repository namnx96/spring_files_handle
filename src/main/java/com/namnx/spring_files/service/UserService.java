package com.namnx.spring_files.service;

import com.namnx.spring_files.model.UserEntity;
import com.namnx.spring_files.service.export.FileExportFactory;
import com.namnx.spring_files.service.export.TypeFileExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private FileExportFactory fileExportFactory;

    private static final List<UserEntity> userEntities = new ArrayList<>();

    @PostConstruct
    void mockData() {
        UserEntity user1 = new UserEntity(1L, "nam@gmail.com", "nam nx", "2020/12/10");
        UserEntity user2 = new UserEntity(2L, "dsk@gmail.com", "da sun kid", "2010/06/15");
        userEntities.add(user1);
        userEntities.add(user2);
    }

    public void exportUser(HttpServletResponse response, TypeFileExport typeFileExport) {
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

}
