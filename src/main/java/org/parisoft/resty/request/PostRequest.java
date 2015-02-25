package org.parisoft.resty.request;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.parisoft.resty.utils.ObjectUtils.isPrimitive;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.parisoft.resty.Client;
import org.parisoft.resty.utils.JacksonUtils;
import org.parisoft.resty.utils.MediaTypeUtils;

public class PostRequest extends Request {

    PostRequest(Client client) throws IOException {
        super(client);
        convertEntity();
    }

    private void convertEntity() throws IOException {
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
            final MediaType type;
            final String entityAsString;

            try {
                type = MediaTypeUtils.valueOf(rawType);
                entityAsString = JacksonUtils.write(rawEntity, type);
            } catch (Exception e) {
                throw new IOException("Cannot write request entity: " + e.getMessage());
            }

            entity = new StringEntity(entityAsString);
        }

        setEntity(entity);
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
