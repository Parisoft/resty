package com.github.parisoft.resty.request.http;

import java.io.IOException;

import org.apache.http.HttpEntity;

import com.github.parisoft.resty.Client;
import com.github.parisoft.resty.entity.EntityWriterImpl;

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
