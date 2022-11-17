package com.fvbackground.controller;


import com.fvbackground.entity.Single;
import com.fvbackground.service.SingleService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/single")
public class SingleIdentifyController {

    @Resource
    private SingleService singleService;


//    ------
//    单个识别
//    ------
    @GetMapping("/identity")
    public String SingleIdentity(@RequestParam String accessToken,@RequestParam String file) {
          return singleService.SingleIdentity(accessToken,file);
    }


//    --------
//    将识别结果存储到数据库中
//    --------
    @PostMapping
    public boolean save(@RequestBody Single single) {
        return singleService.Save(single);
    }


//    ---------
//    获取从百度百科爬到的内容
//    ---------
    @GetMapping("/introduction")
    public String getIntroduction(@RequestParam String search,
                                  @RequestParam String logId,
                                  @RequestParam String username) throws UnsupportedEncodingException {
       return singleService.getIntroduction(search,logId,username);
    }

}
