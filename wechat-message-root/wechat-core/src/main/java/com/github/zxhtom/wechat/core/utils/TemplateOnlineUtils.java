package com.github.zxhtom.wechat.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/5
 */
public class TemplateOnlineUtils{
    private static ThreadLocal<String> template = new ThreadLocal<String>();
    public static String getTemplateId() {
        return template.get();
    }
    public static void setTemplateId(String value) {
        if (StringUtils.isNotEmpty(template.get())) {
            template.remove();
        }
        template.set(value);
    }
}
