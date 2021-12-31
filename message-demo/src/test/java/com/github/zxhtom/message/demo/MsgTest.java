package com.github.zxhtom.message.demo;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotText;
import com.github.zxhtom.dingding.core.service.DeptService;
import com.github.zxhtom.dingding.core.service.RobotMessageService;
import com.github.zxhtom.message.api.model.AbstrctUser;
import com.github.zxhtom.message.api.service.MessageService;
import com.github.zxhtom.message.api.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    UserInfoService userInfoService;
    @Autowired
    MessageService messageService;
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
        Long deptId = 581377087L;
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
        List<String> userIds = Arrays.asList(new String[]{"manager2239"});
        List<String> deptIds = Arrays.asList(new String[]{"581377087"});
        messageService.sendToDeptInUser(userIds,deptIds,false,"你们好，元旦放假了！！！"+UUID.randomUUID());
    }
}
