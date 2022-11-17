package com.fvbackground.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Single {
    private Integer id;
    private String logId;
    private String username;
    private String name;
    private double score;
    private String title;
    private String description;
    private String benefit;
    private double price;

    @TableField(exist = false)
    private String photo;

    @TableField(exist = false)
    private Integer resuleNum;
}
