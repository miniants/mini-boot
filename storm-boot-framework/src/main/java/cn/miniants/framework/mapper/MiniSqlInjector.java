
package cn.miniants.framework.mapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**

 * ----------------------------------------
 * 批量插入注入器
 *
 * @author 青苗
 * @since 2021-10-28
 */
public class AizudaSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatch("insertBatch"));
        methodList.add(new RecoverByIds());
        return methodList;
    }
}
