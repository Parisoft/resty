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
package com.github.parisoft.resty.utils;

import static com.github.parisoft.resty.utils.ArrayUtils.isEmpty;

import java.net.HttpCookie;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Cookie;

public class CookieUtils {

    public static String toString(Cookie... cookies) {
        if (isEmpty(cookies)) {
            return null;
        }

        final HttpCookie[] httpCookies = new HttpCookie[cookies.length];

        for (int i = 0; i < cookies.length; i++) {
            final Cookie srcCookie = cookies[i];
            final HttpCookie httpCookie = new HttpCookie(srcCookie.getName(), srcCookie.getValue());
            httpCookie.setDomain(srcCookie.getDomain());
            httpCookie.setPath(srcCookie.getPath());
            httpCookie.setVersion(srcCookie.getVersion());

            httpCookies[i] = httpCookie;
        }

        return toString(httpCookies);
    }

    public static String toString(org.apache.http.cookie.Cookie... cookies) {
        if (isEmpty(cookies)) {
            return null;
        }

        final HttpCookie[] httpCookies = new HttpCookie[cookies.length];

        for (int i = 0; i < cookies.length; i++) {
            final org.apache.http.cookie.Cookie srcCookie = cookies[i];
            final HttpCookie httpCookie = new HttpCookie(srcCookie.getName(), srcCookie.getValue());
            httpCookie.setComment(srcCookie.getComment());
            httpCookie.setDomain(srcCookie.getDomain());
            httpCookie.setPath(srcCookie.getPath());
            httpCookie.setSecure(srcCookie.isSecure());
            httpCookie.setVersion(srcCookie.getVersion());

            final Date now = new Date();

            if (srcCookie.isExpired(now)) {
                httpCookie.setMaxAge(0);
            } else {
                httpCookie.setMaxAge(TimeUnit.MILLISECONDS.toSeconds(srcCookie.getExpiryDate().getTime() - now.getTime()));
            }

            httpCookies[i] = httpCookie;
        }

        return toString(httpCookies);
    }

    public static String toString(HttpCookie... cookies) {
        if (isEmpty(cookies)) {
            return null;
        }

        final StringBuilder cookieBuilder = new StringBuilder();

        for (HttpCookie cookie : cookies) {
            cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue());

            if (cookie.getComment() != null) {
                cookieBuilder.append(";").append("Comment=").append(cookie.getComment());
            }

            if (cookie.getDomain() != null) {
                cookieBuilder.append(";").append("Domain=").append(cookie.getDomain());
            }

            if (cookie.getPath() != null) {
                cookieBuilder.append(";").append("Path=").append(cookie.getPath());
            }

            if (cookie.getSecure()) {
                cookieBuilder.append(";").append("Secure");
            }

            if (cookie.isHttpOnly()) {
                cookieBuilder.append(";").append("HttpOnly");
            }

            if (cookie.getVersion() > 0) {
                cookieBuilder.append(";").append("Version=").append(cookie.getVersion());
            }

            if (cookie.hasExpired()) {
                cookieBuilder.append(";").append("Max-Age=0");
            } else {
                cookieBuilder.append(";").append("Max-Age=").append(cookie.getMaxAge());
            }

            cookieBuilder.append(", ");
        }

        return cookieBuilder
                .deleteCharAt(cookieBuilder.length() - 1)
                .toString();
    }
}
