/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.function.Supplier;

/**
 * 爱组搭 http://aizuda.com
 * ----------------------------------------
 * 自定义 Service 基类
 *
 * @author 青苗
 * @since 2021-10-28
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 校验指定条件是否存在
     *
     * @param condition 判断条件
     * @param supplier  查询条件
     * @param message   存在提示
     */
    default void checkExists(boolean condition, Supplier<LambdaQueryWrapper<T>> supplier, String message) {
        if (condition) {
            checkExists(supplier.get(), message);
        }
    }

    /**
     * 校验指定条件是否存在
     *
     * @param lqw     查询条件 LambdaQueryWrapper
     * @param message 存在提示
     */
    default void checkExists(LambdaQueryWrapper<T> lqw, String message) {
        Assert.isNull(getOne(lqw.last("limit 1")), message);
    }

    /**
     * 根据 ID 查询 检查数据合法性
     *
     * @param id 主键ID
     * @return
     */
    default T checkById(Long id) {
        T t = this.getById(id);
        Assert.notNull(t, "指定ID查询数据不存在");
        return t;
    }
}
