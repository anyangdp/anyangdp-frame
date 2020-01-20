package com.anyangdp.tools.util;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述 通用工具类
 * @创建人  anyang
 * @创建时间  2019/11/26
 * @修改人和其它信息
 */
@Slf4j
public class CommonUtil {

    //根据身份证号得到年龄
    public static Integer idCardToAge(String idCard) {
        String dateStr;
        int invalidAge = -1;
        if (idCard.length() == 15) {
            dateStr = "19" + idCard.substring(6, 12);
        } else if (idCard.length() == 18) {
            dateStr = idCard.substring(6, 14);
        } else {//默认是合法身份证号，但不排除有意外发生
            return invalidAge;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return getAgeByDate(birthday);
        } catch (ParseException e) {
            return invalidAge;
        }
    }

    public static int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        int invalidAge = -1;
        //calendar.before()有的点bug
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return invalidAge;
        }
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthday);
        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirthday;
        if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
            age--;
        }
        return age;
    }

    // 根据身份证号得到性别
    public static String idCardToSex(String idCard) {
        if (!ObjectUtils.isEmpty(idCard) && idCard.length() >= 18) {
            String id17 = idCard.substring(16, 17);
            if (Integer.parseInt(id17) % 2 != 0) {
                return "男";
            } else {
                return "女";
            }
        }
        return "未知";
    }

    /*
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(!String.valueOf(value).equals("null")){
                map.put(fieldName, value);
            }

        }
        return map;
    }
    /*
     * 获取利用反射获取类里面的值和名称(驼峰)
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMapLowerUnderScoreKey(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(!String.valueOf(value).equals("null")){
                map.put(fieldName, value);
            }

        }
        return map;
    }

}
