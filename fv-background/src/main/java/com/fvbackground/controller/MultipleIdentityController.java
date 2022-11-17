package com.fvbackground.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.entity.Multiple;
import com.fvbackground.service.MultipleService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/multiple")
public class MultipleIdentityController {

    @Resource
    private MultipleService multipleService;


//    -----------
//    多个果蔬识别
//    -----------
    @GetMapping("/identity")
    public String multipleIdentity(@RequestParam String accessToken, @RequestParam String file) {
        return multipleService.multipleIdentity(accessToken,file);
    }

//    --------
//    将识别结果存储到数据库中
//    --------
    @PostMapping
    public boolean save(@RequestBody Multiple multiple) {
        return  multipleService.Save(multiple);
    }

    //获取数据库中的结果
    @GetMapping("page")
    public IPage<Multiple> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam String username,
                                    @RequestParam String logId) {
        IPage<Multiple> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Multiple> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("log_id",logId);
        return multipleService.page(page,queryWrapper);
    }




}
