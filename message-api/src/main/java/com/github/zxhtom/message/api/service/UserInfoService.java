package com.github.zxhtom.message.api.service;

import com.github.zxhtom.message.api.model.AbstrctUser;

import java.util.List;

public interface UserInfoService {
    public List<AbstrctUser> selectUserListBaseOnDeptId(Long deptId);

    public List<AbstrctUser> selectFullUserInfo(List<String> userId);

    public List<AbstrctUser> selectUserBaseOnPhone(String iphone);

    public AbstrctUser selectUserBaseOnCode(String code);
}
