package com.github.parisoft.resty.request.http;

import java.io.IOException;

import com.github.parisoft.resty.Client;

public class PostRequest extends HttpRequestByEntity {

    PostRequest(Client client) throws IOException {
        super(client);
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
