package com.github.zxhtom.message.api.service.impl;

import com.github.zxhtom.message.api.model.message.Message;
import com.github.zxhtom.message.api.service.MessageService;

import java.util.List;
import java.util.Map;

/**
 * 消息模块
 */
public class MessageServiceImpl implements MessageService {
    @Override
    public Integer sendToAll(Message msg) {
        return null;
    }

    @Override
    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, Message msg) {
        return null;
    }


}
