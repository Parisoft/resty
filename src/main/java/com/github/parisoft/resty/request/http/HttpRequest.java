package com.github.parisoft.resty.request.http;

import static com.github.parisoft.resty.utils.StringUtils.urlEncode;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import com.github.parisoft.resty.client.Client;
import com.github.parisoft.resty.entity.EntityWriterImpl;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.response.ResponseInvocationHandler;

public class HttpRequest extends HttpEntityEnclosingRequestBase {

    protected final Client client;
    protected final String method;

    public HttpRequest(Client client, String method) throws IOException {
        this.client = client;
        this.method = method;
        constructConfig();
        constructHeaders();
        constructUri();
        constructEntity();
    }

    private void constructConfig() {
        final int timeout = client.timeout();

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        setConfig(requestConfig);
    }

    private void constructHeaders() {
        for (Entry<String, List<String>> header : client.headers().entrySet()) {
            final StringBuilder valueBuilder = new StringBuilder();

            for (String value : header.getValue()) {
                valueBuilder.append(value).append(", ");
            }

            valueBuilder.delete(valueBuilder.length() - 2, valueBuilder.length());

            addHeader(header.getKey(), valueBuilder.toString());
        }
    }

    private void constructUri() {
        final StringBuilder uriBuilder = new StringBuilder(client.rootPath());

        for (String path : client.paths()) {
            uriBuilder.append("/").append(urlEncode(path));
        }

        if (!client.queries().isEmpty()) {
            uriBuilder.append("?");

            for (NameValuePair pair : client.queries()) {
                uriBuilder.append(pair.getName()).append("=").append(urlEncode(pair.getValue())).append("&");
            }

            uriBuilder.deleteCharAt(uriBuilder.length() - 1);
        }

        setURI(URI.create(uriBuilder.toString()));
    }

    private void constructEntity() throws IOException {
        final HttpEntity entity = new EntityWriterImpl(client).getEntity();
        setEntity(entity);
    }

    public Response submit() throws IOException {
        final HttpResponse httpResponse = client
                .toHttpClient()
                .execute(this);

        return proxyResponse(httpResponse);
    }

    private Response proxyResponse(HttpResponse httpResponse) {
        return (Response) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ Response.class },
                new ResponseInvocationHandler(httpResponse));
    }

    @Override
    public String getMethod() {
        return method;
    }
}