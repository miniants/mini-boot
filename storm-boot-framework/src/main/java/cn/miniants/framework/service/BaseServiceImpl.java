
package cn.miniants.framework.service;

import cn.miniants.framework.mapper.CrudMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**

 * ----------------------------------------
 * 自定义 IBaseService 实现
 *
 * @author 青苗
 * @since 2021-10-28
 */
public class BaseServiceImpl<M extends CrudMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

}
