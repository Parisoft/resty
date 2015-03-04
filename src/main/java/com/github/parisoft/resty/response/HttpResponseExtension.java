package com.github.parisoft.resty.response;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;


public interface HttpResponseExtension {

    public int getStatusCode();

    public Response assertOk();

    public Response assertStatus(int httpStatus);

    public HttpCookie getCookie(String name) throws IOException;

    public List<HttpCookie> getCookies() throws IOException;

    public void setResponse(Response response);
}
