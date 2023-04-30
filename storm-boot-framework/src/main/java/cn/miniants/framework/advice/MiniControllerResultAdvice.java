/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.advice;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.miniants.framework.api.ApiResult;
import cn.miniants.framework.api.IErrorCode;
import cn.miniants.framework.exception.ApiException;
import cn.miniants.framework.exception.MiniFeignException;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.toolkit.JSONUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    /////////////////处理报文响应结构//////////////////////
    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
        return !returnType.getMethod().getReturnType().isAssignableFrom(Void.TYPE);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        //框架的接口都用Mini-Api标识
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


    /////////////////处理异常报文//////////////////////
    @Value("${spring.validation.message.enable:true}")
    private Boolean enableValidationMessage;

    /**
     * 转换FieldError列表为错误提示信息
     *
     * @param fieldErrors
     * @return
     */
    private String convertFiledErrors(List<FieldError> fieldErrors) {
        return Optional.ofNullable(fieldErrors)
                .filter(fieldErrorsInner -> this.enableValidationMessage)
                .map(fieldErrorsInner -> fieldErrorsInner.stream()
                        .flatMap(fieldError -> Stream.of(fieldError.getField() + " " + fieldError.getDefaultMessage()))
                        .collect(Collectors.joining(", ")))
                .orElse(null);
    }

    /**
     * 转换ConstraintViolationException 异常为错误提示信息
     *
     * @param constraintViolationException
     * @return
     */
    private String convertConstraintViolationsToMessage(ConstraintViolationException constraintViolationException) {
        return Optional.ofNullable(constraintViolationException.getConstraintViolations())
                .filter(constraintViolations -> this.enableValidationMessage)
                .map(constraintViolations -> constraintViolations.stream().flatMap(constraintViolation -> {
                            String path = constraintViolation.getPropertyPath().toString();
                            StringBuffer errorMessage = new StringBuffer();
                            errorMessage.append(path.substring(path.lastIndexOf(".") + 1));
                            errorMessage.append(" ").append(constraintViolation.getMessage());
                            return Stream.of(errorMessage.toString());
                        }).collect(Collectors.joining(", "))
                ).orElse(null);
    }

    private Map<Path, String> convertConstraintViolationsToMap(ConstraintViolationException constraintViolationException) {

        // 获取异常信息
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        // 将异常信息收集到Map，key为校验失败的字段，value为失败原因
        Map<Path, String> errorPahtMap = constraintViolations.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage));
        // 返回校验失败信息


        return errorPahtMap;
    }

    /**
     * 不支持的方法
     *
     * @param httpServletResponse
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<String> httpExceptionHandler(HttpServletResponse httpServletResponse, HttpRequestMethodNotSupportedException ex) {
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ApiResult.failed("请求错误：%s".formatted(ex.getMessage()));
    }

    /**
     * jwt 校验异常捕获处理
     *
     * @return
     */
    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ApiResult<String> jwtExceptionHandler(HttpServletResponse httpServletResponse, JwtException ex) {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ApiResult.failed("身份验证失败(token失效)，请重新验证身份!");
    }

    /**
     * jwt时间失效异常处理
     *
     * @param httpServletResponse
     * @param exception
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResult<String> jwtExpiredExceptionHandler(HttpServletResponse httpServletResponse, ExpiredJwtException exception) {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ApiResult.result(null, 4001, "token无效");
    }

    /**
     * BindException 验证异常处理 - form参数（对象参数，没有加 @RequestBody）触发
     * MethodArgumentNotValidException 验证异常处理 - 在 @RequestBody 上添加 @Validated 处触发
     * ConstraintViolationException 验证异常处理 - @Validated加在 controller 类上，且在参数列表中直接指定constraints时触发
     *
     * @param request {@link HttpServletRequest}
     * @param e       {@link BindException}
     * @return
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult<String> handleBindException(HttpServletRequest request, BindException e) {
        ApiResult<String> res = ApiResult.failed(this.convertFiledErrors(e.getBindingResult().getFieldErrors()));
        if (SpringHelper.isValidationControllerMethod()) {
            res.setCode(-2);//-2是验证错误码，前端不会弹出ElMessage
        }
        return res;
    }


    /**
     * 自定义 REST 业务异常
     *
     * @param e    异常类型
     * @param resp 响应请求
     * @return
     */
    @ExceptionHandler(value = {Throwable.class, IllegalArgumentException.class})
    public ApiResult<Object> handleBadRequest(Throwable e, HttpServletResponse resp) {
        ApiResult<Object> res = null;

        if (e instanceof ApiException) {
            // 业务逻辑异常
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            if (null != errorCode) {
                res = ApiResult.failed(errorCode);
            }
            res = ApiResult.failed(e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            // 断言异常
            res = ApiResult.failed(e.getMessage());
        } else if (e instanceof NestedServletException) {
            // 参数缺失
            res = ApiResult.failed(e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            // 请求参数无法读取
            res = ApiResult.failed(e.getMessage());
        } else if (e instanceof ConstraintViolationException) {
            res = ApiResult.failed(this.convertConstraintViolationsToMessage((ConstraintViolationException) e));
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            resp.setStatus(301);
            return ApiResult.failed("请求参数错误");
        } else if (e instanceof MiniFeignException) {
            return ApiResult.failed("Feign调用异常%s".formatted(e.getMessage()));
        } else {
            // 系统内部异常，打印异常栈
            log.error("Error: handleBadRequest StackTrace : {}", ExceptionUtil.stacktraceToString(e));
            res = ApiResult.failed("Internal Server Error");
        }

        if (SpringHelper.isValidationControllerMethod()) {
            res.setCode(-2);//-2是验证错误码，前端不会弹出ElMessage
        }

        return res;
    }
}

