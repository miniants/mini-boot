package cn.miniants.framework.advice;

import cn.miniants.framework.api.ApiResult;
import cn.miniants.toolkit.JSONUtil;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.miniants.framework.constant.StormwindConstant.MiniConstants.HTTP_HEAD_MINI_API;

public class MiniFeignDecoder implements Decoder {

    private final Decoder delegate;

    public MiniFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.delegate = new SpringDecoder(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Type wrappedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return ApiResult.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        //如果是框架的api都会包装成ApiResult，所以这里需要判断一下
        Map<String, Collection<String>> headers = response.headers();
        if (headers.get(HTTP_HEAD_MINI_API).stream().anyMatch(value -> value.contains("true"))
                || headers.get("Content-Type").stream().anyMatch(value -> value.contains("text/plain") || value.contains("json/application"))
        ) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
                String responseBody = reader.lines().collect(Collectors.joining("\n"));
                ApiResult<?> apiResponse = JSONUtil.readValue(responseBody, wrappedType);
                return null == apiResponse ? null : apiResponse.getData();
            }
        }

        //其他情况，直接调用原来的解码器
        ApiResult<?> apiResponse = (ApiResult<?>) delegate.decode(response, wrappedType);
        return apiResponse.getData();
    }
}
