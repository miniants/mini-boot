package cn.miniants.framework.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.miniants.framework.api.ApiResult;
import cn.miniants.framework.exception.MiniFeignException;
import cn.miniants.toolkit.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Response;
import feign.codec.ErrorDecoder;

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
        if(isMiniApi) {
            String responseBody;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
                responseBody = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                throw new MiniFeignException(-1, "处理FeignClient异常时发生错误");
            }

            String message = response.reason();
            int code = response.status();
            if (StrUtil.isNotBlank(responseBody)) {
                JsonNode resp = JSONUtil.readTree(responseBody);
                if (null != resp.get("message")) {
                    message = resp.get("message").asText();
                }
                throw new MiniFeignException(code, message);
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }


        // 对于其他错误，使用默认的ErrorDecoder
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
