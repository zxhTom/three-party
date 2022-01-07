package com.github.zxhtom.dingding.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.github.zxhtom.dingding.core.model.DingTalkResponse;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotResponse;
import com.github.zxhtom.dingding.core.service.TokenService;
import com.github.zxhtom.message.api.model.message.Message;
import com.github.zxhtom.message.api.service.MessageService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

@Slf4j
public class MessageDingDingServiceImpl implements MessageService {
    //消息最大长度,不超过2048字节...
    protected static final Long MESSAGE_MAX_SIZE=2048L;

    private TokenService tokenService;

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Integer sendToAll(Message msg) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest req = new OapiChatSendRequest();
        req.setChatid("chate39f5xxxxxx335");
        req.setMsg(msg.toString());
        OapiChatSendResponse rsp = null;
        try {
            rsp = client.execute(req, tokenService.accessAndGetDingDingToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public Integer sendToDeptInUser(List<String> userIds, List<Long> depts, boolean allUser, Message message) {
        if (!allUser){
            if (CollectionUtil.isEmpty(userIds)&& CollectionUtil.isEmpty(depts)){
                throw new IllegalArgumentException("未推送给全部企业员工则需要指定企业员工或部门");
            }
            //发送给企业全部用户
            if (CollectionUtil.isNotEmpty(userIds)&&userIds.size()>100){
                throw new IllegalArgumentException("推送企业员工最多不能超过100个");
            }
            if (CollectionUtil.isNotEmpty(depts)&&depts.size()>20){
                throw new IllegalArgumentException("推送企业部门不能超过20个");
            }
        }
        String msg = message.toString();
        if (StringUtils.isEmpty(msg)){
            throw new IllegalArgumentException("消息体不能为空");
        }
        if (msg.getBytes().length>MESSAGE_MAX_SIZE){
            throw new IllegalArgumentException("消息体最大长度不能超过"+MESSAGE_MAX_SIZE.toString()+"个字节");
        }
        DingTalkResponse dingTalkResponse=new DingTalkResponse();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setToAllUser(allUser);
        request.setAgentId(Long.valueOf(this.tokenService.getToken().getAgentId()));
        if (!allUser){
            if (CollectionUtil.isNotEmpty(userIds)&&userIds.size()>0){
                request.setUseridList(CollectionUtil.join(userIds,","));
            }
            if (CollectionUtil.isNotEmpty(depts)&&depts.size()>0){
                request.setDeptIdList(CollectionUtil.join(depts,","));
            }
        }
        //设置发送消息
        request.setMsg(msg);
        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,this.tokenService.accessAndGetDingDingToken());
            dingTalkResponse.setErrMsg(response.getErrmsg());
            dingTalkResponse.setErrorCode(response.getErrorCode());
            dingTalkResponse.setTaskId(response.getTaskId());
            dingTalkResponse.setSuccess(response.getErrcode().longValue()==0);
            if (response.getErrcode().longValue()== -1){
                //业务繁忙,重试
                log.error("服务器繁忙,推送工作消息失败,重试1次");
            }
        } catch (ApiException e) {
            log.error("推送工作消息失败,错误信息:{}",e.getErrMsg());
            log.error(e.getMessage(),e);
        }
        return 1;
    }

    public boolean workMessageStatus(Long taskId){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult");
        OapiMessageCorpconversationGetsendresultRequest request = new OapiMessageCorpconversationGetsendresultRequest();
        request.setTaskId(taskId);
        request.setAgentId(Long.valueOf(this.tokenService.getToken().getAgentId()));
        OapiMessageCorpconversationGetsendresultResponse response = new OapiMessageCorpconversationGetsendresultResponse();
        try {
            response = client.execute(request, this.tokenService.accessAndGetDingDingToken());
            if(response.getErrcode() != 0){
                log.error("查询推送消息状态失败");
            }
        } catch (ApiException e) {
            log.error("查询推送消息状态失败,错误信息:{}",e.getErrMsg());
            log.error(e.getMessage(),e);
        }
        if (response.getSendResult()!=null&&CollectionUtil.isNotEmpty(response.getSendResult().getFailedUserIdList())){
            return false;
        }
        return true;
    }
}
