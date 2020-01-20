package com.anyangdp.frame.handler;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author anyang
 * @CreateTime 2019/10/9
 * @Des 底层通用抽象实体DTO
 */
@Accessors(chain = true)
@Data
public abstract class AbstractDTO implements Serializable {
    private static final long serialVersionUID = -5756045370916112553L;

    private String id;
}
