package com.github.zxhtom.dingding.core.service;

import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.github.zxhtom.dingding.core.model.Dept;

import java.util.List;

public interface DeptService {

    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> selectDeptList(Long deptId);
}
