package com.github.zxhtom.message.demo.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
public interface EventService {
    Object processRequest(HttpServletRequest request);
    public String getToken();
    public Map<String, String> getUserInfo(String openid);
}
