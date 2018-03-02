/**
 * For Business Activities.
 * <p>
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 * <p>
 * Create At 2016年8月24日
 */
package com.anyangdp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ValidatorDelegate implements Validator {

    private static Map<Class<?>, Validator> validators = new ConcurrentHashMap<Class<?>, Validator>();

    public void addValidator(Class<?> clazz, Validator validator) {
        if (!validators.containsKey(clazz)) {
            validators.put(clazz, validator);
        }
    }

    public void removeValidator(Class<?> clazz) {
        if (validators.containsKey(clazz)) {
            validators.remove(clazz);
        }
    }

    /**
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        Validator validator = validators.get(target.getClass());
        if (null != validator && validator.supports(target.getClass())) {
            validator.validate(target, errors);
        }
    }

}
