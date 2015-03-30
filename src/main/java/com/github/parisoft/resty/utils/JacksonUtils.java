/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
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
