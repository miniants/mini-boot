package cn.miniants.framework.advice;

import cn.miniants.framework.api.ApiResult;
import cn.miniants.toolkit.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应方法返回值处理类
 *
 * @author hubin
 * @since 2021-12-10
 */
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 所有的返回都做处理，包括void
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
        return !returnType.getMethod().getReturnType().isAssignableFrom(Void.TYPE);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        if (body instanceof ApiResult) {
            return body;
        }
        ApiResult<Object> apiResult = ApiResult.ok(body);
        if (returnType.getParameterType().isAssignableFrom(String.class)) {
            // 字符串类型特殊处理
            return JSONUtil.toJSONString(apiResult);
        }
        return apiResult;
    }
}
