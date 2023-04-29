package cn.miniants.framework.constant;

/**
 * @author guoqianyou
 * @date 2022/10/21 10:48
 */
public interface StormwindConstant {
    interface StoreBucket {
        String SCANNER_BUCKET = "scanner";
        String FILE_BUCKET = "files";
        String STUDENT_PHOTO_BUCKET = "photos";
    }

    interface AuthConstants{
        String JWT_CREDENTIALS = "Credentials";
        String JWT_USER_SESSION = "UserSession";

    }

    interface GatewayConstants{
        String SERVICE_INSTANCE_ID = "instance-id";
        String SERVICE_INSTANCE_HOST = "instance-host";
    }
}
