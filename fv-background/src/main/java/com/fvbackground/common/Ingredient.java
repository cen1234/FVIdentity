package com.fvbackground.common;

import com.fvbackground.utils.Base64Util;
import com.fvbackground.utils.FileUtil;
import com.fvbackground.utils.HttpUtil;
import java.net.URLEncoder;

//果蔬识别
public class Ingredient {

    private String fileUploadPath = "C:/Users/cxy/Desktop/创新综合实践/FV/files/";

    public String ingredient(String myaccessToken,String file) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient";
        try {
            // 本地文件路径
            String filePath = fileUploadPath + file;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            String accessToken = myaccessToken;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
