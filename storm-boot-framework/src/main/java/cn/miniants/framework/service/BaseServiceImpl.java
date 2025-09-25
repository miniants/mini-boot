
package cn.miniants.framework.service;

import cn.miniants.framework.mapper.CrudMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public class BaseServiceImpl<M extends CrudMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

}
