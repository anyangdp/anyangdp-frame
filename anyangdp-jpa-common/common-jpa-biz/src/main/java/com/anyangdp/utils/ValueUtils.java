package com.anyangdp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class ValueUtils {

    public static <S, T> T dump(S source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <S, T> T dump(S source, T t) {
        t = DozerUtils.copy(source, t);
        return t;
    }

}
