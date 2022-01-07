package com.github.zxhtom.dingding.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.model.material.Meterial;
import com.github.zxhtom.message.api.model.material.MeterialResponse;
import com.github.zxhtom.message.api.service.UploadService;
import com.github.zxhtom.message.api.utils.BeanUtils;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.internal.mapping.ApiField;
import lombok.Data;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/7
 */
@Data
public class DingDingUploadServiceImpl implements UploadService {
    private TokenService tokenService;
    @Override
    public MeterialResponse uploadPic(Meterial meterial, String fileName, InputStream inputStream) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/media/upload");
        OapiMediaUploadRequest req = new OapiMediaUploadRequest();
        req.setType(meterial.getType());
        // 要上传的媒体文件
        FileItem item = new FileItem(fileName,inputStream);
        req.setMedia(item);
        OapiMediaUploadResponse rsp=null;
        try {
            rsp = client.execute(req, tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        MeterialResponse response = new MeterialResponse();
        final Field[] declaredFields = rsp.getClass().getDeclaredFields();
        JSONObject jsonObject = new JSONObject();
        for (Field declaredField : declaredFields) {
            final ApiField annotation = declaredField.getAnnotation(ApiField.class);
            if (null == annotation) {
                continue;
            }
            final String value = annotation.value();
            declaredField.setAccessible(true);
            try {
                jsonObject.put(value, declaredField.get(rsp));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        BeanUtils.transBeanWithTranAnnotation(jsonObject, response);
        return response;
    }
}
