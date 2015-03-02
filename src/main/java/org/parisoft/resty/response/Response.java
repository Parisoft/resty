package org.parisoft.resty.response;

import org.apache.http.HttpResponse;
import org.parisoft.resty.entity.EntityReader;

public interface Response extends HttpResponse, HttpResponseExtension, EntityReader {

}
