package com.github.zxhtom.dingding.core.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserListsimpleRequest;
import com.dingtalk.api.response.OapiUserListsimpleResponse;
import com.github.zxhtom.dingding.core.model.DingDingUser;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.taobao.api.ApiException;

import java.util.ArrayList;
import java.util.List;

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
}
