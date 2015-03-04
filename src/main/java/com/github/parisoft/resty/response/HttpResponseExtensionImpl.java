package com.github.parisoft.resty.response;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class HttpResponseExtensionImpl implements HttpResponseExtension {

    private final HttpResponse httpResponse;
    private Response responseProxy;

    private List<HttpCookie> cookies;

    public HttpResponseExtensionImpl(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void setResponse(Response response) {
        responseProxy = response;
    }

    @Override
    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    @Override
    public Response assertOk() {
        return assertStatus(HttpStatus.SC_OK);
    }

    @Override
    public Response assertStatus(int expectedStatus) {
        final int actualStatus = getStatusCode();

        if (expectedStatus != actualStatus) {
            throw new IllegalStateException(String.format("Cannot assert response status code: expected %s, got %s", expectedStatus, actualStatus));
        }

        return responseProxy;
    }

    @Override
    public HttpCookie getCookie(String name) throws IOException {
        for (HttpCookie cookie : getCookies()) {
            if (Objects.equals(cookie.getName(), name)) {
                return cookie;
            }
        }

        return null;
    }

    @Override
    public List<HttpCookie> getCookies() throws IOException {
        if (cookies != null) {
            return cookies;
        }

        final Header cookieHeader = httpResponse.getFirstHeader("Set-Cookie");

        if (cookieHeader == null) {
            return cookies = Collections.emptyList();
        }

        try {
            return cookies = HttpCookie.parse(cookieHeader.toString());
        } catch (Exception e) {
            throw new IOException("Cannot parse response cookie", e);
        }
    }
}
