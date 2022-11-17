package com.fvbackground.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.common.Constants;
import com.fvbackground.entity.Dist;
import com.fvbackground.entity.Menu;
import com.fvbackground.mapper.DistMapper;
import com.fvbackground.service.DistService;
import com.fvbackground.service.MenuService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

   @Resource
   private DistMapper distMapper;


//    ------
//    查找全部
//    ------
    @GetMapping
    public List<Menu> find(@RequestParam(defaultValue = "") String name) {
        return menuService.find(name);
    }

//    ------
//    分页查询
//    ------
    @GetMapping("page")
    public IPage<Menu> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "1") String type) {
        IPage<Menu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        switch (type)
        {
            case "1":{
                queryWrapper.like("name",search);
                break;
            }
        }
        return menuService.page(page,queryWrapper);
    }

//    -----
//    新增|修改菜单
//    -----
    @PostMapping
    public boolean save(@RequestBody Menu menu) {
        return menuService.saveMenu(menu);
    }

 //   ------
//    删除菜单
//    ------
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return menuService.deleteById(id);
    }

    @DeleteMapping
    public  boolean deleteByIds(@RequestParam Collection idlist) {
        return menuService.removeByIds(idlist);
    }

    //    ------
//    导出菜单
//    ------
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查出所有数据
        List<Menu> list = menuService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("description","描述");
        writer.addHeaderAlias("icon","图标");
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
        String fileName = URLEncoder.encode("菜单信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        out.close();
        // 关闭writer，释放内存
        writer.close();

    }

//    ------
//    获取icon信息
//    ------
    @GetMapping("/icons")
    public List<Dist> getIcons() {
        QueryWrapper<Dist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",Constants.DICT_TYPE_ICON);
        return  distMapper.selectList(queryWrapper);
    }
}
