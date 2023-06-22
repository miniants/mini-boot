package cn.miniants.framework.exception;

import cn.miniants.framework.api.IErrorCode;

public class ApiAssert {
    public static void isTrue(boolean exp, String message) {
        if(!exp)
            throw new ApiException(message);
    }
    public static void isTrue(boolean exp, IErrorCode code) {
        if(!exp)
            throw new ApiException(code);
    }
}
