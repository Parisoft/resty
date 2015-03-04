package com.github.parisoft.resty.request.http;

import java.io.IOException;

import com.github.parisoft.resty.Client;

public class PutRequest extends HttpRequestByEntity {

    PutRequest(Client client) throws IOException {
        super(client);
    }

    @Override
    public String getMethod() {
        return "PUT";
    }
}
