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
