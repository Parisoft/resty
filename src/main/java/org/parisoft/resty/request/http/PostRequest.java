package org.parisoft.resty.request.http;

import java.io.IOException;

import org.parisoft.resty.Client;

public class PostRequest extends HttpRequestByEntity {

    PostRequest(Client client) throws IOException {
        super(client);
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
