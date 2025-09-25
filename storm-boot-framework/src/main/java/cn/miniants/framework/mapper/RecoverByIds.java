import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class RestoreByIdsMethod extends AbstractMethod {

    public RestoreByIdsMethod() {
        super("restoreByIds"); // 对应 Mapper 接口的方法名
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass,
                                                 Class<?> modelClass,
                                                 TableInfo tableInfo) {

        // 逻辑删除字段信息
        TableFieldInfo logic = tableInfo.getLogicDeleteFieldInfo();
        if (logic == null) {
            throw new IllegalStateException(tableInfo.getTableName() + " 未启用 @TableLogic");
        }

        String table = tableInfo.getTableName();
        String logicCol = logic.getColumn();                 // 逻辑列名，如 deleted
        String delVal   = logic.getLogicDeleteValue();       // 已删值（配置里的一般是 1 / true）
        String notDelVal= logic.getLogicNotDeleteValue();    // 未删值（一般是 0 / false）
        String keyCol   = tableInfo.getKeyColumn();          // 主键列名，如 id

        // 注意：字符串型逻辑值应在配置里写成带引号的常量（如 "'Y'"），MP 会原样拼接
        String sql = """
            <script>
            UPDATE %s
            SET %s = %s
            WHERE %s = %s
              AND %s IN
              <foreach collection="ids" item="id" open="(" separator="," close=")">
                #{id}
              </foreach>
            </script>
            """.formatted(table, logicCol, notDelVal, logicCol, delVal, keyCol);

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        // 注册为 update 类型的 MappedStatement
        return this.addUpdateMappedStatement(mapperClass, modelClass, this.methodName, sqlSource);
    }
}
