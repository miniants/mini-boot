
package cn.miniants.framework.spring;

import cn.miniants.toolkit.ThreadLocalUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**

 * ----------------------------------------
 * Spring Boot 相关辅助类
 *
 * @author 青苗
 * @since 2021-10-28
 */
@SuppressWarnings("unused")
public class SpringHelper {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 获取当前请求
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            // 这里不能抛出异常，存在为 null 的场景
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 设置 applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (null == APPLICATION_CONTEXT) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * 获取 applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    private final static String ValidationControllerMethod_TLFLAG = "ValidationControllerMethod_TLFLAG";
    public static boolean isValidationControllerMethod(){
        if(null==ThreadLocalUtils.get(ValidationControllerMethod_TLFLAG))
            return false;
        return Boolean.TRUE.equals(ThreadLocalUtils.get(ValidationControllerMethod_TLFLAG));
    }

    public static void setValidationControllerMethod(){
        ThreadLocalUtils.put(ValidationControllerMethod_TLFLAG, true);
    }
}
