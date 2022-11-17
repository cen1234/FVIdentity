package com.fvbackground.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.entity.Menu;
import com.fvbackground.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService  extends ServiceImpl<MenuMapper, Menu> {

    public boolean saveMenu(Menu menu) {
        if(menu.getId()==null) {
            return save(menu);//mybatis-plus提供的方法，表示插入数据
        } else {
            return updateById(menu);//根据id进行修改
        }
    }

    public boolean deleteById(Integer id) {
        return removeById(id);
    }

    public List<Menu> find(String name) {
        //查询出所有数据
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name",name);
        }
        List<Menu> list = list(queryWrapper);
        //找出pid为null的一级菜单
        List<Menu> parentNode= list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Menu menu:parentNode) {
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }

        return parentNode;
    }
}
