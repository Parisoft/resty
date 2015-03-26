package com.github.parisoft.resty.feature.response.primitive;

import static org.junit.Assert.assertEquals;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.server.LocalServer;
import com.github.parisoft.resty.utils.ReflectionUtils;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PrimitiveStepDefs {

    private Response actualResponse;

    @Before
    public void before() {
        LocalServer.start();
    }

    @When("^do a GET request to primitive with value \"(.*?)\"$")
    public void do_a_GET_request_to_primitive_with_value(String value) throws Throwable {
        actualResponse = RESTy.request(LocalServer.getHost())
                .path("primitive")
                .query("value", value)
                .client()
                .get();
    }

    @Then("^the response entity is converted to a \"(.*?)\" with value \"(.*?)\"$")
    public void the_response_entity_is_converted_to_a_with_value(String expectedClassName, String expectedEntityValueAsString) throws Throwable {
        final Class<?> expectedClass = Class.forName(expectedClassName);
        final Object expectedEntityValue = String.class.equals(expectedClass)
                ? expectedEntityValueAsString
                        : ReflectionUtils.getMethod(expectedClass, "valueOf", String.class).invoke(null, expectedEntityValueAsString);
        final Object actualEntityValue = actualResponse.getEntityAs(expectedClass);

        assertEquals(expectedEntityValue, actualEntityValue);
    }
}
