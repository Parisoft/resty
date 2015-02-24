package org.parisoft.resty.utils;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.codehaus.jackson.type.TypeReference;
import org.parisoft.resty.RESTy;

public class JacksonUtils {

    public static <T> T read(HttpEntity entity, Class<T> someClass, MediaType type) throws IOException {
        if (RESTy.getJsonProvider().isReadable(someClass, null, null, type)) {
            return RESTy.getJsonProvider().locateMapper(someClass, type).readValue(entity.getContent(), someClass);
        } else if (RESTy.getXmlProvider().isReadable(someClass, null, null, type)) {
            return RESTy.getXmlProvider().locateMapper(someClass, type).readValue(entity.getContent(), someClass);
        } else {
            throw new IOException(String.format("Cannot read response entity: no providers found for class=%s and Content-Type=%s", someClass.getName(), type));
        }
    }

    public static <T> T read(HttpEntity entity, TypeReference<T> reference, MediaType type) throws IOException {
        if (RESTy.getJsonProvider().isReadable(reference.getClass(), reference.getType(), null, type)) {
            return RESTy.getJsonProvider().locateMapper(reference.getClass(), type).readValue(entity.getContent(), reference);
        } else if (RESTy.getXmlProvider().isReadable(reference.getClass(), reference.getType(), null, type)) {
            return RESTy.getXmlProvider().locateMapper(reference.getClass(), type).readValue(entity.getContent(), toXmlReference(reference));
        } else {
            throw new IOException(String.format("Cannot read response entity: no providers found for class=%s and Content-Type=%s", reference.getType(), type));
        }
    }

    public static String write(Object object, MediaType type) throws IOException {
        if (RESTy.getJsonProvider().isWriteable(object.getClass(), null, null, type)) {
            return RESTy.getJsonProvider().locateMapper(object.getClass(), type).writeValueAsString(object);
        } else if (RESTy.getXmlProvider().isWriteable(object.getClass(), null, null, type)) {
            return RESTy.getXmlProvider().locateMapper(object.getClass(), type).writeValueAsString(object);
        } else {
            throw new IOException(String.format("Cannot write request entity: no providers found for class=%s and Content-Type=%s", object.getClass(), type));
        }
    }

    public static <T> com.fasterxml.jackson.core.type.TypeReference<T> toXmlReference(final TypeReference<T> jsonReference) {
        return new com.fasterxml.jackson.core.type.TypeReference<T>() {
            @Override
            public Type getType() {
                return jsonReference.getType();
            }
        };
    }

    public static <T> TypeReference<T> toJsonReference(final com.fasterxml.jackson.core.type.TypeReference<T> xmlReference) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return xmlReference.getType();
            }
        };
    }
}
