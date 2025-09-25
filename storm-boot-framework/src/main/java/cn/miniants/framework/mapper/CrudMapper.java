
package cn.miniants.framework.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface CrudMapper<T> extends BaseMapper<T> {

    Integer insertBatch(@Param("list") Collection<T> t);

    int recoverByIds(@Param("ids") Collection<?> ids);
}
