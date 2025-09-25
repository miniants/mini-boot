
package cn.miniants.framework.mapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

public class MiniSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatch("insertBatch"));
        
        // 逻辑删除字段信息
        TableFieldInfo logic = tableInfo.getLogicDeleteFieldInfo();
        if (logic != null) {
            methodList.add(new RecoverByIds());
//            throw new IllegalStateException(tableInfo.getTableName() + " 未启用 @TableLogic");
        }
        return methodList;
    }
}
