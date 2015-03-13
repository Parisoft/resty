package com.github.parisoft.resty.response;

import org.apache.http.HttpResponse;

import com.github.parisoft.resty.entity.EntityReader;
import com.github.parisoft.resty.response.http.HttpResponseExtension;

/**
 * {@inheritDoc HttpResponse}
 * @author apoliveira
 *
 */
public interface Response extends HttpResponse, HttpResponseExtension, EntityReader {

}
