package com.github.zxhtom.dingding.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotResponse;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.service.MessageService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Base64;

@Slf4j
public class MessageDingDingServiceImpl implements MessageService {
    private TokenService tokenService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Integer sendToAll() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest req = new OapiChatSendRequest();
        req.setChatid("chate39f5xxxxxx335");
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
        text.setContent("请于本月底提交月度工作报告。");
        msg.setText(text);
        msg.setMsgtype("text");
        req.setMsg(msg);
        OapiChatSendResponse rsp = null;
        try {
            rsp = client.execute(req, tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return 1;
    }


}
