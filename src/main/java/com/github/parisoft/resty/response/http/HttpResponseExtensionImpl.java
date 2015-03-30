/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.parisoft.resty.response.http;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import com.github.parisoft.resty.exception.IllegalHttpStatusCode;
import com.github.parisoft.resty.response.Response;

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
    public Response assertOk() throws IllegalHttpStatusCode {
        return assertStatus(HttpStatus.SC_OK);
    }

    @Override
    public Response assertStatus(int expectedStatus) throws IllegalHttpStatusCode {
        final int actualStatus = getStatusCode();

        if (expectedStatus != actualStatus) {
            throw new IllegalHttpStatusCode(expectedStatus, actualStatus);
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
