package com.github.parisoft.resty.client;

import static com.github.parisoft.resty.utils.ArrayUtils.isEmpty;
import static com.github.parisoft.resty.utils.StringUtils.emptyIfNull;
import static com.github.parisoft.resty.utils.StringUtils.splitAfterSlashes;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import com.github.parisoft.resty.request.Request;
import com.github.parisoft.resty.request.RequestRetryHandler;
import com.github.parisoft.resty.request.ssl.BypassTrustStrategy;
import com.github.parisoft.resty.utils.CookieUtils;

public class Client {
    private static final String NULL_VALUE = null;

    private Map<String, List<String>> headers = new HashMap<>();
    private List<NameValuePair> queries = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private String rootPath;
    private int retries = 0;
    private int timeout = 0;
    private boolean bypassSSL = true;
    private Object entity;

    public Client(String baseAddress) {
        this(pathToUri(baseAddress));
    }

    public Client(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Cannot create a client: URI cannot be null");
        }

        path(emptyIfNull(uri.getPath()));

        try {
            query(URLEncodedUtils.parse(uri, UTF_8.name()));
            rootPath = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, uri.getFragment()).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create a client: " + e.getMessage());
        }
    }

    private static URI pathToUri(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Cannot create a client: basic path cannot be null");
        }

        return URI.create(path);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public String rootPath() {
        return rootPath;
    }

    public List<String> paths() {
        return paths;
    }

    public List<NameValuePair> queries() {
        return queries;
    }

    public int retries() {
        return retries;
    }

    public int timeout() {
        return timeout;
    }

    public boolean bypassSSL() {
        return bypassSSL;
    }

    public Object entity() {
        return entity;
    }

    public Client accept(ContentType... contentTypes) {
        if (isEmpty(contentTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (ContentType contentType : contentTypes) {
            header(ACCEPT, contentType.toString());
        }

        return this;
    }

    public Client accept(MediaType... mediaTypes) {
        if (isEmpty(mediaTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (MediaType mediaType : mediaTypes) {
            header(ACCEPT, mediaType.toString());
        }

        return this;
    }

    public Client type(ContentType contentType) {
        if (contentType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, contentType.toString());
    }

    public Client type(MediaType mediaType) {
        if (mediaType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, mediaType.toString());
    }

    public Client cookie(HttpCookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Client cookie(Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Client cookie(org.apache.http.cookie.Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Client cookie(String cookieAsString) {
        return header("Cookie", cookieAsString);
    }

    public Client header(HttpHeaders name, String... values) {
        return header(name.toString(), values);
    }

    public Client header(String name, String... values) {
        if (isEmpty(values)) {
            headers.remove(name);
            return this;
        }

        for (String value : values) {
            final List<String> valueList = headers.containsKey(name) ? headers.get(name) : new ArrayList<String>();
            valueList.add(value);
            headers.put(name, valueList);
        }

        return this;
    }

    public Client query(List<NameValuePair> nameValuePairs) {
        queries.addAll(nameValuePairs);

        return this;
    }

    public Client query(String name, String value) {
        queries.add(new BasicNameValuePair(name, value));

        return this;
    }

    public Client path(String... paths) {
        if (isEmpty(paths)) {
            throw new IllegalArgumentException("Cannot crate a client: path cannot be null");
        }

        for (String path : paths) {
            this.paths.addAll(splitAfterSlashes(path));
        }

        return this;
    }

    public Client retries(int retries) {
        this.retries = retries;

        return this;
    }

    public Client timeout(int value, TimeUnit unit) {
        timeout = (int) unit.toMillis(value);

        return this;
    }

    public Client bypassSSL(boolean bypassSSL) {
        this.bypassSSL = bypassSSL;

        return this;
    }

    public Client entity(Object entity) {
        this.entity = entity;

        return this;
    }

    public Request request() {
        return new Request(this);
    }

    public HttpClient toHttpClient() throws IOException {
        final SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(timeout)
                .build();


        final SSLContext sslContext;
        final HostnameVerifier hostnameVerifier;

        if (bypassSSL) {
            hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            try {
                sslContext = SSLContexts
                        .custom()
                        .loadTrustMaterial(new BypassTrustStrategy())
                        .useProtocol(SSLConnectionSocketFactory.TLS)
                        .build();
            } catch (Exception e) {
                throw new IOException("Cannot create bypassed SSL context", e);
            }
        } else {
            sslContext = SSLContexts.createSystemDefault();
            hostnameVerifier = null;
        }

        final HttpRequestRetryHandler retryHandler = new RequestRetryHandler(retries);

        return HttpClientBuilder
                .create()
                .setRetryHandler(retryHandler)
                .setDefaultSocketConfig(socketConfig)
                .setSslcontext(sslContext)
                .setSSLHostnameVerifier(hostnameVerifier)
                .build();
    }
}