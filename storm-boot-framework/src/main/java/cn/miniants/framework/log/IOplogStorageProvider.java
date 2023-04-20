/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.log;

import org.aspectj.lang.Signature;

/**
 * 爱组搭 http://aizuda.com
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
     * @return
     */
    void save(Signature signature, Oplog oplog);
}
