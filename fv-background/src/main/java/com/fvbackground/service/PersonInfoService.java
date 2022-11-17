package com.fvbackground.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.entity.User;
import com.fvbackground.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoService extends ServiceImpl<UserMapper, User> {

    //获取当前用户信息
    public User loadInfo(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);

        return getOne(queryWrapper);
    }

    //修改当前用户信息
    public boolean saveUser(User user) {
        return updateById(user);
    }

}
