package com.github.zxhtom.message.api.service;

import com.github.zxhtom.message.api.model.AbstrctUser;

import java.util.List;

public interface UserInfoService {
    public List<AbstrctUser> selectUserListBaseOnDeptId(Long deptId);
}
