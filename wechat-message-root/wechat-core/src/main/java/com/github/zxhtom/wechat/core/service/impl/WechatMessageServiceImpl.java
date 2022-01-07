package com.github.zxhtom.wechat.core.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.model.message.Message;
import com.github.zxhtom.message.api.model.message.impl.TextMessage;
import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.wechat.core.model.MsgData;
import com.github.zxhtom.wechat.core.service.TokenService;
import com.github.zxhtom.wechat.core.utils.TemplateOnlineUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
public class WechatMessageServiceImpl implements MessageService {
    private TokenService tokenService;

    private UserInfoService userInfoService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Integer sendToAll(Message msg) {
        TextMessage textMessage = (TextMessage) msg;
        final List<AbstrctUser> userList = userInfoService.selectUserListBaseOnDeptId(null);
        String templateId = TemplateOnlineUtils.getTemplateId();
        List<MsgData> dataList = JSONArray.parseArray(textMessage.getContent(), MsgData.class);
        if (CollectionUtils.isNotEmpty(userList)) {
            for (AbstrctUser abstrctUser : userList) {
                final String openId = abstrctUser.getUserId();
                sendMsgBaseOpenId(openId, templateId, dataList);
            }
        }
        return 1;
    }

    @Override
    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, Message msg) {
        if (allUser) {
            sendToAll(msg);
        }
        String templateId = TemplateOnlineUtils.getTemplateId();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<MsgData> dataList = JSONArray.parseArray(((TextMessage) msg).getContent(), MsgData.class);
            for (String userId : userIds) {
                sendMsgBaseOpenId(userId, templateId, dataList);
            }
        }
        return null;
    }

    public Integer sendCommonMsgBaseOpenId(String openId, Message message) {
        String sendUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", this.tokenService.accessAndGetDingDingToken());
        JSONObject jsonObject = JSONObject.parseObject(message.toString());
        jsonObject.put("touser", openId);
        HttpUtil.post(sendUrl, jsonObject.toJSONString());
        return 1;
    }
    private Integer sendMsgBaseOpenId(String openId, String templateId, List<MsgData> dataList) {
        if (StringUtils.isEmpty(templateId)) {
            throw new RuntimeException("未检测到消息模版，无法发送message");
        }
        Map<String, Object> map = new HashMap();
        map.put("touser", openId);//你要发送给某个用户的openid 前提是已关注该公众号,该openid是对应该公众号的，不是普通的openid
        map.put("template_id", templateId);
        Map<String,Object> dataMap = new HashMap();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (MsgData msgData : dataList) {
                dataMap.put(msgData.getName(), msgData);
            }
        }
        map.put("data", dataMap);
        String msgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + this.tokenService.accessAndGetDingDingToken();
        HttpUtil.post(msgUrl, JSON.toJSONString(map));
        return 1;
    }
}
