package com.furongsoft.base.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 *
 * @author linyehai
 */
public class DateUtils {
    /**
     * 获取Date格式的今日、昨日、明日
     *
     * @return 三日Map
     */
    public static Map<String, Date> getYesterdayTodayTomorrow() {
        Map<String, Date> map = new HashMap<>();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int day = calendar.get(Calendar.DATE);
        map.put("today", today);
        calendar.set(Calendar.DATE, day - 1);
        map.put("yesterday", calendar.getTime());
        calendar.set(Calendar.DATE, day + 1);
        map.put("tomorrow", calendar.getTime());
        return map;
    }

    /**
     * 获取字符串的昨日、今日、明日   默认yyyy-MM-dd HH:mm:ss格式
     *
     * @return 字符串型三日Map
     */
    public static Map<String, String> getYesterdayTodayTomorrowString(String formatString) {
        if (StringUtils.isNullOrEmpty(formatString)) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        Map<String, Date> dateMap = getYesterdayTodayTomorrow();
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        Map<String, String> map = new HashMap<>();
        map.put("today", sdf.format(dateMap.get("today")));
        map.put("yesterday", sdf.format(dateMap.get("yesterday")));
        map.put("tomorrow", sdf.format(dateMap.get("tomorrow")));
        return map;
    }
}
