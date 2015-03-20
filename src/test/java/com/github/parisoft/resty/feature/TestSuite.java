package com.github.parisoft.resty.feature;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.parisoft.resty.feature.client.delete.DeleteFeature;
import com.github.parisoft.resty.feature.client.execute.patch.ExecutePatchFeature;
import com.github.parisoft.resty.feature.client.get.GetFeature;
import com.github.parisoft.resty.feature.client.post.PostFeature;
import com.github.parisoft.resty.feature.client.put.PutFeature;
import com.github.parisoft.resty.feature.client.retry.RetryFeature;
import com.github.parisoft.resty.feature.client.timeout.TimeoutFeature;
import com.github.parisoft.resty.feature.request.path.PathFeature;
import com.github.parisoft.resty.feature.request.uri.UriFeature;

@RunWith(Suite.class)
@SuiteClasses({
    GetFeature.class, DeleteFeature.class, PostFeature.class, PutFeature.class, ExecutePatchFeature.class,
    PathFeature.class, UriFeature.class,
    TimeoutFeature.class, RetryFeature.class })
public class TestSuite {

}
