package com.hxy.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期格式转换工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DateTimeUtil
 * @date 2019年07月15日 17:38:23
 */
public class DateTimeUtil {

    /**
     * 默认的format格式
     */
    private static final String STANDER_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转Date
     *
     * @param dateTimeStr 时间字符串
     * @param formatStr   时间格式
     * @return
     */
    public static Date str2Date(String dateTimeStr, String formatStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = formatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * Date转字符串
     *
     * @param date      时间
     * @param formatStr 字符串格式
     * @return
     */
    public static String date2Str(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
     * 字符串转Date，时间格式为默认的标准时间格式
     *
     * @param dateTimeStr 时间字符串
     * @return
     */
    public static Date str2Date(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(STANDER_FORMAT);
        DateTime dateTime = formatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * Date转字符串,时间格式为默认的标准时间格式
     *
     * @param date 时间
     * @return
     */
    public static String date2Str(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDER_FORMAT);
    }
}
