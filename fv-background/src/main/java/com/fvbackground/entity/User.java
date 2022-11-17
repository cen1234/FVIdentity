package com.fvbackground.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

//使用lombok帮助简化代码，可以不写set,get方法
@Data
public class User {
//    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String role;
    private String password;
    private String realname;
    private String sex;
    private Integer age;
    private String email;
    private String phone;
    private String address;

    @TableField(exist = false)
    private List<Menu> menus;

}
