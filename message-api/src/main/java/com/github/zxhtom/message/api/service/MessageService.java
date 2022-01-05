package com.github.zxhtom.message.api.service;

import java.util.List;
import java.util.Map;

public interface MessageService {
    public Integer sendToAll(String msg);


    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, String msg);

}
