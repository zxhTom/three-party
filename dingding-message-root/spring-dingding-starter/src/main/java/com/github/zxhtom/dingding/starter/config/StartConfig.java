package com.github.zxhtom.dingding.starter.config;

import com.github.zxhtom.dingding.core.config.Token;
import com.github.zxhtom.dingding.core.service.DeptService;
import com.github.zxhtom.dingding.core.service.RobotMessageService;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.dingding.core.service.impl.*;
import com.github.zxhtom.dingding.starter.annotation.DingDing;
import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.message.api.service.impl.UserInfoServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
@Configuration
public class StartConfig {
    @ConfigurationProperties(prefix = "message.dingding")
    @Bean
    public Token token() {
        return new Token();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService(Token token) {
        final TokenServiceImpl tokenService = new TokenServiceImpl();
        tokenService.setToken(token);
        return tokenService;
    }

    @Bean
    @ConditionalOnMissingBean
    public RobotMessageService robotMessageService() {
        RobotMessageService robotMessageService = new RobotMessageImpl();
        return robotMessageService;
    }

    @Bean
    @ConditionalOnMissingBean
    public DeptService deptService(TokenService tokenService) {
        DeptServiceImpl deptService = new DeptServiceImpl();
        deptService.setTokenService(tokenService);
        return deptService;
    }

    @Bean("dingdingUserInfoService")
    public UserInfoService userInfoService(TokenService tokenService) {
        UserInfoDingDingServiceImpl userInfoService = new UserInfoDingDingServiceImpl();
        userInfoService.setTokenService(tokenService);
        return userInfoService;
    }

    @Bean("dingdingMessageService")
    public MessageService messageService(TokenService tokenService) {
        MessageDingDingServiceImpl messageDingDingService = new MessageDingDingServiceImpl();
        messageDingDingService.setTokenService(tokenService);
        return messageDingDingService;
    }
}
