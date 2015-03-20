package com.github.parisoft.resty.utils;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.jaxrs.base.ProviderBase;
import com.github.parisoft.resty.processor.DataProcessors;

public class JacksonUtils {

    public static <T> T read(HttpEntity entity, Class<T> someClass, MediaType contentType) throws IOException {
        for (ProviderBase<?, ?, ?, ?> provider : DataProcessors.getInstance().values()) {
            if (provider.isReadable(someClass, null, null, contentType)) {
                return provider
                        .locateMapper(someClass, contentType)
                        .readValue(entity.getContent(), someClass);
            }
        }

        throw new IOException(String.format("no processors found for class=%s and Content-Type=%s", someClass.getName(), contentType));
    }

    public static <T> T read(HttpEntity entity, TypeReference<T> reference, MediaType contentType) throws IOException {
        for (ProviderBase<?, ?, ?, ?> provider : DataProcessors.getInstance().values()) {
            if (provider.isReadable(reference.getClass(), reference.getType(), null, contentType)) {
                return provider
                        .locateMapper(reference.getClass(), contentType)
                        .readValue(entity.getContent(), reference);
            }
        }

        throw new IOException(String.format("no processors found for type=%s and Content-Type=%s", reference.getType(), contentType));
    }

    public static <T> T read(String content, Class<T> someClass, MediaType contentType) throws IOException {
        for (ProviderBase<?, ?, ?, ?> provider : DataProcessors.getInstance().values()) {
            if (provider.isReadable(someClass, null, null, contentType)) {
                return provider
                        .locateMapper(someClass, contentType)
                        .readValue(content, someClass);
            }
        }

        throw new IOException(String.format("no processors found for class=%s and Content-Type=%s", someClass.getName(), contentType));
    }

    public static <T> T read(String content, TypeReference<T> reference, MediaType contentType) throws IOException {
        for (ProviderBase<?, ?, ?, ?> provider : DataProcessors.getInstance().values()) {
            if (provider.isReadable(reference.getClass(), reference.getType(), null, contentType)) {
                return provider
                        .locateMapper(reference.getClass(), contentType)
                        .readValue(content, reference);
            }
        }

        throw new IOException(String.format("no processors found for type=%s and Content-Type=%s", reference.getType(), contentType));
    }


    public static String write(Object object, MediaType contentType) throws IOException {
        for (ProviderBase<?, ?, ?, ?> provider : DataProcessors.getInstance().values()) {
            if (provider.isWriteable(object.getClass(), null, null, contentType)) {
                return provider
                        .locateMapper(object.getClass(), contentType)
                        .writeValueAsString(object);
            }
        }

        throw new IOException(String.format("no processors found for class=%s and Content-Type=%s", object.getClass(), contentType));
    }
}
