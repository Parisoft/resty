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
package com.github.parisoft.resty.request.retry;

import java.util.concurrent.ConcurrentSkipListSet;

import com.github.parisoft.resty.client.Client;

/**
 * Class that holds the idempotent and safe methods in which a request is eligible to be retried.<br>
 * <br>
 * By default these methods are:
 * <ul>
 * <li>GET</li>
 * <li>HEAD</li>
 * <li>TRACE</li>
 * <li>OPTIONS</li>
 * </ul>
 * @author Andre Paris
 * @see RequestRetryHandler
 * @see Client#retries(int)
 */
public class IdempotentRequestMethods extends ConcurrentSkipListSet<String> {

    private static final long serialVersionUID = -5207417261749846206L;
    private static final IdempotentRequestMethods instance = new IdempotentRequestMethods();

    private IdempotentRequestMethods() {
        super();
        reset();
    }

    /**
     * @return The singleton instance of IdempotentRequestMethods
     */
    public static IdempotentRequestMethods getInstance() {
        return instance;
    }

    /**
     * Reset to the default methods:
     * <ul>
     * <li>GET</li>
     * <li>HEAD</li>
     * <li>TRACE</li>
     * <li>OPTIONS</li>
     * </ul>
     */
    public void reset() {
        clear();
        add("GET");
        add("HEAD");
        add("TRACE");
        add("OPTIONS");
    }
}
