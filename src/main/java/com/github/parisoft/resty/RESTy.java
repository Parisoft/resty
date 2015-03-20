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
package com.github.parisoft.resty;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.protocol.ResponseProcessCookies;

import com.github.parisoft.resty.request.Request;

public class RESTy {

    static {
        Logger.getLogger(ResponseProcessCookies.class.getName()).setLevel(Level.OFF);
    }

    public static Request request(String uri) {
        return new Request(uri);
    }

    public static Request request(URI uri) {
        return new Request(uri);
    }
}
