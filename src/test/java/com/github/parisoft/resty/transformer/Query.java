package com.github.parisoft.resty.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cucumber.api.Transformer;

public class Query extends Transformer<List<NameValuePair>> {

    @Override
    public List<NameValuePair> transform(String value) {
        final List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        for (String string : value.split(",")) {
            final String[] pair = string.split("=");

            pairs.add(new BasicNameValuePair(pair[0], pair[1]));
        }

        return pairs;
    }

}
