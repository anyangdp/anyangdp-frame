package com.anyangdp.frame.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author anyang
 * @CreateTime 2019/11/15
 * @Des
 */
public class PropertiesUtil {
    /**
     * 获取properties文件的单个值
     *
     * @param key      键
     * @param fileName 文件名称
     * @return 值
     */
    public static String getProperty(String key, String fileName) {
        Properties prop = new Properties();
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}
