package com.github.zxhtom.message.api.model.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
@Data
public class Message {
    //文本消息
    protected static final String MESSAGE_TEXT="text";
    //图片消息
    protected static final String MESSAGE_IMAGE="image";
    //语音消息
    protected static final String MESSAGE_VOICE="voice";
    //文件
    protected static final String MESSAGE_FILE="file";
    //链接消息
    protected static final String MESSAGE_LINK="link";
    //markdown消息
    protected static final String MESSAGE_MARKDOWN="markdown";
    //卡片消息
    protected static final String MESSAGE_CARD="action_card";

    private String type;
    public Message(String type) {
        this.type = type;
    }

    public Map<String, Object> initMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", this.type);
        return map;
    }
}
