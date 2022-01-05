package com.github.zxhtom.wechat.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.utils.DateUtils;
import com.github.zxhtom.wechat.core.config.Token;
import com.github.zxhtom.wechat.core.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
@Slf4j
public class TokenServiceImpl implements TokenService {
    private Token token;

    private String tokenStr;

    private Date startTime;

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String accessAndGetDingDingToken() {
        log.info("start get wechat talk token ........");
        if (StringUtils.isNotEmpty(tokenStr)) {
            //判断是否过期
            if (DateUtils.getInstance().chargeDate(startTime,new Date())>0) {
                //还在有效期
                return this.tokenStr;
            }
        }
        String appId = token.getAppId();
        String appSecret = token.getAppSecret();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String accessToken = null;
        JSONObject jsonObj = null;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            accessToken = new String(jsonBytes, "UTF-8");
            jsonObj = JSONObject.parseObject(accessToken);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tokenStr = jsonObj.getString("access_token");
        final Long expiresIn = jsonObj.getLong("expires_in");
        startTime = DateUtils.getInstance().judgeTime(new Date(), expiresIn.intValue());
        return tokenStr;
    }

    @Override
    public Token getToken() {
        return token;
    }
}
