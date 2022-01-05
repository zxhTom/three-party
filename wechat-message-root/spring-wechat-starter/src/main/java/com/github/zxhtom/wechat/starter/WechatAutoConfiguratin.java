package com.github.zxhtom.wechat.starter;

import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.wechat.core.config.Token;
import com.github.zxhtom.wechat.core.service.TokenService;
import com.github.zxhtom.wechat.core.service.impl.TokenServiceImpl;
import com.github.zxhtom.wechat.core.service.impl.UserWechatInfoServiceImpl;
import com.github.zxhtom.wechat.core.service.impl.WechatMessageServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
public class WechatAutoConfiguratin {
    @ConfigurationProperties(prefix = "message.wechat")
    @Bean("wechatToken")
    public Token token() {
        return new Token();
    }

    @Bean("wechatTokenService")
    public TokenService tokenService(Token token) {
        final TokenServiceImpl tokenService = new TokenServiceImpl();
        tokenService.setToken(token);
        return tokenService;
    }

    @Bean("wechatMessageService")
    public MessageService wechatMessageService(TokenService tokenService) {
        WechatMessageServiceImpl wechatMessageService = new WechatMessageServiceImpl();
        wechatMessageService.setTokenService(tokenService);
        return wechatMessageService;
    }

    @Bean("wechatUserInfoService")
    public UserInfoService wechatUserInfoService(TokenService tokenService) {
        UserWechatInfoServiceImpl userWechatInfoService = new UserWechatInfoServiceImpl();
        userWechatInfoService.setTokenService(tokenService);
        return userWechatInfoService;
    }
}
