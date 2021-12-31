package com.github.zxhtom.dingding.core.service.impl;

import com.alibaba.fastjson.JSON;
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
                throw new RuntimeException("请求失败");
            }
            final OapiV2UserGetResponse.UserGetResponse result = rsp.getResult();
            DingDingUser user = new DingDingUser();
            user.setUserGetResponse(result);
            user.setUserId(result.getUserid());
            user.setUserName(result.getName());
            user.setVisualUserId(result.getUnionid());
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
            throw new RuntimeException("用户获取异常");
        }
        final OapiV2UserGetbymobileResponse.UserGetByMobileResponse result = rsp.getResult();
        final List<AbstrctUser> userList = selectFullUserInfo(Arrays.asList(result.getUserid()));
        return userList;
    }
}
