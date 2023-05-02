package cn.miniants.toolkit;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

public class MiniStrUtil {
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
        return getOrDefault(obj, "");
    }
    public static String getOrDefault(Object obj, String defaultStr) {
        if(null == obj) {
            return defaultStr;
        }
        if(obj instanceof String) {
            return (String) obj;
        }else if(obj instanceof JsonNode){
            return ((JsonNode) obj).asText();
        }else {
            return obj.toString();
        }
    }
}
