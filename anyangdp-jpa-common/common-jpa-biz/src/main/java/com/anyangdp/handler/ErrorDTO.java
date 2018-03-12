/**
 * For Business Activities.
 *
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 *
 * Create At 2016年8月24日
 */
package com.anyangdp.handler;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author william
 *
 */
@Data
@NoArgsConstructor
public class ErrorDTO {

	private String code;

	private String message;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
