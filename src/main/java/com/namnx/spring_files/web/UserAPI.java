package com.namnx.spring_files.web;

import com.namnx.spring_files.service.UserService;
import com.namnx.spring_files.service.export.TypeFileExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    /**
     *
     * http://localhost:8080/user/export?typeFileExport=CSV
     * http://localhost:8080/user/export?typeFileExport=EXCEL
     *
     * */
    @GetMapping("/export")
    public void exportUser(@RequestParam TypeFileExport typeFileExport,
                           HttpServletResponse response) {
        userService.exportUser(response, typeFileExport);
    }
}
