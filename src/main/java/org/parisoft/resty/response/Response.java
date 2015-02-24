package org.parisoft.resty.response;

import org.apache.http.HttpResponse;
import org.parisoft.resty.response.entity.EntityReader;

public interface Response extends HttpResponse, EntityReader {

    public int getStatusCode();
}
