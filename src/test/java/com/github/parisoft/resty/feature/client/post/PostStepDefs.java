package com.github.parisoft.resty.feature.client.post;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.server.LocalServer;
import com.github.parisoft.resty.server.domain.Car;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PostStepDefs {

    private Response actualResponse;
    private Car actualCar;
    private List<Car> actualCarList;

    private Object entity;

    @Before
    public void before() {
        LocalServer.start();
    }

    @Given("^an entity as a \"(.*?)\" and a \"(.*?)\" list$")
    public void an_entity_as_a_and_a_list(String carName1, String carName2) throws Throwable {
        final Car car1 = new Car();
        car1.setName(carName1);

        final Car car2 = new Car();
        car2.setName(carName2);

        entity = Arrays.asList(car1, car2);
    }

    @Given("^an entity as a \"(.*?)\" instance$")
    public void an_entity_as_a_instance(String carName) throws Throwable {
        final Car car = new Car();
        car.setName(carName);

        entity = car;
    }

    @When("^do a POST request to \"(.*?)\" for a Response instance from json$")
    public void do_a_GET_request_to_for_a_Response_instance_from_json(String path) throws Throwable {
        actualResponse = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_JSON)
                .type(ContentType.APPLICATION_JSON)
                .path(path)
                .client()
                .post(entity);
    }

    @When("^do a POST request to \"(.*?)\" for a list of Car instances from json$")
    public void do_a_GET_request_to_for_a_list_of_Car_instances_from_json(String path) throws Throwable {
        actualCarList = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_JSON)
                .type(ContentType.APPLICATION_JSON)
                .path(path)
                .client()
                .post(entity, new TypeReference<List<Car>>() {});
    }

    @When("^do a POST request to \"(.*?)\" for a Car instance from json$")
    public void do_a_GET_request_to_for_a_Car_instance_from_json(String path) throws Throwable {
        actualCar = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_JSON)
                .type(ContentType.APPLICATION_JSON)
                .path(path)
                .client()
                .post(entity, Car.class);
    }

    @When("^do a POST request to \"(.*?)\" for a Response instance from xml$")
    public void do_a_GET_request_to_for_a_Response_instance_from_xml(String path) throws Throwable {
        actualResponse = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_XML)
                .type(ContentType.APPLICATION_XML)
                .path(path)
                .client()
                .post(entity);
    }

    @When("^do a POST request to \"(.*?)\" for a list of Car instances from xml$")
    public void do_a_GET_request_to_for_a_list_of_Car_instances_from_xml(String path) throws Throwable {
        actualCarList = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_XML)
                .type(ContentType.APPLICATION_XML)
                .path(path)
                .client()
                .post(entity, new TypeReference<List<Car>>() {});
    }

    @When("^do a POST request to \"(.*?)\" for a Car instance from xml$")
    public void do_a_GET_request_to_for_a_Car_instance_from_xml(String path) throws Throwable {
        actualCar = RESTy.request(LocalServer.getHost())
                .accept(ContentType.APPLICATION_XML)
                .type(ContentType.APPLICATION_XML)
                .path(path)
                .client()
                .post(entity, Car.class);
    }

    @Then("^the status code is (\\d+)$")
    public void the_status_code_is(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, actualResponse.getStatusCode());
    }

    @Then("^the content type is \"(.*?)\"$")
    public void the_content_type_is(String expectedContentType) throws Throwable {
        assertEquals(expectedContentType, actualResponse.getContentType().getMimeType());
    }

    @Then("^the car list size is (\\d+)$")
    public void the_car_list_size_is(int expectedSize) throws Throwable {
        assertEquals(expectedSize, actualCarList.size());
    }

    @Then("^the (\\d+)st car is a \"(.*?)\"$")
    public void the_st_car_is_a(int index, String carname) throws Throwable {
        actualCar = actualCarList.get(index - 1);
        the_car_is_a(carname);
    }

    @Then("^the (\\d+)nd car is a \"(.*?)\"$")
    public void the_nd_car_is_a(int index, String carname) throws Throwable {
        actualCar = actualCarList.get(index - 1);
        the_car_is_a(carname);
    }

    @Then("^the car is a \"(.*?)\"$")
    public void the_car_is_a(String expectedCarName) throws Throwable {
        assertEquals(expectedCarName, actualCar.getName());
    }
}
