package org.parisoft.resty.request.http;

import static org.parisoft.resty.utils.StringUtils.urlEncode;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.parisoft.resty.Client;
import org.parisoft.resty.request.RequestRetryHandler;
import org.parisoft.resty.request.ssl.BypassTrustStrategy;
import org.parisoft.resty.response.Response;
import org.parisoft.resty.response.ResponseInvocationHandler;

public abstract class HttpRequest extends HttpEntityEnclosingRequestBase {

    protected final Client client;

    HttpRequest(Client client) {
        this.client = client;
        constructConfig();
        constructHeaders();
        constructUri();
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

    public Response submit() throws IOException {
        final HttpResponse httpResponse = newHttpClient().execute(this);

        return proxyResponse(httpResponse);
    }

    private HttpClient newHttpClient() throws IOException {
        final SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(client.timeout())
                .build();


        final SSLContext sslContext;

        try {
            sslContext = SSLContexts
                    .custom()
                    .loadTrustMaterial(new BypassTrustStrategy())
                    .useProtocol(SSLConnectionSocketFactory.TLS)
                    .build();
        } catch (Exception e) {
            throw new IOException("Cannot create bypassed SSL context", e);
        }

        final HttpRequestRetryHandler retryHandler = new RequestRetryHandler(client.retries());

        return HttpClientBuilder
                .create()
                .setRetryHandler(retryHandler)
                .setDefaultSocketConfig(socketConfig)
                .setSslcontext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
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