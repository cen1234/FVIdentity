package com.fvbackground.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.common.Constants;
import com.fvbackground.common.Result;
import com.fvbackground.dto.UserDto;
import com.fvbackground.entity.User;
import com.fvbackground.service.UserService;
import com.fvbackground.utils.TokenUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.SheetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
//      ------
//       登录
//      ------
    @PostMapping("/login")
    private Result Login(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        //校验username,password是否为空
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数不足!");
        }
        //在数据库中查到用户信息，给前台返回一些用户的信息存储到浏览器上
        UserDto userDto1 = userService.login(userDto);
        return Result.success(userDto1);
    }

//       -----
//       注册
//       -----
        @PostMapping("/register")
        public Result Register(@RequestBody UserDto userDto) {
            String username = userDto.getUsername();
            String password = userDto.getPassword();
            //校验username,password是否为空
            if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
                return Result.error(Constants.CODE_400,"参数不足!");
            }
            //在数据库中添加注册的用户信息
            return Result.success(userService.register(userDto));
        }


//    ------
//    查找全部
//    ------

    public List<User> find() {
        return userService.list();
    }

//    ------
//    分页查询
//    使用mybatis-plus方法进行分页查询
//    @RequestParam接收： ？pageNum=1&pageSize=10
//    设置page条件：哪一页和页的数据多少条；
//    设置查询条件：queryWrapper；
//    用户可以根据 username，realname，email，phone这些关键词进行模糊查询，并带上type
//    ------
    @GetMapping("page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "1") String type) {
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        switch (type)
        {
            case "1":{
                queryWrapper.like("username",search);
                break;
            }
            case "2":{
               queryWrapper.like("realname",search);
               break;
            }
           case "3":{
                queryWrapper.like("email",search);
               break;
           }
            case "4":{
                queryWrapper.like("phone",search);
                break;
           }
        }
        //获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        return userService.page(page,queryWrapper);
    }

//    ------
//    新增|修改用户
//    ------
    @Autowired
    private UserService userService;

    @PostMapping
    //@RequestBody将前台传过来的参数转化为User对象
    public boolean save(@RequestBody User user) {
            return userService.saveUser(user);
    }


//      ------
//      删除用户
//      ------
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return userService.deleteById(id);
    }

    @DeleteMapping
    public  boolean deleteByIds(@RequestParam Collection idlist) {
        return userService.removeByIds(idlist);
    }

//    -----
//    导出用户信息
//    -----
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查出所有数据
        List<User> list = userService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("role","角色");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("realname","真实姓名");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("address","地址");
        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //定义单元格背景色
        StyleSet style = writer.getStyleSet();
        //设置内容字体
        Font font = writer.createFont();
        font.setFontHeightInPoints((short) 12);
        //重点，设置中文字体
        font.setFontName("宋体");
        //第二个参数表示是否忽略头部样式
        style.getHeadCellStyle().setFont(font);
        int columnCount = writer.getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            double width = SheetUtil.getColumnWidth(writer.getSheet(), i, false);
            if (width != -1.0D) {
                width *= 256.0D;
                //此处可以适当调整，调整列空白处宽度
                width += 220D;
                writer.setColumnWidth(i, Math.toIntExact(Math.round(width / 256D)));
            }
        }
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("角色信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        out.close();
        // 关闭writer，释放内存
        writer.close();

    }
}
