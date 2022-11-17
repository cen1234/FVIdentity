package com.fvbackground.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.entity.Role;
import com.fvbackground.entity.RoleMenu;
import com.fvbackground.mapper.RoleMapper;
import com.fvbackground.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    public boolean saveUser(Role role) {
        if(role.getId()==null) {
            return save(role);//mybatis-plus提供的方法，表示插入数据
        } else {
            return updateById(role);//根据id进行修改
        }
    }

    public boolean deleteById(Integer id) {
        return removeById(id);
    }

    @Transactional
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除角色与菜单的对应关系
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        roleMenuMapper.delete(queryWrapper);
        //再根据前端传过来的关系建立新的对应关系
        for (Integer menuId:menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRole_id(roleId);
            roleMenu.setMenu_id(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }
}
