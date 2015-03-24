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
package com.github.parisoft.resty.request;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.utils.URIBuilder;

import com.github.parisoft.resty.entity.EntityWriterImpl;

/**
 * Implementation of {@link HttpEntityEnclosingRequestBase}.
 *
 * @author Andre Paris
 *
 */
public class HttpRequest extends HttpEntityEnclosingRequestBase {

    protected final Request request;
    protected final String method;

    /**
     * Creates an instance of HttpRequest from a {@link Request} configuration and a HTTP method.
     *
     * @param request The request who carries all the HTTP meta data like header, path, query and entity
     * @param method The request method like GET, POST, PUT, DELETE, or other
     * @throws IOException In case some problem occurs during the URI's creation or the entity's processing
     */
    public HttpRequest(Request request, String method) throws IOException {
        this.request = request;
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