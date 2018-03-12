package com.anyangdp.controller;

import com.anyangdp.handler.GenericResponse;

/**
 * For Business Activities.
 *
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 *
 * Create At 2016年8月24日
 */


@FunctionalInterface
public interface ControllerCallback<RS> {

	void execute(GenericResponse<RS> response);
}
