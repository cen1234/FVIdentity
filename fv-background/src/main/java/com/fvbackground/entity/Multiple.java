package com.fvbackground.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

@Data
public class Multiple {
    private Integer id;
    private String logId;
    private String username;
    private String name;
    private double score;
    private String type;

    @TableField(exist = false)
    private String photo;

    @TableField(exist = false)
    private Integer resuleNum;

    @TableField(exist = false)
    private List<Multiple> result;
}
