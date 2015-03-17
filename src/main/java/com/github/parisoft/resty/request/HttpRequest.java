package com.github.parisoft.resty.request;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.utils.URIBuilder;

import com.github.parisoft.resty.entity.EntityWriterImpl;

public class HttpRequest extends HttpEntityEnclosingRequestBase {

    protected final Request request;
    protected final String method;

    public HttpRequest(Request client, String method) throws IOException {
        this.request = client;
        this.method = method;
        constructHeaders();
        constructUri();
        constructEntity();
    }

    private void constructHeaders() {
        for (Entry<String, List<String>> header : request.headers().entrySet()) {
            final StringBuilder valueBuilder = new StringBuilder();

            for (String value : header.getValue()) {
                valueBuilder.append(value).append(", ");
            }

            valueBuilder.delete(valueBuilder.length() - 2, valueBuilder.length());

            addHeader(header.getKey(), valueBuilder.toString());
        }
    }

    private void constructUri() throws IOException {
        final String path;

        if (request.paths().isEmpty()) {
            path = null;
        } else {
            final StringBuilder pathBuilder = new StringBuilder();

            for (String requestPath : request.paths()) {
                pathBuilder.append("/").append(requestPath);
            }

            path = pathBuilder.toString();
        }

        try {
            setURI(request.queries().isEmpty()
                    ? new URIBuilder(request.rootUri).setPath(path).build()
                            : new URIBuilder(request.rootUri).setPath(path).setParameters(request.queries()).build());
        } catch (URISyntaxException e) {
            throw new IOException("Cannot create the request URI", e);
        }
    }

    private void constructEntity() throws IOException {
        final HttpEntity entity = new EntityWriterImpl(request).getEntity();
        setEntity(entity);
    }

    @Override
    public String getMethod() {
        return method;
    }
}