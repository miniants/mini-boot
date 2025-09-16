
package cn.miniants.framework.advice;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.miniants.framework.api.ApiResult;
import cn.miniants.framework.api.IErrorCode;
import cn.miniants.framework.exception.ApiException;
import cn.miniants.framework.exception.MiniFeignException;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.toolkit.JSONUtil;
import feign.FeignException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.miniants.framework.constant.StormwindConstant.MiniConstants.HTTP_HEAD_MINI_API;

/**
 * Service 异常处理
 *
 * @author 青苗
 * @since 2021-09-29
 */
@Slf4j
@RestControllerAdvice
public class MiniControllerResultAdvice implements ResponseBodyAdvice<Object> {
    /// //////////////处理报文响应结构//////////////////////
    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
        // void 返回值不包裹
        Method m = returnType.getMethod();
        return m != null && m.getReturnType() != Void.TYPE;
    }

    /// //////////////确保我们所有的RestController返回的结构除了明确使用MiniControllerResult标识skipWrapper的时候都返回ApiResult的结构//////////////////////
    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        //框架的接口都用Mini-Api标识
        //当前RestControllerAdvice会在controller会写的时候触发，标识这个后，方便框架内FeignClient知道这个是我们服务内的调用
        response.getHeaders().add(HTTP_HEAD_MINI_API, "true");

        if (body instanceof ApiResult) {
            return body;
        }
        Method method = returnType.getMethod();
        if (method != null) {
            MiniControllerResult myAnnotation = method.getAnnotation(MiniControllerResult.class);
            // 如果MyAnnotation注解存在，则执行相应的操作
            if (myAnnotation != null && myAnnotation.skipWrapper()) {
                return body;
            }
        }

        ApiResult<Object> apiResult = ApiResult.ok(body);
        if (returnType.getParameterType().isAssignableFrom(String.class)) {
            // 字符串类型特殊处理
            return JSONUtil.toJSONString(apiResult);
        }
        return apiResult;
    }


    /// //////////////处理异常报文//////////////////////
    @ExceptionHandler(value = {Exception.class})
    public ApiResult<Object> handleException(Exception ex, HttpServletResponse resp, HttpServletRequest req) {
        return handleBadRequest(ex, resp, req);
    }

    /// //////////////内部函数几公共函数//////////////////////
    private static String convertFiledErrors(List<FieldError> fieldErrors) {
        // 转换FieldError列表为错误提示信息
        return Optional.ofNullable(fieldErrors)
//                .filter(fieldErrorsInner -> enableValidationMessage)
                .map(fieldErrorsInner -> fieldErrorsInner.stream()
                        .flatMap(fieldError -> Stream.of(fieldError.getField() + " " + fieldError.getDefaultMessage()))
                        .collect(Collectors.joining(", ")))
                .orElse(null);
    }

    private static String convertConstraintViolationsToMessage(ConstraintViolationException constraintViolationException) {
        //转换ConstraintViolationException 异常为错误提示信息
        return Optional.ofNullable(constraintViolationException.getConstraintViolations())
//                .filter(constraintViolations -> enableValidationMessage)
                .map(constraintViolations -> constraintViolations.stream().flatMap(constraintViolation -> {
                            String path = constraintViolation.getPropertyPath().toString();
                            String errorMessage = path.substring(path.lastIndexOf(".") + 1) +
                                    " " + constraintViolation.getMessage();
                            return Stream.of(errorMessage);
                        }).collect(Collectors.joining(", "))
                ).orElse(null);
    }

    /* ---------- Utils ---------- */
    private static boolean isClientAbort(Throwable e) {
        // 统一识别：Tomcat 的 ClientAbortException、HTTP 客户端重置、Broken pipe/Connection reset
        Throwable root = ExceptionUtil.getRootCause(e);
        Throwable t = (root != null) ? root : e;
        String msg = (t.getMessage() == null ? "" : t.getMessage()).toLowerCase();
        return (t instanceof org.apache.catalina.connector.ClientAbortException)
                || (t instanceof java.io.EOFException)
                || msg.contains("broken pipe")
                || msg.contains("connection reset by peer")
                || msg.contains("connection reset");
    }

    private static Throwable unwrapNested(Throwable e) {
        if (e instanceof org.springframework.web.util.NestedServletException && e.getCause() != null) {
            return e.getCause();
        }
        return e;
    }

    public static ApiResult<Object> handleBadRequest(Exception ex, HttpServletResponse resp, HttpServletRequest req) {
        // ---- 关键修复 1：unwrap 后优先识别客户端断开/响应已提交 ----
        Throwable e = unwrapNested(ex);
        if (null == resp || resp.isCommitted() || isClientAbort(e)) {
            if (null != resp && !resp.isCommitted()) resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            log.debug("[CLIENT-ABORT] {} {} -> {}", req.getMethod(), req.getRequestURI(), e.getMessage());
            return null; // 不写 body，不再抛
        }

        ApiResult<Object> res;
        //------ 业务逻辑异常,这里的异常都使用 ApiAssert.assertApi方法抛出,这类异常以200代码返回---//
        if (e instanceof ApiException) {
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            return null != errorCode ?ApiResult.failed(errorCode):ApiResult.failed(e.getMessage());

        //------ 业务逻辑异常,这里的异常都使用Assert.assert方法抛出---//
        }else if(e instanceof IllegalArgumentException) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ApiResult.failed(e.getMessage());

        //------ 业务逻辑异常, 参数验证错误---//
        } else if (e instanceof ConstraintViolationException) {
            //     * ConstraintViolationException 验证异常处理 - @Validated加在 controller 类上，且在参数列表中直接指定constraints时触发
            // 这里没有设置res.status,默认为200，约束参数异常在apiResult中用-1来处理。
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ApiResult.failed(convertConstraintViolationsToMessage((ConstraintViolationException) e));

        //------ BindException异常处理，里面有个特殊的就是前端数据给后端验证方法来校验数据而不弹出错误---//
        } else if (e instanceof BindException) {
            //     * BindException 验证异常处理 - form参数（对象参数，没有加 @RequestBody）触发
            //     * MethodArgumentNotValidException 验证异常处理 - 在 @RequestBody 上添加 @Validated 处触发 是BindException的子类
            res = ApiResult.failed(convertFiledErrors(((BindException)e).getBindingResult().getFieldErrors()));
            if (SpringHelper.isValidationControllerMethod()) {
                //201是验证错误码，前端不会弹出ElMessage,修改为正常的状态码,默认异常code是=-1，request.ts中会弹出异常提示窗
                resp.setStatus(HttpServletResponse.SC_OK);
                res.setCode(201);
            }
            return res;

        //------  鉴权异常处理（25/9/12分析这里应该不会出现这个）---//
        } else if (e instanceof JwtException) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ApiResult.result(e, 4001, "token过期或无效");

        //------ FeignClient服务调用的自己框架异常处理---//
        } else if (e instanceof MiniFeignException) {
            //这里的异常是MiniFeignDecoder MiniFeignErrorDecoder 抛出的
            resp.setStatus(((MiniFeignException) e).getErrorCode());
            res = ApiResult.failed(e.getMessage());
            res.setErrorDetails(((MiniFeignException) e).getFeignTraceMessage());
            return res;

        //------ FeignClient服务调用系统框架异常处理---//
        } else if (e instanceof FeignException) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res = ApiResult.failed(e.getMessage());
            return res;

        //------ 其他的异常 ---//
        } else {
            // 系统内部异常，打印异常栈
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            res = ApiResult.failed("Service Error:%s".formatted(e.getMessage()));
            log.error("Service Error: {}", ExceptionUtil.stacktraceToString(e));
            return res;
        }
    }
}

