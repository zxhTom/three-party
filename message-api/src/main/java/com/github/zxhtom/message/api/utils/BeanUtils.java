package com.github.zxhtom.message.api.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.annations.Trans;

import java.lang.reflect.Field;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/7
 */
public class BeanUtils {
    public static <T> T transBeanWithTranAnnotation(JSONObject jsonObject , T t) {
        final Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            final Trans annotation = declaredField.getAnnotation(Trans.class);
            if (null != annotation) {
                String value = annotation.value();
                declaredField.setAccessible(true);
                try {
                    declaredField.set(t,jsonObject.get(value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
