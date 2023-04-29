package cn.miniants.framework.advice;

import cn.miniants.framework.api.ApiResult;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

public class CustomFeignDecoder implements Decoder {

    private final Decoder delegate;

    public CustomFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
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

        ApiResult<?> apiResponse = (ApiResult<?>) delegate.decode(response, wrappedType);
        return apiResponse.getData();
    }
}
