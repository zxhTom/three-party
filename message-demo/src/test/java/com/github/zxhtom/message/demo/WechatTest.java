package com.github.zxhtom.message.demo;

import com.alibaba.fastjson.JSON;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.model.material.MeterialResponse;
import com.github.zxhtom.message.api.model.material.impl.ImageMeterial;
import com.github.zxhtom.message.api.model.message.impl.ImageMessage;
import com.github.zxhtom.message.api.model.message.impl.TextMessage;
import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UserInfoService;
import com.github.zxhtom.wechat.core.model.MsgData;
import com.github.zxhtom.wechat.core.service.TokenService;
import com.github.zxhtom.message.api.service.UploadService;
import com.github.zxhtom.wechat.core.service.impl.WechatMessageServiceImpl;
import com.github.zxhtom.wechat.core.utils.TemplateOnlineUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/4
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
//必须加上webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT  否则websocket注册失败
@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WechatTest {


    @Autowired
    TokenService tokenService;
    @Autowired
    @Qualifier("wechatMessageService")
    MessageService messageService;

    @Autowired
    @Qualifier("wechatUserInfoService")
    UserInfoService userInfoService;

    @Autowired
    @Qualifier("wechatUploadService")
    UploadService uploadService;
    @Test
    public void sendPic() {
        WechatMessageServiceImpl wechatMessageService = (WechatMessageServiceImpl) messageService;
        String openId = "ohlIG6FYIITO0_j3avuhPlWnEAnY";
        ImageMessage message = new ImageMessage("cVXflRYfSsxidRUwrY6eJM-yBjMPZS2D5qf2owWoAnAcYaEjdTgCD54oYezenYiz");
        wechatMessageService.sendCommonMsgBaseOpenId(openId, message);
    }
    @Test
    public void upload() throws FileNotFoundException {
        String fileName = "/template.jpeg";
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        final MeterialResponse response = uploadService.uploadPic(new ImageMeterial(), fileName, resourceAsStream);
        System.out.println(response);
    }
    @Test
    public void getToken() {
        final String s = tokenService.accessAndGetDingDingToken();
        System.out.println(s);
    }

    @Test
    public void getUserList() {
        final List<AbstrctUser> userList = userInfoService.selectUserListBaseOnDeptId(null);
        System.out.println(userList);
    }

    @Test
    public void sendMsgOpenId() {
        String openId = "ohlIG6FYIITO0_j3avuhPlWnEAnY";
        List<String> userIds = Arrays.asList(new String[]{openId});
        List<MsgData> dataList = new ArrayList<>();
        MsgData first = new MsgData();
        first.setName("first");
        first.setValue("新的消费通知！");
        first.setColor("#173177");
        MsgData keyword1 = new MsgData();
        keyword1.setName("keyword1");
        keyword1.setValue("test");
        keyword1.setColor("#fff");
        MsgData keyword2 = new MsgData();
        keyword2.setName("keyword2");
        keyword2.setValue("-");
        keyword2.setColor("#fff");
        MsgData keyword3 = new MsgData();
        keyword3.setName("keyword3");
        keyword3.setValue("1.0元");
        keyword3.setColor("#fff");
        MsgData keyword4 = new MsgData();
        keyword4.setName("keyword4");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        keyword4.setValue(simpleDateFormat.format(date));
        keyword4.setColor("#fff");
        MsgData remark = new MsgData();
        remark.setName("remark");
        remark.setValue("尊敬的用户,您于" + simpleDateFormat.format(date) + "在test消费了");
        dataList.add(first);
        dataList.add(keyword1);
        dataList.add(keyword2);
        dataList.add(keyword3);
        dataList.add(keyword4);
        dataList.add(remark);
        TemplateOnlineUtils.setTemplateId("zr42C8YoW06PIHz_PkXP714ev8dqMFVCR16LTRxOGg8");
        TextMessage message = new TextMessage(JSON.toJSONString(dataList));

        messageService.sendToDeptInUser(userIds, null, false, message);
    }

    @Test
    public void selectUserByCode() {
        String code = "091JVyll2J3Op84lb7ml2H12ZB1JVylZ";
        final AbstrctUser abstrctUser = userInfoService.selectUserBaseOnCode(code);
        System.out.println(abstrctUser);
    }
}
