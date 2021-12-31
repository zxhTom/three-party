package com.github.zxhtom.dingding.core.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.github.zxhtom.dingding.core.config.Token;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.utils.DateUtils;
import com.taobao.api.ApiException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TokenServiceImpl implements TokenService {
    private Token token;

    private String tokenStr;

    private Date startTime;

    private Lock lock=new ReentrantLock();

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public String accessAndGetDingDingToken() {
        log.info("start get dingding talk token ........");
        if (StringUtils.isNotEmpty(tokenStr)) {
            //判断是否过期
            if (DateUtils.getInstance().chargeDate(startTime,new Date())>0) {
                //还在有效期
                return this.tokenStr;
            }
        }
        this.startTime=null;
        this.tokenStr = StringUtils.EMPTY;
        //构建请求类
        DefaultDingTalkClient defaultDingTalkClient=new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request=new OapiGettokenRequest();
        //填充企业信息
        request.setAppkey(token.getAppKey());
        request.setAppsecret(token.getAppSecret());
        //请求方式
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response=defaultDingTalkClient.execute(request);
            this.tokenStr = response.getAccessToken();
            startTime = DateUtils.getInstance().judgeTime(new Date(), response.getExpiresIn().intValue());
        } catch (ApiException e) {
            log.error("dingding token run error");
            e.printStackTrace();
        }
        return tokenStr;
    }
}
