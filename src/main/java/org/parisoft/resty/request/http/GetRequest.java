package org.parisoft.resty.request.http;

import org.parisoft.resty.Client;

public class GetRequest extends HttpRequest {

    GetRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}
