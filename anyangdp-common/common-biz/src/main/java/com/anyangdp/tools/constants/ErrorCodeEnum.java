package com.anyangdp.tools.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限错误：1000~1999
 * 业务相关：
     * 用户错误 10000~19999
     * 参数错误 20000~29999
     * 接口异常 30000~39999
 * @Author anyang
 * @CreateTime 2019/10/31
 * @Des 错误code枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /*权限错误 1000~1999*/
    TOKEN_ERROR(1000, "身份令牌有误", "token invalid"),
    PERMISSION_NOT_ACCESS(1001, "权限不足，禁止访问", "insufficient permissions to access"),
    /*网关等等系统平台级别异常 2000~2999*/
    SERVICE_EXCEPTION(2000, "服务异常，稍候重试", "service exception"),
    /*用户错误 10000~19999*/
    USER_NOT_EXIST(10000, "用户不存在", "user not exist"),
    /*参数错误 20000~29999*/
    PARAM_IS_INVALID(20000, "参数无效", "param is invalid"),
    NUMBER_FORMAT_EXCEPTION(20001, "数字格式异常", "number format exception"),
    NOT_FOUND_EXCEPTION(20001, "接口地址错误", "Interface address error"),
    /*接口异常, 30000~39999*/
    PARAM_IS_BLANK(30000, "接口异常", "interface exception"),
    ;
    private Integer code;
    private String message;
    private String enMessage;

}
