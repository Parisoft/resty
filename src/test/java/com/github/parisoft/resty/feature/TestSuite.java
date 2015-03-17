package com.github.parisoft.resty.feature;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.parisoft.resty.feature.client.get.GetFeature;
import com.github.parisoft.resty.feature.request.path.PathFeature;
import com.github.parisoft.resty.feature.request.uri.UriFeature;

@RunWith(Suite.class)
@SuiteClasses({ GetFeature.class, PathFeature.class, UriFeature.class })
public class TestSuite {

}
