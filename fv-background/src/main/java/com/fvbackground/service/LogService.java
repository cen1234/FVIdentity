package com.fvbackground.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.entity.Log;
import com.fvbackground.mapper.LogMapper;
import org.springframework.stereotype.Service;

@Service
public class LogService extends ServiceImpl<LogMapper, Log> {
}
