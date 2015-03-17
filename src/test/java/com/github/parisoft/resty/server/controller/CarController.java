package com.github.parisoft.resty.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.parisoft.resty.util.FileUtils;

@RestController
public class CarController {

    private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + "; charset=utf-8";

    @RequestMapping(method=GET, value="/car", produces=APPLICATION_JSON)
    public String listCars(@RequestParam(value="count", defaultValue="10") int count) throws Exception {
        if (count < 1) {
            return "[]";
        } else if (count > 1) {
            return String.format("[%s, %s]", FileUtils.read("/json/caprice.json"), FileUtils.read("/json/dart.json"));
        } else {
            return String.format("[%s]", FileUtils.read("/json/caprice.json"));
        }
    }

    @RequestMapping(method=GET, value="/car/{name}", produces=APPLICATION_JSON)
    public String searchCar(@PathVariable("name") String name) throws Exception {
        return FileUtils.read("/json/" + name);
    }
}
