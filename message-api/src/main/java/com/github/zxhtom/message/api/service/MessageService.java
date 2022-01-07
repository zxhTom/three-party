package com.github.zxhtom.message.api.service;

import com.github.zxhtom.message.api.model.message.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {
    public Integer sendToAll(Message msg);


    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, Message msg);

}
