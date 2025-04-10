package com.cece.pst.controller;

import com.cece.pst.dto.Car;
import com.cece.pst.dto.Cars;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class CarAPI {

    private final List<Car> cars;

    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy,dd,MM");
    private static final Comparator<Car> DATE_COMPARATOR = Comparator.comparing(Car::getReleaseDate);
    private static final Comparator<Car> USD_COMPARATOR = Comparator.comparingDouble(Car::getPriceUSD);
    private static final Comparator<Car> EUR_COMPARATOR = Comparator.comparingDouble(Car::getPriceEUR);
    private static final Comparator<Car> JPY_COMPARATOR = Comparator.comparingDouble(Car::getPriceJPY);
    private static final Comparator<Car> GBP_COMPARATOR = Comparator.comparingDouble(Car::getPriceGBP);

    @GetMapping(value = "json", produces = "application/json")
    public List<Car> getCarsJson(@RequestParam Map<String, String> params) {
        return filterAndSortCars(params);
    }

    @GetMapping(value = "xml", produces = "application/xml")
    public Cars getCarsXml(@RequestParam Map<String, String> params) {
        final List<Car> filteredSortedCars = filterAndSortCars(params);
        final Cars result = new Cars();
        result.setCars(filteredSortedCars);
        return result;
    }

    private List<Car> filterAndSortCars(Map<String, String> params) {

        params.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        Stream<Car> stream = cars.stream();

        if(params.containsKey("brand")) {
            stream = stream.filter(car -> car.getBrand().equalsIgnoreCase(params.get("brand")));
        }

        if(params.containsKey("bodyType")) {
            stream = stream.filter(car -> car.getType().equalsIgnoreCase(params.get("bodyType")));
        }

        stream = filterDate(stream, params);
        stream = filterPrice(stream, params);
        stream = sort(stream, params);

        return stream.toList();
    }

    private Stream<Car> filterDate(Stream<Car> stream, final Map<String, String> params) {
        if(params.containsKey("dateMin")) {
            LocalDate dateMin = LocalDate.parse(params.get("dateMin"), pattern);
            stream = stream.filter(car -> car.getReleaseDate().isAfter(dateMin.plusDays(-1)));
        }

        if(params.containsKey("dateMax")) {
            LocalDate dateMax = LocalDate.parse(params.get("dateMax"), pattern);
            stream = stream.filter(car -> car.getReleaseDate().isBefore(dateMax.plusDays(1)));
        }
        return stream;
    }

    private static Stream<Car> filterPrice(Stream<Car> stream, final Map<String, String> params) {
        final String currency = params.get("currency");
        if(params.containsKey("priceMin")) {
            double priceMin = Double.parseDouble(params.get("priceMin"));
            switch (currency) {
                case "USD" -> stream = stream.filter(car -> car.getPriceUSD() >= priceMin);
                case "EUR" -> stream = stream.filter(car -> car.getPriceEUR() >= priceMin);
                case "JPY" -> stream = stream.filter(car -> car.getPriceJPY() >= priceMin);
                case "GBP" -> stream = stream.filter(car -> car.getPriceGBP() >= priceMin);
            }
        }

        if(params.containsKey("priceMax")) {
            double priceMax = Double.parseDouble(params.get("priceMax"));
            switch (currency) {
                case "USD" -> stream = stream.filter(car -> car.getPriceUSD() <= priceMax);
                case "EUR" -> stream = stream.filter(car -> car.getPriceEUR() <= priceMax);
                case "JPY" -> stream = stream.filter(car -> car.getPriceJPY() <= priceMax);
                case "GBP" -> stream = stream.filter(car -> car.getPriceGBP() <= priceMax);
            }
        }
        return stream;
    }

    private static Stream<Car> sort( Stream<Car> stream, final Map<String, String> params) {
        if(params.containsKey("sort")) {
            final String currency = params.get("currency");
            switch (params.get("sort")) {
                case "dateLatest" -> stream = stream.sorted(DATE_COMPARATOR.reversed());
                case "dateOldest" -> stream = stream.sorted(DATE_COMPARATOR);
                case "priceLow" -> {
                    switch (currency) {
                        case "USD" -> stream = stream.sorted(USD_COMPARATOR);
                        case "EUR" -> stream = stream.sorted(EUR_COMPARATOR);
                        case "JPY" -> stream = stream.sorted(JPY_COMPARATOR);
                        case "GBP" -> stream = stream.sorted(GBP_COMPARATOR);
                    }
                }
                case "priceHigh" -> {
                    switch (currency) {
                        case "USD" -> stream = stream.sorted(USD_COMPARATOR.reversed());
                        case "EUR" -> stream = stream.sorted(EUR_COMPARATOR.reversed());
                        case "JPY" -> stream = stream.sorted(JPY_COMPARATOR.reversed());
                        case "GBP" -> stream = stream.sorted(GBP_COMPARATOR.reversed());
                    }
                }
            }
        }
        return stream;
    }

}
