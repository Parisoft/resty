package org.parisoft.resty.request;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.parisoft.resty.Client;

public class Request {

    public static HttpResponse doGet(Client client) throws IOException {
        return newHttpClient(client)
                .execute(new GetRequest(client));
    }

    private static HttpClient newHttpClient(Client client) {
        final SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(client.timeout())
                .build();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setValidateAfterInactivity(-1);
        connectionManager.setDefaultSocketConfig(socketConfig);

        final HttpRequestRetryHandler retryHandler = new RequestRetryHandler(client.retries());

        return HttpClientBuilder
                .create()
                .setRetryHandler(retryHandler)
                .setConnectionManager(connectionManager)
                .build();
    }
}
