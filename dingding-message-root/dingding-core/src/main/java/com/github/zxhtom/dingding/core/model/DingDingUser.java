package com.github.zxhtom.dingding.core.model;

import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.github.zxhtom.message.api.model.AbstrctUser;
import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/31
 */
@Data
public class DingDingUser extends AbstrctUser {
    private OapiV2UserGetResponse.UserGetResponse userGetResponse;
}
