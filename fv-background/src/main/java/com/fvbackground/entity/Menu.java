package com.fvbackground.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.List;

@Data
public class Menu {
     private Integer id;
     private Integer pid;
     private String  name;
     private String  description;
     private String  path;
     private String  icon;

     @TableField(exist = false)
     private List<Menu> children;
}
