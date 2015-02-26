package org.parisoft.resty.request;

import org.parisoft.resty.Client;

public class GetRequest extends Request {

    GetRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}