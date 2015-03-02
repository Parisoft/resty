package org.parisoft.resty.request.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.parisoft.resty.Client;
import org.parisoft.resty.entity.EntityWriterImpl;

public abstract class HttpRequestByEntity extends HttpRequest {

    HttpRequestByEntity(Client client) throws IOException {
        super(client);
        constructEntity();
    }

    private void constructEntity() throws IOException {
        final HttpEntity entity = new EntityWriterImpl(client).getEntity();
        setEntity(entity);
    }
}
