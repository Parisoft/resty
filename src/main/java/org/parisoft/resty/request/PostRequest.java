package org.parisoft.resty.request;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.parisoft.resty.Client;
import org.parisoft.resty.utils.JacksonUtils;

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
        } else {
            final String rawType = client.headers().get(CONTENT_TYPE);
            final MediaType type;

            try {
                type = MediaType.valueOf(rawType);
            } catch (Exception e) {
                throw new IOException("Cannot write request entity: unknown Content-Type=" + rawType);
            }

            final String entityString = JacksonUtils.write(rawEntity, type);
            entity = new StringEntity(entityString);
        }

        setEntity(entity);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

}
