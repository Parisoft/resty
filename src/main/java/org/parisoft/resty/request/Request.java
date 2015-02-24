package org.parisoft.resty.request;

import static org.parisoft.resty.utils.StringUtils.htmlEncode;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.parisoft.resty.Client;
import org.parisoft.resty.response.Response;
import org.parisoft.resty.response.ResponseInvocationHandler;

public abstract class Request extends HttpEntityEnclosingRequestBase {

    protected final Client client;

    Request(Client client) {
        this.client = client;
        convertConfig();
        convertHeaders();
        convertUri();
    }

    private void convertConfig() {
        final int timeout = client.timeout();

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        setConfig(requestConfig);
    }

    private void convertHeaders() {
        for (Entry<String, String> header : client.headers().entrySet()) {
            addHeader(header.getKey(), header.getValue());
        }
    }

    private void convertUri() {
        final String fullPath;

        if (client.queries().isEmpty()) {
            fullPath = client.path();
        } else {
            final StringBuilder uriBuilder = new StringBuilder(client.path()).append("?");

            for (NameValuePair pair : client.queries()) {
                uriBuilder.append(pair.getName()).append("=").append(htmlEncode(pair.getValue())).append("&");
            }

            fullPath = uriBuilder.deleteCharAt(uriBuilder.length() - 1).toString();
        }

        setURI(URI.create(fullPath));
    }

    public Response submit() throws IOException {
        final HttpResponse httpResponse = newHttpClient().execute(this);

        return proxyResponse(httpResponse);
    }

    private HttpClient newHttpClient() {
        final SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(client.timeout())
                .build();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setValidateAfterInactivity(-1);

        final HttpRequestRetryHandler retryHandler = new RequestRetryHandler(client.retries());

        return HttpClientBuilder
                .create()
                .setRetryHandler(retryHandler)
                .setConnectionManager(connectionManager)
                .build();
    }

    private Response proxyResponse(HttpResponse httpResponse) {
        return (Response) Proxy
                .newProxyInstance(
                        getClass().getClassLoader(),
                        new Class[]{ Response.class },
                        new ResponseInvocationHandler(httpResponse));
    }
}