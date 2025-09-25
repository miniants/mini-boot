
package cn.miniants.framework.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.miniants.framework.exception.ApiException;

public interface BeanConvert {

    /**
     * 获取自动转换后的JavaBean对象
     *
     * @param clazz 转换对象类
     * @param <T>   转换对象
     * @return T 待转换对象
     */
    default <T> T convert(Class<T> clazz) {
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(this,t);
            return t;
        } catch (Exception e) {
            throw new ApiException("转换对象失败", e);
        }
    }
}
