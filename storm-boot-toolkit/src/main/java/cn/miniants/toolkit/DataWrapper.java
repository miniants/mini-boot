package cn.miniants.toolkit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据包装。 在lamda、与函数等 参数作用域的包装使用
 *
 * @author guoqianyou
 * @date 2022/10/25 21:15
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataWrapper<T> {
    private T data;
}
