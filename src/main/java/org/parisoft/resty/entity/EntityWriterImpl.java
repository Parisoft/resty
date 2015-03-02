package org.parisoft.resty.entity;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.parisoft.resty.utils.ObjectUtils.isPrimitive;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.parisoft.resty.Client;
import org.parisoft.resty.utils.JacksonUtils;
import org.parisoft.resty.utils.MediaTypeUtils;

public class EntityWriterImpl implements EntityWriter {

    private final Client client;

    public EntityWriterImpl(Client client) {
        this.client = client;
    }

    @Override
    public HttpEntity getEntity() throws IOException {
        final Object rawEntity = client.entity();
        final HttpEntity entity;

        if (rawEntity == null || rawEntity instanceof HttpEntity) {
            entity = (HttpEntity) rawEntity;
        } else if (rawEntity instanceof String) {
            entity = new StringEntity((String) rawEntity);
        } else if (isPrimitive(rawEntity)) {
            entity = new StringEntity(rawEntity.toString());
        } else {
            final String rawType = client.headers().containsKey(CONTENT_TYPE) ? client.headers().get(CONTENT_TYPE).get(0) : null;
            final MediaType contentType;
            final String entityAsString;

            try {
                contentType = MediaTypeUtils.valueOf(rawType);
                entityAsString = JacksonUtils.write(rawEntity, contentType);
            } catch (Exception e) {
                throw new IOException("Cannot write request entity: " + e.getMessage());
            }

            entity = new StringEntity(entityAsString);
        }

        return entity;
    }

}
