package com.fvbackground.entity;

import lombok.Data;

@Data
public class Picture {
    private Integer id;
    private String username;
    private String name;
    private String type;
    private long size;
    private String url;
}
