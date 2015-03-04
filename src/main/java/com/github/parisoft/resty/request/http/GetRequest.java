package com.github.parisoft.resty.request.http;

import com.github.parisoft.resty.Client;

public class GetRequest extends HttpRequest {

    GetRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}
