package com.anyangdp.tools.handler;

/**
 * @author anyangdp
 *
 */
@FunctionalInterface
public interface ControllerCallback<RS> {

	void execute(GenericResponse<RS> response) throws Exception;
}
