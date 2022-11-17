package com.fvbackground.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.common.Constants;
import com.fvbackground.dto.UserDto;
import com.fvbackground.entity.Menu;
import com.fvbackground.entity.User;
import com.fvbackground.exception.ServiceException;
import com.fvbackground.mapper.RoleMapper;
import com.fvbackground.mapper.RoleMenuMapper;
import com.fvbackground.mapper.UserMapper;
import com.fvbackground.utils.TokenUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    public boolean saveUser(User user) {
        if(user.getId()==null) {
             return save(user);//mybatis-plus提供的方法，表示插入数据
        } else {
            return updateById(user);//根据id进行修改
        }
    }

    public boolean deleteById(Integer id) {
        return removeById(id);//根据id进行删除
    }

    public UserDto login(UserDto userDto) {
        User  oneData = getUserInfo(userDto);
        if (oneData !=null) {
            BeanUtil.copyProperties(oneData,userDto,true);
            //设置Token
            String token = TokenUtils.genToken(oneData.getId().toString(),oneData.getPassword());
            userDto.setToken(token);

            //设置access_token
            userDto.setAccessToken(getAuth());

            //获取用户角色对应的菜单功能
            String rolename = oneData.getRole();
            userDto.setMenus(getRoleMenus(rolename));
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误或用户已存在!");
        }
    }

    public User register(UserDto userDto) {
        User  oneData = getUserInfo(userDto);
        //如果当前数据库中找不到与注册的信息一样的用户，那么给数据库中插入注册的用户信息
        if (oneData == null) {
            oneData = new User();
            BeanUtil.copyProperties(userDto,oneData,true);
            save(oneData);
        } else {
            throw new ServiceException(Constants.CODE_600,"用户已经存在！");
        }
        return oneData;
    }

    public User  getUserInfo(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        User oneData;
        try {
            oneData =getOne(queryWrapper);//从数据库根据queryWrapper查询条件查询数据
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误!");
        }
        return oneData;
    }

    //获取当前角色的菜单
    private List<Menu> getRoleMenus(String rolename) {
        Integer roleId = roleMapper.selectByFlag(rolename);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出所有菜单
        List<Menu> menus = menuService.find("");
        //筛选出用户对应的菜单
        List<Menu> userMenus =new ArrayList<>();
        for (Menu menu:menus) {
            if (menuIds.contains(menu.getId())) {
                userMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child ->!menuIds.contains(child.getId()));
        }
        return userMenus;
    }

    //获取百度ai识别所需的access_token
    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "P3yFfN9HwNL8OGFeEM2qZqqc";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "NPaR1WmKte3nf7hmN2s5RVc5yVR4l5ZE";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Secret Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }




}
