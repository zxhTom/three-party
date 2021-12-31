package com.github.zxhtom.dingding.core.service;

import com.github.zxhtom.dingding.core.config.Token;

public interface TokenService {
    public String accessAndGetDingDingToken();

    public Token getToken();
}
