
package cn.miniants.framework.mapper.log;

/**

 * ----------------------------------------
 * 操作日志上下文
 *
 * @author hubin
 * @since 2022-08-01
 */
public class OplogContext {
    /**
     * 操作日志本地线程
     */
    protected static ThreadLocal<Oplog> OPLOG_HOLDER = new ThreadLocal<>();

    /**
     * 构建操作日志上下文对象
     *
     * @return
     */
    public static Oplog builder() {
        Oplog oplog = OPLOG_HOLDER.get();
        if (null == oplog) {
            oplog = new Oplog();
        }
        return oplog;
    }

    /**
     * 删除本地线程操作日志
     */
    public static void remove() {
        OPLOG_HOLDER.remove();
    }
}
