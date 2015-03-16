package com.github.parisoft.resty.request;

import static com.github.parisoft.resty.utils.StringUtils.urlEncode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

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
                pathBuilder.append("/").append(urlEncode(requestPath));
            }

            path = pathBuilder.toString();
        }

        final String query;

        if (request.queries().isEmpty()) {
            query = null;
        } else {
            final StringBuilder queryBuilder = new StringBuilder();

            for (NameValuePair pair : request.queries()) {
                queryBuilder.append(pair.getName()).append("=").append(urlEncode(pair.getValue())).append("&");
            }

            query = queryBuilder.deleteCharAt(queryBuilder.length() - 1).toString();
        }

        final URI rootUri = request.rootUri;

        try {
            setURI(new URI(rootUri.getScheme(), rootUri.getUserInfo(), rootUri.getHost(), rootUri.getPort(), path, query, rootUri.getFragment()));
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