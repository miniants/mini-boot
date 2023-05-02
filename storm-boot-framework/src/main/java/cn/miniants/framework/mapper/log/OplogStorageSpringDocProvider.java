///*
// * 爱组搭 http://aizuda.com 低代码组件化开发平台
// * ------------------------------------------
// * 受知识产权保护，请勿删除版权申明
// */
//package cn.miniants.framework.log;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.reflect.MethodSignature;
//
///**
// * 爱组搭 http://aizuda.com
// * ----------------------------------------
// * 日志存储 Spring Doc 文档注解获取提供者
// *
// * @author hubin
// * @since 2022-08-01
// */
//public abstract class OplogStorageSpringDocProvider implements IOplogStorageProvider {
//
//    /**
//     * 获取 Spring Doc 文档注解信息，保存日志
//     *
//     * @param signature {@link Signature}
//     * @param oplog     操作日志
//     * @return
//     */
//    @Override
//    public void save(Signature signature, Oplog oplog) {
//        // 操作模块
//        if (null == oplog.getModule()) {
//            Tag tag = AnnotationUtil.getAnnotation(signature.getDeclaringType(), Tag.class);
//            if (null != tag) {
//                oplog.module(tag.name());
//            }
//        }
//        // 操作业务
//        if (null == oplog.getBusiness() && signature instanceof MethodSignature) {
//            MethodSignature ms = (MethodSignature) signature;
//            Operation operation = AnnotationUtil.getAnnotation(ms.getMethod(), Operation.class);
//            if (null != operation) {
//                oplog.business(operation.summary());
//            }
//        }
//        // 保存操作日志
//        this.save(oplog);
//    }
//
//    /**
//     * 保存日志
//     *
//     * @param oplog 操作日志
//     */
//    public abstract void save(Oplog oplog);
//}
