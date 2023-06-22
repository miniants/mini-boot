package cn.miniants.framework.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.miniants.framework.api.ApiResult;
import cn.miniants.framework.exception.MiniFeignException;
import cn.miniants.toolkit.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Response;
import feign.codec.ErrorDecoder;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static cn.miniants.framework.constant.StormwindConstant.MiniConstants.HTTP_HEAD_MINI_API;

public class MiniFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        boolean isMiniApi = ObjectUtil.isNotNull(response.headers().get(HTTP_HEAD_MINI_API)) && response.headers().get(HTTP_HEAD_MINI_API).stream().anyMatch(value -> value.contains("true"));

        // 在这里，您可以根据响应状态码或其他信息自定义异常处理逻辑
        if (isMiniApi) {
            String importMsg = "=====>FeignClient返回异常：%s--->%s:".formatted(response.request().url(), methodKey);

            String responseBody;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
                responseBody = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                throw new MiniFeignException(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "读取响应体失败", importMsg);
            }

            int code = response.status();
            String message = response.reason();
            if (StrUtil.isNotBlank(responseBody)) {
                JsonNode resp = JSONUtil.readTree(responseBody);
                if (null != resp.get("message")) {
                    message = resp.get("message").asText();
                }
                throw new MiniFeignException(code, message, importMsg);
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }


        // 对于其他错误，使用默认的ErrorDecoder
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
