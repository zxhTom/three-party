package com.github.zxhtom.wechat.core.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.wechat.core.model.OauthToken;
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

    @Override
    public AbstrctUser selectUserBaseOnCode(String code) {
        OauthToken token = selectOTOnCode(code);
        String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", token.getAccessToken(), token.getOpenId());
        final String s = HttpUtil.get(url);
        final WechatUser user = JSONObject.parseObject(s, WechatUser.class);
        return user;
    }

    public AbstrctUser selectUserBaseOnRefreshToken(String refreshToken) {
        final String appId = this.tokenService.getToken().getAppId();
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s", appId, refreshToken);
        final OauthToken oauthToken = getOauthToken(url);
        String usrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", oauthToken.getAccessToken(), oauthToken.getOpenId());
        final String s = HttpUtil.get(usrl);
        final WechatUser user = JSONObject.parseObject(s, WechatUser.class);
        return user;
    }
    private OauthToken selectOTOnCode(String code) {
        String appId = this.tokenService.getToken().getAppId();
        String appSecret = this.tokenService.getToken().getAppSecret();
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appId,appSecret,code);
        OauthToken token = getOauthToken(url);
        return token;
    }

    private OauthToken getOauthToken(String url) {
        String s = HttpUtil.get(url);
        JSONObject json = JSONObject.parseObject(s);
        OauthToken token = new OauthToken();
        token.setAccessToken(json.getString("access_token"));
        token.setScope(json.getString("scope"));
        token.setRefreshToken(json.getString("refresh_token"));
        token.setOpenId(json.getString("openid"));
        token.setExpiresIn(json.getLong("expires_in"));
        return token;
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
