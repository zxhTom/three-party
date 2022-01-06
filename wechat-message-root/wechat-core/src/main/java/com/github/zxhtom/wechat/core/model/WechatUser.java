package com.github.zxhtom.wechat.core.model;

import com.github.zxhtom.message.api.model.AbstrctUser;
import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
@Data
public class WechatUser extends AbstrctUser {
    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;
}
