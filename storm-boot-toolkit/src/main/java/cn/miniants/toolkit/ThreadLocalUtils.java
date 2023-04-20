package cn.miniants.toolkit;

import java.util.HashMap;
import java.util.Map;


public class ThreadLocalUtils {
    /**
     * 请求参数存取
     */
    private static final ThreadLocal<Map<String, Object>> REQUEST_DATA = new ThreadLocal<>();

    /**
     * 设置请求参数
     *
     * @param key   请求参数 key
     * @param value 请求参数 value
     */
    public static void put(String key, Object value) {
        Map<String, Object> dataMap = get();
        if (null == dataMap) {
            dataMap = new HashMap<>();
        }
        dataMap.put(key, value);
        REQUEST_DATA.set(dataMap);
    }

    /**
     * 设置请求参数
     *
     * @param dataMap 请求参数 MAP 对象
     */
    public static void put(Map<String, Object> dataMap) {
        REQUEST_DATA.set(dataMap);
    }

    /**
     * 获取请求参数
     *
     * @param key 请求参数 key
     * @return 请求参数 MAP 对象
     */
    public static <T> T get(String key) {
        Map<String, Object> dataMap = get();
        return null == dataMap ? null : (T) dataMap.get(key);
    }

    /**
     * 获取请求参数
     *
     * @return 请求参数 MAP 对象
     */
    public static Map<String, Object> get() {
        return REQUEST_DATA.get();
    }

    /**
     * 删除指定请求参数
     *
     * @param key 请求参数key
     */
    public static void remove(String key) {
        Map<String, Object> dataMap = get();
        if (null != dataMap) {
            dataMap.remove(key);
            REQUEST_DATA.set(dataMap);
        }
    }

    /**
     * 删除所有请求参数
     */
    public static void removeAll() {
        REQUEST_DATA.remove();
    }
}
