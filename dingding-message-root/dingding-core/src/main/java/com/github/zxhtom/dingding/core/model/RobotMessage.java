package com.github.zxhtom.dingding.core.model;

import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
@Data
public class RobotMessage {
    private String msgtype;
    private RobotText text;
}
