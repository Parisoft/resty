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

/**
 * The entry point to configure an HTTP request.<br>
 * <br>
 * Example of usage:<br>
 * <br>
 * Given the /meta github service wich a request to https://api.github.com/meta brings a JSON like this<br>
 * <pre>
 * {
 *   "verifiable_password_authentication": true,
 *   "github_services_sha": "f39e3c6fa71f1c5c0e8497c28a5e7e1a4501035f",
 *   "hooks": [
 *     "192.30.252.0/22"
 *   ],
 *   "git": [
 *     "192.30.252.0/22"
 *   ],
 *   "importer": [
 *     "54.221.4.64",
 *     "54.205.129.240",
 *     "54.227.175.200"
 *   ]
 * }
 * </pre>
 * And a class to represent the JSON
 * <pre>
 * {@literal @JsonIgnoreProperties}(ignoreUnknown=true)
 * public class Meta {
 *
 *     {@literal @JsonProperty}("verifiable_password_authentication")
 *     private boolean verifiable;
 *
 *     private List<String> hooks;
 *
 *     public boolean isVerifiable() {
 *         return verifiable;
 *     }
 *
 *     public void setVerifiable(boolean verifiable) {
 *         this.verifiable = verifiable;
 *     }
 *
 *     public List<String> getHooks() {
 *         return hooks;
 *     }
 *
 *     public void setHooks(List<String> hooks) {
 *         this.hooks = hooks;
 *     }
 * }
 * </pre>
 * This code consumes the /meta service and writes the result into a Meta object
 * <pre>
 * Meta meta = RESTy.request("https://api.github.com")
 *               .path("meta")
 *               .accept(ContentType.<i>APPLICATION_JSON</i>)
 *               .client()
 *               .get(Meta.class);
 *
 * System.out.println("Verifiable = " + meta.isVerifiable());
 * System.out.println("Hooks      = " + meta.getHooks());
 * </pre>
 * @author Andre Paris
 *
 */
public class RESTy {

    static {
        Logger.getLogger(ResponseProcessCookies.class.getName()).setLevel(Level.OFF);
    }

    /**
     * Creates a {@link Request} from a URI string.<br>
     * The URI must conform the <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a> with the <i>path</i> and <i>query</i> properly encoded.<br>
     * <br>
     * For your convenience, call this method with the base address of your request - like {@literal <scheme>://<authority>} -
     * and use {@link Request#path(String...)} and {@link Request#query(String, String)} to configure the URI path and query respectively without worring about escape.<br>
     * <br>
     * As example, you can do<br>
     * <pre>
     * Request request = RESTy.request("http://some.domain.org:1234")
     *                       .path("any unescaped path")
     *                       .query("don't", "scape too");
     * </pre>
     * or
     * <pre>
     * Request request = RESTy.request(http://some.domain.org:1234/any%20unescaped%20path?don%27t=scape+too);
     * </pre>
     *
     * @param uri The string to be parsed into a URI
     * @throws IllegalArgumentException If the URI string is null or violates RFC 2396
     */
    public static Request request(String uri) {
        return new Request(uri);
    }

    /**
     * Creates a {@link Request} from a {@link URI}.<br>
     * The URI must conform the <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a> with the <i>path</i> and <i>query</i> properly encoded.<br>
     * <br>
     * For your convenience, call this constructor with the base address of your request - like {@literal <scheme>://<authority>} -
     * and use {@link Request#path(String...)} and {@link Request#query(String, String)} to configure the URI path and query respectively without worring about escape.<br>
     * <br>
     * As example, you can do<br>
     * <pre>
     * URI partialUri = new URI("http://some.domain.org:1234");
     * Request request = RESTy.request(partialUri)
     *                       .path("any unescaped path")
     *                       .query("don't", "scape too");
     * </pre>
     * or
     * <pre>
     * URI fullUri = new URI("http://some.domain.org:1234/any%20unescaped%20path?don%27t=scape+too");
     * Request request = RESTy.request(fullUri);
     * </pre>
     *
     * @param uri A {@link URI} as defined by <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a>
     * @throws IllegalArgumentException If the URI is null
     */
    public static Request request(URI uri) {
        return new Request(uri);
    }
}
