package com.fvbackground.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fvbackground.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from role where name = #{rolename}")
    Integer selectByFlag(@Param("rolename") String rolename);
}
