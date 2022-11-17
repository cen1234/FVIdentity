package com.fvbackground.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.common.Ingredient;
import com.fvbackground.entity.Log;
import com.fvbackground.entity.Multiple;
import com.fvbackground.mapper.MultipleMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class MultipleService  extends ServiceImpl<MultipleMapper, Multiple> {

    @Resource
    private LogService logService;

    //获取多个果蔬识别结果
    public String multipleIdentity(String accessToken, String file) {
        Ingredient ingredient = new Ingredient();
        return ingredient.ingredient(accessToken,file);
    }

    //在数据库中存储结果
    public boolean Save(Multiple multiple) {
        //把日志写入log表中
        Log log = new Log();
        log.setPath("/multiple");
        log.setUsername(multiple.getUsername());
        log.setRecordId(multiple.getLogId());
        log.setPhoto(multiple.getPhoto());
        log.setResuleNum(multiple.getResuleNum());
        boolean isLogSave = logService.save(log);

        //把result数据批量存入multiple表
        List<Multiple> result = multiple.getResult();
        boolean isMultipleSave = saveBatch(result,20);
        return isLogSave && isMultipleSave;
    }



}
