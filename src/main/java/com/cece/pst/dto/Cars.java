package com.cece.pst.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@JacksonXmlRootElement(localName = "cars")
@Data
public class Cars {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "car")
    private List<Car> cars;
}
