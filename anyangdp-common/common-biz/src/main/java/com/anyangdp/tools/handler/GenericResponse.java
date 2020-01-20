package com.anyangdp.tools.handler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通用返回类
 * @param <T>
 */
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
@ApiModel("通用返回类")
public class GenericResponse<T> {
    @ApiModelProperty(value = "返回结果")
    private boolean result;
    @ApiModelProperty(value = "错误内容")
    private ErrorDTO error;
    @ApiModelProperty(value = "返回内容")
    private T data;

}
