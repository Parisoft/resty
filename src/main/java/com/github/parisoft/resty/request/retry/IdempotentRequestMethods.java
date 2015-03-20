package com.github.parisoft.resty.request.retry;

import java.util.TreeSet;

public class IdempotentRequestMethods extends TreeSet<String> {

    private static final long serialVersionUID = -5207417261749846206L;
    private static final IdempotentRequestMethods instance = new IdempotentRequestMethods();

    private IdempotentRequestMethods() {
        super();
        add("GET");
        add("HEAD");
        add("TRACE");
        add("OPTIONS");
    }

    public static IdempotentRequestMethods getInstance() {
        return instance;
    }
}
