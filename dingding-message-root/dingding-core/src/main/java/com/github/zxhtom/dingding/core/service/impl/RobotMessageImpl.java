package com.github.zxhtom.dingding.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotResponse;
import com.github.zxhtom.dingding.core.service.RobotMessageService;
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

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
@Slf4j
public class RobotMessageImpl implements RobotMessageService {


    @Override
    public RobotResponse robotSendMsgToChat(RobotMessage robotMessage){
        String accessTokenurl = "https://oapi.dingtalk.com/robot/send?access_token=4a3ecbc1529cde41fa82d4c128887adf240bed10727242c92052a6cd846b785c";
        String secret = "SEC2ced70e6340b061fd25254e8dcf09b4d198d8deeb1ad747d8c152176cc7a1664";
        RobotResponse robotResponse=new RobotResponse();
        StringBuffer rebotUrl=new StringBuffer(accessTokenurl);
        try {
            String timestamp=String.valueOf(System.currentTimeMillis());
            //签名
            String strToSign=timestamp+"\n"+secret;
            Mac mac=Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(strToSign.getBytes("UTF-8"));
            String sign= URLEncoder.encode(new String(Base64.getEncoder().encode(signData)),"UTF-8");
            log.info("sign:{},timestamp:{}",sign,timestamp);
            rebotUrl.append("&timestamp=").append(timestamp).append("&sign=").append(sign);
            CloseableHttpClient client= HttpClients.createDefault();
            HttpPost post=new HttpPost(rebotUrl.toString());
            post.addHeader("Content-Type","application/json");
            post.setEntity(new StringEntity(JSON.toJSONString(robotMessage),"UTF-8"));
            CloseableHttpResponse response=client.execute(post);
            if (response!=null&&response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                log.info("发送请求成功");
                String resp= EntityUtils.toString(response.getEntity(),"UTF-8");
                log.info(resp);
                JSONObject jsonObject=JSONObject.parseObject(resp);
                int code=jsonObject.getInteger("errcode");
                String msg=jsonObject.getString("errmsg");
                robotResponse.setErrMsg(msg);
                robotResponse.setErrCode(code);
                robotResponse.setSuccess(code== 0);
                if (code== -1){
                    //业务繁忙,重试
                    log.error("服务器繁忙,推送工作消息失败,重试1次");
                }
            }
        } catch (Exception e) {
            log.error("推送机器人消息失败,错误信息:{}",e.getMessage());
            log.info(e.getMessage(),e);
        }
        return robotResponse;

    }
}
