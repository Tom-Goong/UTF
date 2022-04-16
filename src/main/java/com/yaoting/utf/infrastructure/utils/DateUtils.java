package com.yaoting.utf.infrastructure.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static Date long2Date(Long time){
        return new Date(time);
    }

    /**
     * 获取nDay的起始时间
     */
    public static Long getDayStarTime(int nDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, nDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public static Date getMonthStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getMonthEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date.getTime());
    }

    public static String getFormattedDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date.getTime());
    }

    public static String getFormattedDate(Long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
    }

    public static Integer compare(Date t1, Date t2){
        return Long.compare(t1.getTime(), t2.getTime());
    }

    @SneakyThrows
    public static Date getProperDate(String dateStr) {
        Preconditions.notBlank(dateStr, "参数不能为空");

        if (dateStr.endsWith("Z")) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            return sf.parse(dateStr.replace("Z", " UTC"));
        } else if (dateStr.contains("+08:00")) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            return sf.parse(dateStr);
        } else {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.parse(dateStr);
        }
    }
}
