package com.fvbackground.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fvbackground.entity.Picture;
import com.fvbackground.mapper.PictureMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file")
public class FileController {

    private String fileUploadPath = "C:/Users/cxy/Desktop/创新综合实践/FV/files/";

    @Resource
    private PictureMapper pictureMapper;

    //文件上传
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file,@RequestParam String username) throws IOException {
        //获取文件原始名称，文件类型，文件大小
       String originalFilename  = file.getOriginalFilename();
       String type = FileUtil.extName(originalFilename);
       long size = file.getSize();
       //把文件存储到磁盘
       File uploadFile = new File(fileUploadPath);
       //如果目录不存在,就创建
       if(!uploadFile.exists()) {
           uploadFile.mkdirs();
       }
       //定义一个文件唯一的标识码
       String uuid = IdUtil.fastSimpleUUID();
       String fileUuid = uuid+ StrUtil.DOT+type;
       File uploadFilePath =  new File(fileUploadPath + fileUuid);
       String  url = "http://localhost:9000/file/"+fileUuid;

       //获取文件的md5,如果数据库中存在这个md5,就不将这个图片存入磁盘了，避免磁盘存入相同的图片，解放了磁盘空间
//       String md5 = SecureUtil.md5(uploadFilePath);
//        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("md5",md5);
//        Picture picture = pictureMapper.selectOne(queryWrapper);
//        //如果md5存在，就直接返回数据库中存的url
//        if (picture != null) {
//           url = picture.getUrl();
//           return url;
//        } else {
//            url = "http://localhost:9000/file/"+fileUuid;
//        }
        //把获取到的文件存储到磁盘目录
        file.transferTo(uploadFilePath);
        //存储到数据库
        Picture savePicture = new Picture();
        savePicture.setUsername(username);
        savePicture.setName(originalFilename);
        savePicture.setType(type);
        savePicture.setSize(size/1024);
        savePicture.setUrl(url);
        pictureMapper.insert(savePicture);
        return fileUuid;
    }

    //下载文件
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        //根据文件唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        //设置输出流格式
       ServletOutputStream os = response.getOutputStream();
       response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUuid,"UTF-8"));
       response.setContentType("application/octet-stream");
       //读取文件字节流
       os.write(FileUtil.readBytes(uploadFile));
       os.flush();
       os.close();
    }
}
