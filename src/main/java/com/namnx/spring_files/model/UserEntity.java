package com.namnx.spring_files.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private Long id;
    private String name;
    private String email;
    private String birthday;

}
