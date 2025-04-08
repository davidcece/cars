package com.cece.pst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    private String brand;
    private String model;
    private String type;
    private LocalDate releaseDate;
    private double priceUSD;
    private double priceEUR;
    private double priceGBP;
    private double priceJPY;
}