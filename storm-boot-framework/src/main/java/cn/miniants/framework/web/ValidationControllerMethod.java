package cn.miniants.framework.web;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ValidationControllerMethod {
    //这个注解是用来标识 具体 controller用来验证数据的controller，他的code返回-2，前端request中不会自动弹出错误。
    String value() default "";//默认为空，因为名字是value，实际操作中可以不写"value="
}
