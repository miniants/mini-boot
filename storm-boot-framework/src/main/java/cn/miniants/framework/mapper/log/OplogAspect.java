
package cn.miniants.framework.mapper.log;

import cn.miniants.framework.spring.SpringHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**

 * ----------------------------------------
 * 操作日志拦截切面
 *
 * @author hubin
 * @since 2022-08-01
 */
@Aspect
public class OplogAspect {
    private IOplogStorageProvider oplogStorageProvider;

    public OplogAspect(IOplogStorageProvider oplogStorageProvider) {
        this.oplogStorageProvider = oplogStorageProvider;
    }

    /**
     * 配置环绕通知
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return
     * @throws Throwable
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object oplogAround(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Oplog oplog = OplogContext.builder().around(pjp.getSignature(),
                SpringHelper.getCurrentRequest(), pjp.getArgs());
        Object result;
        try {
            // 执行原方法
            result = pjp.proceed();
            // 执行成功
            oplog.status(true);
        } catch (Throwable t) {
//            if (t instanceof ApiException) {
//                // 逻辑异常
//                oplog.content(t.getMessage()).failed();
//            } else {
//                // 系统异常
//                oplog.exception(ThrowableUtils.getStackTrace(t));
//            }
            throw t;
        } finally {
            // 保存操作日志
            oplogStorageProvider.save(pjp.getSignature(), oplog.build(System.currentTimeMillis() - beginTime));
            // 删除操作日志
            OplogContext.remove();
        }
        return result;
    }
}
