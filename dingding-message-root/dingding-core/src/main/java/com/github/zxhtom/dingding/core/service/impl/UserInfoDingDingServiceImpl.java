package com.github.zxhtom.dingding.core.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserListsimpleRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiUserListsimpleResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.github.zxhtom.dingding.core.model.DingDingUser;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class UserInfoDingDingServiceImpl implements UserInfoService {

    private TokenService tokenService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public List<AbstrctUser> selectUserListBaseOnDeptId(Long deptId) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
        req.setDeptId(deptId);
        req.setCursor(0L);
        req.setSize(10L);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiUserListsimpleResponse rsp = null;
        try {
            rsp = client.execute(req, tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        List<AbstrctUser> resultList = new ArrayList<>();
        if (rsp.getErrcode() == 0) {
            final List<OapiUserListsimpleResponse.ListUserSimpleResponse> list = rsp.getResult().getList();
            for (OapiUserListsimpleResponse.ListUserSimpleResponse listUserSimpleResponse : list) {
                AbstrctUser user = new DingDingUser();
                user.setUserId(listUserSimpleResponse.getUserid());
                user.setUserName(listUserSimpleResponse.getName());
                resultList.add(user);
            }
        }

        return resultList;
    }

    @Override
    public List<AbstrctUser> selectFullUserInfo(List<String> userIds) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        List<AbstrctUser> userList = new ArrayList<>();
        for (String userId : userIds) {
            req.setUserid(userId);
            req.setLanguage("zh_CN");
            OapiV2UserGetResponse rsp = null;
            try {
                rsp = client.execute(req, this.tokenService.accessAndGetDingDingToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
            if (rsp.getErrcode() != 0) {
                log.error(JSON.toJSONString(rsp));
                throw new RuntimeException("????????????");
            }
            final OapiV2UserGetResponse.UserGetResponse result = rsp.getResult();
            DingDingUser user = new DingDingUser();
            user.setUserGetResponse(result);
            user.setUserId(result.getUserid());
            user.setUserName(result.getName());
            user.setVisualUserId(result.getUnionid());
            user.setAvatar(result.getAvatar());
            user.setMobile(result.getMobile());
            userList.add(user);
        }
        return userList;
    }

    @Override
    public List<AbstrctUser> selectUserBaseOnPhone(String iphone) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
        OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
        req.setMobile(iphone);
        OapiV2UserGetbymobileResponse rsp = null;
        try {
            rsp = client.execute(req, this.tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        if (rsp.getErrcode()!=0) {
            log.error(JSON.toJSONString(rsp));
            throw new RuntimeException("??????????????????");
        }
        final OapiV2UserGetbymobileResponse.UserGetByMobileResponse result = rsp.getResult();
        final List<AbstrctUser> userList = selectFullUserInfo(Arrays.asList(result.getUserid()));
        return userList;
    }

    @Override
    public AbstrctUser selectUserBaseOnCode(String code) {
        String urlFormat = String.format("https://oapi.dingtalk.com/user/getuserinfo?access_token=%s&code=%s", this.tokenService.accessAndGetDingDingToken(), code);
        String s = HttpUtil.get(urlFormat);
        JSONObject json = JSONObject.parseObject(s);
        if ("40078".equals(json.getString("errcode"))) {
            throw new RuntimeException("code??????");
        }
        DingDingUser dingDingUser = JSONObject.parseObject(s, DingDingUser.class);
        dingDingUser.setUserName(json.getString("name"));
        List<AbstrctUser> userList = selectFullUserInfo(Arrays.asList(dingDingUser.getUserId()));
        if (CollectionUtils.isNotEmpty(userList)) {
            return userList.get(0);
        }
        return dingDingUser;
    }
}
