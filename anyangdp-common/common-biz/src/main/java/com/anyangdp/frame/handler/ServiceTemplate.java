package com.anyangdp.frame.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.concurrent.Callable;

/**
 * @author anyangdp
 *
 */
@Slf4j
public class ServiceTemplate {

	public static <RQ, RS> Callable<GenericResponse<RS>> spawnAction(ServiceCallback<RS> callback) {
		return spawnAction(null, callback);
	}

	public static <RQ, RS> Callable<GenericResponse<RS>> spawnAction(BindingResult bindingResult,
                                                                     ServiceCallback<RS> callback) {
		return () -> call(bindingResult, callback);
	}

	public static <RQ, RS> GenericResponse<RS> call(ServiceCallback<RS> callback) throws Exception {
		return ServiceTemplate.call(null, callback);
	}

	public static <RQ, RS> GenericResponse<RS> call(BindingResult bindingResult, ServiceCallback<RS> callback)
			throws Exception {

		GenericResponse<RS> response = new GenericResponse<RS>();
		callback.execute(response);
		return response;
	}
}
