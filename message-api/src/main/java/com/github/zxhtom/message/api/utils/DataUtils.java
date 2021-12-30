package com.github.zxhtom.message.api.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.zxhtom.message.api.annations.Trans;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 拷贝数据
 *
 * @author zxhtom
 * 2021/12/30
 */
public class DataUtils {
    private static DataUtils utils = new DataUtils();
    private DataUtils() {

    }

    public static DataUtils getInstance() {
        return utils;
    }

    public <T> T copyProperties(T t, JSONObject jsonObject) throws IllegalAccessException {
        for (Field declaredField : t.getClass().getDeclaredFields()) {
            final Trans annotation = declaredField.getAnnotation(Trans.class);
            declaredField.setAccessible(true);
            if (null == annotation) {
                declaredField.set(t,jsonObject.get(declaredField.getName()));
                continue;
            }
            final String value = annotation.value();
            declaredField.set(t,jsonObject.get(declaredField.getName()));
        }
        return t;
    }
}
