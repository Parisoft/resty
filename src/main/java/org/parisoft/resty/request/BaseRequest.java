package org.parisoft.resty.request;

import static org.parisoft.resty.utils.StringUtils.htmlEncode;

import java.net.URI;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.parisoft.resty.Client;

abstract class BaseRequest extends HttpEntityEnclosingRequestBase {

    BaseRequest(Client client) {
        convertConfig(client);
        convertHeaders(client);
        convertUri(client);
    }

    private void convertConfig(Client client) {
        final int timeout = client.timeout();

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        setConfig(requestConfig);
    }

    private void convertHeaders(Client client) {
        for (Entry<String, String> entry : client.headers().entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    private void convertUri(Client client) {
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
}