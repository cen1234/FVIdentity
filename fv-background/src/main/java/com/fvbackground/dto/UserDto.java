package com.fvbackground.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fvbackground.entity.Menu;
import lombok.Data;

import java.util.List;

//接受前端接受请求的数据
@Data
public class UserDto {
    private String username;
    private String password;
    private String token;
    private String accessToken;

    @TableField(exist = false)
    private List<Menu> menus;
}
