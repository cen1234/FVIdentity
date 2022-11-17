package com.fvbackground.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.common.Result;
import com.fvbackground.entity.Role;
import com.fvbackground.entity.RoleMenu;
import com.fvbackground.mapper.RoleMenuMapper;
import com.fvbackground.service.RoleMenuService;
import com.fvbackground.service.RoleService;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.SheetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Resource
    private RoleMenuMapper roleMenuMapper;
//    ------
//    查找全部
//    ------
    @GetMapping
    public List<Role> find() {
        return roleService.list();
    }

//    ------
//    分页查询
//    ------
    @GetMapping("page")
    public IPage<Role> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "1") String type) {
        IPage<Role> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        switch (type)
        {
            case "1":{
                queryWrapper.like("name",search);
                break;
            }
        }
        return roleService.page(page,queryWrapper);
    }

//    -----
//    新增|修改角色
//    -----
    @PostMapping
    public boolean save(@RequestBody Role role) {
        return roleService.saveUser(role);
    }

//    ------
//    删除角色
//    ------
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return roleService.deleteById(id);
    }

    @DeleteMapping
    public  boolean deleteByIds(@RequestParam Collection idlist) {
        return roleService.removeByIds(idlist);
    }

//    ------
//    导出角色
//    ------
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查出所有数据
        List<Role> list = roleService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("description","描述");
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
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        out.close();
        // 关闭writer，释放内存
        writer.close();

    }

//    ---------------
//    绑定角色与菜单的关系
//    ---------------
      @PostMapping("/roleMenu/{roleId}")
      public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds) {
          roleService.setRoleMenu(roleId,menuIds);
          return Result.success();
      }

//      ----------------
//      获取角色所绑定的菜单
//      ----------------
       @GetMapping("/roleMenu")
       public List<Integer> getRoleMenu(@RequestParam Integer roleId) {
            return roleMenuMapper.selectByRoleId(roleId);
       }

}
