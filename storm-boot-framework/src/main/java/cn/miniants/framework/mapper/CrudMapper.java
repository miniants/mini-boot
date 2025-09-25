
package cn.miniants.framework.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface CrudMapper<T> extends BaseMapper<T> {

    Integer insertBatch(@Param("list") Collection<T> t);

    /**
     * 只有逻辑删除的表才会添加这个方法。这个控制逻辑在@MiniSqlInjector中。
     * @param ids
     * @return
     */
    int recoverByIds(@Param("ids") Collection<?> ids);
}
