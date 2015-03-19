package com.github.parisoft.resty.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.parisoft.resty.util.FileUtils;

@RestController
public class CarReadController {

    private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + "; charset=utf-8";
    private static final String APPLICATION_XML = MediaType.APPLICATION_XML + "; charset=utf-8";
    private static final List<String> CAR_LIST = Arrays.asList("caprice", "dart");

    @RequestMapping(method={GET, DELETE, HEAD, OPTIONS}, value="/car", produces=APPLICATION_JSON)
    public ResponseEntity<String> listCars() throws Exception {
        final String list = String.format("[%s, %s]", FileUtils.read("/json/caprice.json"), FileUtils.read("/json/dart.json"));

        return ResponseEntity.ok(list);
    }

    @RequestMapping(method={GET, DELETE, HEAD, OPTIONS}, value="/car", produces=APPLICATION_XML)
    public ResponseEntity<String> listCarsInXml(@RequestParam(value="count", defaultValue="10") int count) throws Exception {
        final String list = String.format("<cars>%s\n%s</cars>", FileUtils.read("/xml/caprice.xml"), FileUtils.read("/xml/dart.xml"));

        return ResponseEntity.ok(list);
    }

    @RequestMapping(method={GET, DELETE, HEAD, OPTIONS}, value="/car/{name}", produces=APPLICATION_JSON)
    public ResponseEntity<String> searchCar(@PathVariable("name") String name) throws Exception {
        if (!CAR_LIST.contains(name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        final String car = FileUtils.read("/json/" + name + ".json");

        return ResponseEntity.ok(car);
    }

    @RequestMapping(method={GET, DELETE, HEAD, OPTIONS}, value="/car/{name}", produces=APPLICATION_XML)
    public ResponseEntity<String> searchCarInXml(@PathVariable("name") String name) throws Exception {
        if (!CAR_LIST.contains(name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        final String car = FileUtils.read("/xml/" + name + ".xml");

        return ResponseEntity.ok(car);
    }
}
