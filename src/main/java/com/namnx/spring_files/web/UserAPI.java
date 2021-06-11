package com.namnx.spring_files.web;

import com.namnx.spring_files.service.UserService;
import com.namnx.spring_files.enums.TypeFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    /**
     * http://localhost:8080/user/export?typeFileExport=CSV
     * http://localhost:8080/user/export?typeFileExport=EXCEL
     */
    @GetMapping("/export")
    public void exportUser(@RequestParam TypeFile typeFileExport,
                           HttpServletResponse response) {
        userService.exportUser(response, typeFileExport);
    }

    @ApiOperation("you can use the file which is exported by the exportAPI for this")
    @PostMapping("/import")
    public ResponseEntity<List<String[]>> importUser(@RequestPart MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.readFileImportUser(multipartFile));
    }

}
