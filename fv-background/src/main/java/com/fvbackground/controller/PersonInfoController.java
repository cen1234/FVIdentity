package com.fvbackground.controller;

import com.fvbackground.entity.User;
import com.fvbackground.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

//    -----
//    获取当前用户信息
//    -----
    @GetMapping
    public User getPersonInfo(@RequestParam String username) {
        //获取当前用户信息
        return personInfoService.loadInfo(username);
    }

//    -----
//    修改当前用户信息
//    -----
    @PostMapping
    public boolean update(@RequestBody User user) {
        return personInfoService.saveUser(user);
    }
}
