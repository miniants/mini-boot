package cn.miniants.toolkit;

import java.util.Random;

public class StrUtil {
    public static String createRandomStr1(int length){

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random = new Random();

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < length; i++) {

            int number = random.nextInt(62);

            stringBuffer.append(str.charAt(number));

        }

        return stringBuffer.toString();

    }

    public static String getOrEmpty(Object obj) {
        if(null == obj) {
            return "";
        }
        if(obj instanceof String) {
            return (String) obj;
        }else {
            return obj.toString();
        }
    }
}
