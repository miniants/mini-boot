/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.api;

/**
 * 爱组搭 http://aizuda.com
 * ----------------------------------------
 * REST API 错误码接口
 *
 * @author 青苗
 * @since 2021-10-28
 */
public interface IErrorCode {
    /**
     * 错误编码 -1、失败 0、成功
     */
    long getCode();

    /**
     * 错误描述
     */
    String getMsg();
}
