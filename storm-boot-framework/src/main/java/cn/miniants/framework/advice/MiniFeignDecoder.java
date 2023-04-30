package cn.miniants.framework.advice;

import cn.miniants.framework.api.ApiResult;
import cn.miniants.toolkit.JSONUtil;
import feign.Response;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

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

        if (response.headers().get("Content-Type").stream().anyMatch(value -> value.contains("text/plain"))) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
                String responseBody = reader.lines().collect(Collectors.joining("\n"));
                // 在这里，你可以将 responseBody 反序列化为你需要的对象类型。
                // 例如，如果你知道响应只包含一个数字，你可以将其解析为一个整数。
                ApiResult<?> apiResponse = JSONUtil.readValue(responseBody, wrappedType);
                return null==apiResponse?null:apiResponse.getData();
            }
        }


        ApiResult<?> apiResponse = (ApiResult<?>) delegate.decode(response, wrappedType);
        return apiResponse.getData();
    }
}
