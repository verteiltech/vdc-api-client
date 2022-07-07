package com.verteil.apiclient.connector.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verteil.apiclient.config.ObjectMapperConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class NdcResponseBodyHandler<W> implements HttpResponse.BodyHandler<W> {

    private Class<W> wClass;

    public NdcResponseBodyHandler(Class<W> wClass) {
        this.wClass = wClass;
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(wClass);
    }

    public static <T> HttpResponse.BodySubscriber<T> asJSON(Class<T> targetType) {
        HttpResponse.BodySubscriber<String> upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                (String body) -> {
                    try {
                        ObjectMapper objectMapper = ObjectMapperConfig.getInstance().createMapper();
                        return objectMapper.readValue(body, targetType);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
}

