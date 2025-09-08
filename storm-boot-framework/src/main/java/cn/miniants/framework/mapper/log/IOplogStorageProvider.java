
package cn.miniants.framework.mapper.log;

import org.aspectj.lang.Signature;

/**

 * ----------------------------------------
 * 日志存储提供者
 *
 * @author hubin
 * @since 2022-08-01
 */
public interface IOplogStorageProvider {

    /**
     * 保存日志
     *
     * @param signature {@link Signature}
     * @param oplog     操作日志
     */
    void save(Signature signature, Oplog oplog);
}
