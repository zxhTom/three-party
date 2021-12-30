package com.github.zxhtom.message.api.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 * zxhtom
 */
public class DateUtils {
    private static DateUtils dateUtils = new DateUtils();
    private DateUtils() {

    }

    public static DateUtils getInstance() {
        return dateUtils;
    }


    /**
     * 时间横跳，秒
     * @param date 起始点
     * @param length 长度
     * @return
     */
    public Date judgeTime(Date date, Integer length) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.SECOND,length);
        return instance.getTime();
    }

    public Long chargeDate(Date startTime, Date endTime) {
        return endTime.getTime()-startTime.getTime();
    }
}
