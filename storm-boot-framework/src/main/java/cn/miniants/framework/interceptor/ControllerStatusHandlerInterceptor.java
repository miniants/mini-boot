package cn.miniants.framework.interceptor;

import cn.hutool.core.thread.ThreadUtil;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.framework.web.ValidationControllerMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ControllerStatusHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        ThreadLocalUtils
        if(handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod)handler;
            if(null!=h.getMethodAnnotation(ValidationControllerMethod.class)){
                SpringHelper.setValidationControllerMethod();
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
    }
}
