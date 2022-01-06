package com.github.zxhtom.wechat.core.model;

import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
@Data
public class OauthToken {
       private String accessToken;
       private Long expiresIn;
       private String refreshToken;
       private String openId;
       private String scope;
}
