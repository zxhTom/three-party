package com.github.zxhtom.message.api.service.impl;

import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.UserInfoService;

import java.util.List;

/**
 * 用户模块
 */
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public List<AbstrctUser> selectUserListBaseOnDeptId(Long deptId) {
        return null;
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
        return null;
    }
}
