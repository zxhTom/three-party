package com.github.zxhtom.message.api.model.message.impl;

import com.alibaba.fastjson.JSON;
import com.github.zxhtom.message.api.model.message.Message;
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
public class TextMessage extends Message {
    private String content;
    public TextMessage(String content) {
        super(MESSAGE_TEXT);
        this.content=content;
    }

    @Override
    public String toString() {
        Map<String, Object> map = initMap();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("content", this.content);
        map.put(getType(), childMap);
        return JSON.toJSONString(map);
    }
}
