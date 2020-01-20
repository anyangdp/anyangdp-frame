package com.anyangdp.tools.handler;

/**
 * @author anyangdp
 *
 */
@FunctionalInterface
public interface ServiceCallback<RS> {

	void execute(GenericResponse<RS> response) throws Exception;
}
