package com.github.zxhtom.message.api.model.message.impl;

import com.alibaba.fastjson.JSON;
import com.github.zxhtom.message.api.model.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
public class ImageMessage extends Message {
    private String mediaId;
    public ImageMessage(String mediaId) {
        super(MESSAGE_IMAGE);
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        Map<String, Object> map = initMap();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("media_id", this.mediaId);
        map.put(getType(), childMap);
        return JSON.toJSONString(map);
    }
}
