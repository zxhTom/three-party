package com.github.zxhtom.wechat.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.model.material.Meterial;
import com.github.zxhtom.message.api.model.material.MeterialResponse;
import com.github.zxhtom.message.api.utils.BeanUtils;
import com.github.zxhtom.message.api.utils.HttpUtils;
import com.github.zxhtom.wechat.core.service.TokenService;
import com.github.zxhtom.message.api.service.UploadService;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
@Data
public class WechatUploadServiceImpl implements UploadService {
    private TokenService tokenService;
    @Override
    public MeterialResponse uploadPic(Meterial meterial, String fileName , InputStream inputStream) {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s", this.tokenService.accessAndGetDingDingToken(), meterial.getType());
        try {
            final String result = HttpUtils.upload(inputStream, fileName, url);
            MeterialResponse response = new MeterialResponse();
            return BeanUtils.transBeanWithTranAnnotation(JSONObject.parseObject(result),response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MeterialResponse();
    }
}
