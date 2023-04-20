/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.bean;

import cn.miniants.framework.validation.Create;
import cn.miniants.framework.validation.Update;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 爱组搭 http://aizuda.com
 * ----------------------------------------
 * 基础类
 *
 * @author 青苗
 * @since 2021-10-28
 */
@Setter
@Getter
public class SuperEntity implements BeanConvert {

    /**
     * 主键
     */
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;

}
