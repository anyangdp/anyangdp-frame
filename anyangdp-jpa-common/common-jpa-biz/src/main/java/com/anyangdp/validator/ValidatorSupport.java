/**
 * For Business Activities.
 *
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 *
 * Create At 2016年8月24日
 */
package com.anyangdp.validator;

import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public abstract class ValidatorSupport implements ValidatorType, Validator {

	@Resource
	private ValidatorDelegate validatorDelegate;

	@PostConstruct
	void registerValidator() {
		validatorDelegate.addValidator(validateType(), this);
	}

	@PreDestroy
	void unregisterValidator() {
		validatorDelegate.removeValidator(validateType());
	}

	/**
	 * @param clazz
	 * @return
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return validateType().isAssignableFrom(clazz);
	}

}
