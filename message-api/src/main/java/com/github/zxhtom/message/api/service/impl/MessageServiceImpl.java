package com.github.zxhtom.message.api.service.impl;

import com.github.zxhtom.message.api.service.MessageService;

import java.util.List;
import java.util.Map;

/**
 * 消息模块
 */
public class MessageServiceImpl implements MessageService {
    @Override
    public Integer sendToAll(String msg) {
        return null;
    }

    @Override
    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, String msg) {
        return null;
    }


}
