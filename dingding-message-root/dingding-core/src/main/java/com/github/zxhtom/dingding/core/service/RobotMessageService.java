package com.github.zxhtom.dingding.core.service;

import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotResponse;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
public interface RobotMessageService {
    public RobotResponse robotSendMsgToChat(RobotMessage robotMessage);
}
