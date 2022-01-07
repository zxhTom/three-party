package com.github.zxhtom.message.demo.controller;

import com.github.zxhtom.message.demo.model.AesException;
import com.github.zxhtom.message.demo.model.WXBizMsgCrypt;
import com.github.zxhtom.message.demo.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
@RestController
@RequestMapping(value = "/event")
@Slf4j
public class EventController {
    @Autowired
    EventService eventService;
    @RequestMapping(value = "/handler",method = RequestMethod.POST)
    public Object handlerReceivce(HttpServletRequest request) {
        return eventService.processRequest(request);
    }
    @RequestMapping(value = "/handler",method = RequestMethod.GET)
    public Object handler(@RequestParam(required = false) String signature,
                          @RequestParam(required = false) String timestamp,
                          @RequestParam(required = false) String nonce,
                          @RequestParam(required = false) String echostr) throws AesException, ParserConfigurationException, IOException, SAXException {
        log.info("signature:" + signature);
        log.info("timestamp:" + timestamp);
        log.info("nonce:" + nonce);
        log.info("echostr:" + echostr);

        return echostr;
    }
}
