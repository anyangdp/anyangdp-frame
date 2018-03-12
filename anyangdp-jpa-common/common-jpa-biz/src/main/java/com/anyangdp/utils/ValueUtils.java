package com.anyangdp.utils;
import org.springframework.beans.BeanUtils;

public class ValueUtils {

    public static <S,T> T dump(S source ,Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
            BeanUtils.copyProperties(source,t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
