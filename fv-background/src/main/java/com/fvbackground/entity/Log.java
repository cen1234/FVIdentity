package com.fvbackground.entity;

import lombok.Data;

@Data
public class Log {
    private Integer id;
    private String recordId;
    private String username;
    private String path;
    private String photo;
    private Integer resuleNum;
}
