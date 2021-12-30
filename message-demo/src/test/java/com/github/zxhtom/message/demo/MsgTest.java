package com.github.zxhtom.message.demo;

import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.github.zxhtom.dingding.core.model.RobotMessage;
import com.github.zxhtom.dingding.core.model.RobotText;
import com.github.zxhtom.dingding.core.service.DeptService;
import com.github.zxhtom.dingding.core.service.RobotMessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    @Test
    public void getDeptListTest() {
        final List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptBaseResponses = deptService.selectDeptList(null);
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseRespons : deptBaseResponses) {
            System.out.println(deptBaseRespons);
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
}
