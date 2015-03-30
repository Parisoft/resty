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
import java.util.List;

import com.github.parisoft.resty.exception.IllegalHttpStatusCode;
import com.github.parisoft.resty.response.Response;

/**
 * Interface that contains useful methods to deal with an HTTP response.
 *
 * @author Andre Paris
 *
 */
public interface HttpResponseExtension {

    /**
     * @return The response HTTP status code
     */
    public int getStatusCode();

    /**
     * @return This response
     * @throws IllegalHttpStatusCode If the response HTTP status code differs from 200 - Ok
     */
    public Response assertOk() throws IllegalHttpStatusCode;

    /**
     * @param expectedHttpStatusCode The expected response HTTP status code
     * @return This response
     * @throws IllegalHttpStatusCode If the response HTTP status code differs from the expected
     */
    public Response assertStatus(int expectedHttpStatusCode) throws IllegalHttpStatusCode;

    /**
     * @param name The cookie name
     * @return An instance of {@link HttpCookie} for a cookie with the given name, or <code>null</code> if the response headers not contains the cookie
     * @throws IOException In case some problems occurs during the response headers parsing
     */
    public HttpCookie getCookie(String name) throws IOException;

    /**
     * @return A list with all the response cookies
     * @throws IOException In case some problems occurs during the response headers parsing
     */
    public List<HttpCookie> getCookies() throws IOException;

    /**
     * Sets this extension response.<br>
     * This method is not intend to be invoked outside the RESTy library.<br>
     * @param response The response to set
     */
    public void setResponse(Response response);
}
