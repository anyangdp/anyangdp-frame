package com.anyangdp.controller;
/**
 * For Business Activities.
 *
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 *
 * Create At 2016年8月24日
 */

import com.anyangdp.handler.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.concurrent.Callable;


@Slf4j
public class ControllerTemplate {

	public static <RQ, RS> Callable<GenericResponse<RS>> spawnAction(ControllerCallback<RS> callback) {
		return spawnAction(null, callback);
	}

	public static <RQ, RS> Callable<GenericResponse<RS>> spawnAction(BindingResult bindingResult,
			ControllerCallback<RS> callback) {
		return new Callable<GenericResponse<RS>>() {

			@Override
			public GenericResponse<RS> call() throws Exception {

				return ControllerTemplate.call(bindingResult, callback);
			}
		};
	}

	public static <RQ, RS> GenericResponse<RS> call(ControllerCallback<RS> callback) throws Exception {
		return ControllerTemplate.call(null, callback);
	}

	public static <RQ, RS> GenericResponse<RS> call(BindingResult bindingResult, ControllerCallback<RS> callback)
			throws Exception {

		GenericResponse<RS> response = new GenericResponse<RS>();

		if (null != bindingResult) {

			log.info("@@@ PROC: action with validator @@@");

			boolean result = validate(bindingResult, response);

			if (result && null != callback) {

				callback.execute(response);
			}
		} else {
			log.info("@@@ PROC: action without validator @@@");

			if (null != callback) {
				callback.execute(response);
			}
		}

		return response;

	}

	protected static <RS> boolean validate(BindingResult bindingResult, GenericResponse<RS> response) {

		boolean result = !bindingResult.hasErrors();

		if (!result) {
			response.setResult(false);
			response.getError().setCode("VALIDATE_ERROR");
			response.getError().setMessage(bindingResult.getAllErrors().toString());
		}

		return result;
	}
}
