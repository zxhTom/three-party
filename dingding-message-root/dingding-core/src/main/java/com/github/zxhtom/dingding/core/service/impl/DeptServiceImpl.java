package com.github.zxhtom.dingding.core.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.github.zxhtom.dingding.core.model.Dept;
import com.github.zxhtom.dingding.core.service.DeptService;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.utils.DataUtils;
import com.taobao.api.ApiException;

import java.util.List;

public class DeptServiceImpl implements DeptService {
    private TokenService tokenService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> selectDeptList(Long deptId) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = null;
        try {
            rsp = client.execute(req, this.tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        if (rsp.getErrcode() != 0) {
            throw new RuntimeException(rsp.getErrmsg());
        }
        return rsp.getResult();
    }
}
