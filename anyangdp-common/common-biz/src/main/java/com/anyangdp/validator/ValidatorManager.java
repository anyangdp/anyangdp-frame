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
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component
public class ValidatorManager {

    @Resource
    private ValidatorDelegate validatorDelegate;

    public Validator getValidator() {
        return validatorDelegate;
    }
}
