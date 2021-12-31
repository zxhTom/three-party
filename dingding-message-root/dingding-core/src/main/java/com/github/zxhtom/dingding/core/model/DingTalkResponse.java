/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.zxhtom.dingding.core.model;

import lombok.Data;

/***
 * 发送消息响应体
 */
@Data
public class DingTalkResponse {

    private boolean success;
    private Long taskId;
    private String errorCode;
    private String errMsg;

}
