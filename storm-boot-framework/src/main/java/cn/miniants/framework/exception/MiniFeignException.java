package cn.miniants.framework.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MiniFeignException extends RuntimeException{
    private final int errorCode;

    public MiniFeignException(int errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
