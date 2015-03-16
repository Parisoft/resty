package com.github.parisoft.resty.entity;

import static com.github.parisoft.resty.utils.ObjectUtils.isPrimitive;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.github.parisoft.resty.request.Request;
import com.github.parisoft.resty.utils.JacksonUtils;
import com.github.parisoft.resty.utils.MediaTypeUtils;

public class EntityWriterImpl implements EntityWriter {

    private final Request request;

    public EntityWriterImpl(Request request) {
        this.request = request;
    }

    @Override
    public HttpEntity getEntity() throws IOException {
        final Object rawEntity = request.entity();
        final String rawType = request.headers().containsKey(CONTENT_TYPE) ? request.headers().get(CONTENT_TYPE).get(0) : null;
        final HttpEntity entity;

        try {
            if (rawEntity == null || rawEntity instanceof HttpEntity) {
                entity = (HttpEntity) rawEntity;
            } else if (rawEntity instanceof String || isPrimitive(rawEntity)) {
                final ContentType contentType = (rawType == null) ? null : ContentType.parse(rawType);

                entity = new StringEntity(rawEntity.toString(), contentType);
            } else {
                final ContentType contentType = ContentType.parse(rawType);
                final MediaType mediaType = MediaTypeUtils.valueOf(contentType);
                final String entityAsString = JacksonUtils.write(rawEntity, mediaType);

                entity = new StringEntity(entityAsString, contentType);
            }
        } catch (Exception e) {
            throw new IOException("Cannot write request entity: " + e.getMessage());
        }

        return entity;
    }

}
