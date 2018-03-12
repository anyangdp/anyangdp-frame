/**
 * For Business Activities.
 * <p>
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 * <p>
 * Create At 2016年8月24日
 */
package com.anyangdp.handler;

import lombok.Data;

/**
 * @author william
 *
 */
@Data
public class GenericResponse<T> {

    private boolean result;

    private ErrorDTO error;

    private T data;

    private PageDTO page;

    public GenericResponse() {
    }

    public GenericResponse(T data, boolean result) {
        this.data = data;
    }
}
