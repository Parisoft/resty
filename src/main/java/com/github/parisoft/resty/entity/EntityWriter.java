package com.github.parisoft.resty.entity;

import java.io.IOException;

import org.apache.http.HttpEntity;

public interface EntityWriter {

    public HttpEntity getEntity() throws IOException;
}
