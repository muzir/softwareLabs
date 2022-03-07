package com.softwarelabs.spring.kafka.apithrottling.config;

import feign.Feign;
import feign.Response;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Should be used together with {@link Feign.Builder#decode404()}.
 */
public final class NotFoundDecoder implements Decoder {

    private final Decoder delegate;

    public NotFoundDecoder(Decoder delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.status() == 404) {
            return null;
        }
        return delegate.decode(response, type);
    }
}
