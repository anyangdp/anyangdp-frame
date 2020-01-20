/**
 * For Business Activities.
 *
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 *
 * Create At 2016年9月4日
 */
package com.anyangdp.tools.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author anyangdp
 *
 */
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * 获得超类的参数类型，取第一个参数类型
	 * 
	 * @param <T>
	 *            类型参数
	 * @param clazz
	 *            超类类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class<?> clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 根据索引获得超类的参数类型
	 * 
	 * @param clazz
	 *            超类类型
	 * @param index
	 *            索引
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

}
