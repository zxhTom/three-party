package com.github.zxhtom.wechat.core.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.wechat.core.model.WechatUser;
import com.github.zxhtom.wechat.core.service.TokenService;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
public class UserWechatInfoServiceImpl implements UserInfoService {
    private TokenService tokenService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public List<AbstrctUser> selectUserListBaseOnDeptId(Long deptId) {
        return selectAllowUserList();
    }

    @Override
    public List<AbstrctUser> selectFullUserInfo(List<String> userId) {
        return null;
    }

    @Override
    public List<AbstrctUser> selectUserBaseOnPhone(String iphone) {
        return null;
    }
    private List<AbstrctUser> selectAllowUserList() {
        //获取用户列表
        String baseUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+this.tokenService.accessAndGetDingDingToken();
        final HttpResponse execute = HttpUtil.createGet(baseUrl).execute();
        final String body = execute.body();
        final JSONObject jsonObject = (JSONObject) JSONObject.parse(body);
        final JSONArray openIdArray = jsonObject.getJSONObject("data").getJSONArray("openid");
        List<AbstrctUser> userList = new ArrayList<>();
        for (Object userOpenId : openIdArray) {
            if (null == userOpenId) {
                continue;
            }
            AbstrctUser user = new WechatUser();
            user.setUserId(userOpenId.toString());
            user.setVisualUserId(userOpenId.toString());
            userList.add(user);
        }
        return userList;
    }
}
