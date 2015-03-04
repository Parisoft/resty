package com.github.parisoft.resty.request.http;

import com.github.parisoft.resty.Client;

public class DeleteRequest extends HttpRequest {

    DeleteRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
