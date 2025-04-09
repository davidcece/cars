package com.cece.pst.controller;

import com.cece.pst.dto.Car;
import com.cece.pst.dto.Cars;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class CarAPI {

    private final List<Car> cars;

    @GetMapping(value = "json", produces = "application/json")
    public List<Car> getCarsJson() {
        return filterAndSortCars();
    }

    @GetMapping(value = "xml", produces = "application/xml")
    public Cars getCarsXml() {
        final List<Car> filteredSortedCars = filterAndSortCars();
        final Cars result = new Cars();
        result.setCars(filteredSortedCars);
        return result;
    }

    private List<Car> filterAndSortCars() {
        
        final Comparator<Car> priceGBPComparator = Comparator.comparingDouble(Car::getPriceGBP);

        return cars.stream()
                .filter(car -> car.getReleaseDate().getYear() > 2000)
                .sorted(priceGBPComparator)
                .toList();
    }

}
