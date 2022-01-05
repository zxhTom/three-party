package com.github.zxhtom.wechat.core.service;

import com.github.zxhtom.wechat.core.config.Token;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
public interface TokenService {

    public String accessAndGetDingDingToken();

    public Token getToken();
}
