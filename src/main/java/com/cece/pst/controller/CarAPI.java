package com.cece.pst.controller;

import com.cece.pst.dto.Car;
import com.cece.pst.dto.Cars;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class CarAPI {

    private final List<Car> cars;


    @GetMapping(value = "json", produces = "application/json")
    public List<Car> getCarsJson() {
        return cars;
    }

    @GetMapping(value = "xml", produces = "application/xml")
    public Cars getCarsXml() {
        Cars cars = new Cars();
        cars.setCars(this.cars);
        return cars;
    }

}
