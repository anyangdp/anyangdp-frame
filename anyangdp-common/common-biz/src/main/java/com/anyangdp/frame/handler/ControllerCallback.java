package com.anyangdp.frame.handler;

/**
 * @author anyangdp
 *
 */
@FunctionalInterface
public interface ControllerCallback<RS> {

	void execute(GenericResponse<RS> response) throws Exception;
}
