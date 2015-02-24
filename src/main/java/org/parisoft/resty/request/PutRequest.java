package org.parisoft.resty.request;

import java.io.IOException;

import org.parisoft.resty.Client;

public class PutRequest extends PostRequest {

    PutRequest(Client client) throws IOException {
        super(client);
    }

    @Override
    public String getMethod() {
        return "PUT";
    }
}
