package com.xazhao.common;

import com.xazhao.constant.HttpStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName InvokeResult.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

@Getter
@Setter
public class InvokeResult {

    /**
     * 返回值数据
     */
    private Object data;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 返回值错误信息
     */
    private String returnMsg;

    /**
     * 接口返回值代码
     */
    private Integer code;

    /**
     * 登陆接口token
     */
    private String token;

    public static InvokeResult success(Object data) {
        InvokeResult result = new InvokeResult();
        result.data = data;
        result.code = HttpStatus.SUCCESS;
        return result;
    }

    public static InvokeResult success(Object data, String returnMsg) {
        InvokeResult result = new InvokeResult();
        result.data = data;
        result.code = HttpStatus.SUCCESS;
        result.returnMsg = returnMsg;
        return result;
    }

    public static InvokeResult success() {
        InvokeResult result = new InvokeResult();
        result.code = HttpStatus.SUCCESS;
        return result;
    }

    public static InvokeResult failure(Object data, String message) {
        InvokeResult result = new InvokeResult();
        result.data = data;
        result.code = HttpStatus.ERROR;
        result.returnMsg = message;
        return result;
    }

    public static InvokeResult failure(String message) {
        InvokeResult result = new InvokeResult();
        result.code = HttpStatus.ERROR;
        result.returnMsg = message;
        return result;
    }

    public static InvokeResult failure() {
        InvokeResult result = new InvokeResult();
        result.code = HttpStatus.ERROR;
        return result;
    }

    public static InvokeResult failure(String returnMsg, String errorMsg) {
        InvokeResult result = new InvokeResult();
        result.code = HttpStatus.ERROR;
        result.returnMsg = returnMsg;
        result.setErrorMsg(errorMsg);
        return result;
    }
}
