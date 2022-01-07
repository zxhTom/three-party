package com.github.zxhtom.message.demo;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.github.zxhtom.dingding.core.model.DingDingUser;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotText;
import com.github.zxhtom.dingding.core.service.DeptService;
import com.github.zxhtom.dingding.core.service.RobotMessageService;
import com.github.zxhtom.dingding.starter.annotation.DingDing;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.model.material.MeterialResponse;
import com.github.zxhtom.message.api.model.material.impl.ImageMeterial;
import com.github.zxhtom.message.api.model.message.Message;
import com.github.zxhtom.message.api.model.message.impl.ImageMessage;
import com.github.zxhtom.message.api.model.message.impl.TextMessage;
import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UploadService;
import com.github.zxhtom.message.api.service.UserInfoService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
//必须加上webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT  否则websocket注册失败
@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MsgTest {
    @Autowired
    DeptService deptService;
    @Autowired
    RobotMessageService robotMessageService;

    @Autowired
    @Qualifier("dingdingMessageService")
    MessageService messageService;
    @Autowired
    List<MessageService> messageServices;
    @Autowired
    @Qualifier("dingdingUserInfoService")
    UserInfoService userInfoService;

    @Autowired
    @Qualifier("dingdingUploadService")
    UploadService uploadService;

    @Test
    public void sendMultiTypeMessage() {
        String fileName = "/macpower.png";
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        final MeterialResponse response = uploadService.uploadPic(new ImageMeterial(), fileName, resourceAsStream);
        System.out.println(response);
        Message message = new ImageMessage(response.getMediaId());
        List<String> userIds = Arrays.asList(new String[]{"manager2239"});
        List<Long> deptIds = Arrays.asList(new Long[]{1l});
        messageService.sendToDeptInUser(userIds,deptIds,false,message);
    }
    @Test
    public void upload() throws FileNotFoundException {
        String fileName = "/macpower.png";
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        final MeterialResponse response = uploadService.uploadPic(new ImageMeterial(), fileName, resourceAsStream);
        System.out.println(response);
    }
    @Test
    public void currentUserTest() {
        String code = "9e6b328bb2223768bbbdbaa7b0db0726";
        AbstrctUser abstrctUser = userInfoService.selectUserBaseOnCode(code);
        System.out.println(abstrctUser);
    }
    @Test
    public void getDeptListTest() {
        final List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptBaseResponses = deptService.selectDeptList(null);
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseRespons : deptBaseResponses) {
            System.out.println(JSON.toJSONString(deptBaseRespons));
        }
    }
    @Test
    public void robotTest() {
        for (int i = 0; i < 10; i++) {

            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setMsgtype("text");
            RobotText robotText = new RobotText();
            robotText.setContent("我是第一个机器人"+ UUID.randomUUID());
            robotMessage.setText(robotText);
            robotMessageService.robotSendMsgToChat(robotMessage);
        }
    }


    @Test
    public void getUserInfo() {
        Long deptId = 1L;
        final List<AbstrctUser> userList = userInfoService.selectUserListBaseOnDeptId(deptId);
        for (AbstrctUser abstrctUser : userList) {
            System.out.println(JSON.toJSONString(abstrctUser));
        }
    }

    @Test
    public void getUsrDetailTest() {
        List<String> userIdList = new ArrayList<>();
        userIdList.add("manager2239");
        final List<AbstrctUser> userList = userInfoService.selectFullUserInfo(userIdList);
        for (AbstrctUser abstrctUser : userList) {
            System.out.println(JSON.toJSONString(abstrctUser));
        }
    }

    @Test
    public void getUsrDetailOnPhoneTest() {
        String phone = "17366236771";
        final List<AbstrctUser> userList = userInfoService.selectUserBaseOnPhone(phone);
        for (AbstrctUser abstrctUser : userList) {
            System.out.println(JSON.toJSONString(abstrctUser));
        }
    }

    @Test
    public void sendMsg() {
        for (int i = 0; i < 1; i++) {
            List<String> userIds = Arrays.asList(new String[]{"221116691019969,manager2239"});
            List<Long> deptIds = Arrays.asList(new Long[]{1l});
            messageService.sendToDeptInUser(userIds,deptIds,false,new TextMessage("你们好，元旦放假了！！！,我正在测试消息发送多人"+UUID.randomUUID()));
        }
    }

    @Test
    public void sendByPhone() {
        String phone = "17366236771";
        List<AbstrctUser> userList = userInfoService.selectUserBaseOnPhone(phone);
        for (AbstrctUser abstrctUser : userList) {
            DingDingUser dingDingUser = (DingDingUser) abstrctUser;
            final OapiV2UserGetResponse.UserGetResponse userGetResponse = dingDingUser.getUserGetResponse();
            final String userId = userGetResponse.getUserid();
            final List<Long> deptIdList = userGetResponse.getDeptIdList();
            List<String> userIdList = Arrays.asList(userId);
            messageService.sendToDeptInUser(userIdList, deptIdList, false, new TextMessage("你好，手机人" + phone));
        }
    }
}
