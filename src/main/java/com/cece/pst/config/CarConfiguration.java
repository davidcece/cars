package com.cece.pst.config;

import com.cece.pst.dto.Brand;
import com.cece.pst.dto.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class CarConfiguration {

    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final String CARS_XML = "carsType.xml";
    private static final String BRANDS_JSON = "CarsBrand.csv";


    @Bean
    public List<Car> cars() {

        final List<Car> result = new ArrayList<>();
        final List<Brand> carBrands = brands();

        try {
            final Document document = getCarsXmlDocument();
            final NodeList carNodes = document.getElementsByTagName("car");
            for (int i = 0; i < carNodes.getLength(); i++) {
                final Node carNode = carNodes.item(i);

                if (carNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element carElement = (Element) carNode;

                    final String type = carElement.getElementsByTagName("type").item(0).getTextContent();
                    final String model = carElement.getElementsByTagName("model").item(0).getTextContent();

                    final NodeList prices = carElement.getElementsByTagName("price");
                    final String priceUSD = prices.item(0).getTextContent();
                    final String priceEUR = prices.item(1).getTextContent();
                    final String priceGBP = prices.item(2).getTextContent();
                    final String priceJPY = prices.item(3).getTextContent();

                    final Car car = Car.builder()
                            .brand(carBrands.get(i).getName())
                            .releaseDate(carBrands.get(i).getReleaseDate())
                            .type(type)
                            .model(model)
                            .priceUSD(Double.parseDouble(priceUSD))
                            .priceEUR(Double.parseDouble(priceEUR))
                            .priceGBP(Double.parseDouble(priceGBP))
                            .priceJPY(Double.parseDouble(priceJPY))
                            .build();
                    result.add(car);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return Collections.unmodifiableList(result);

    }

    private Document getCarsXmlDocument() {
        try {
            final File carsXmlFile = new ClassPathResource(CARS_XML).getFile();
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(carsXmlFile);
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public List<Brand> brands() {

        final List<Brand> brands = new ArrayList<>();
        try {
            final ClassPathResource resource = new ClassPathResource(BRANDS_JSON);
            final List<String> lines = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                final String line = lines.get(i).replaceAll("\"", ""); //Remove enclosing quotes
                final String[] fields = line.split(",");
                brands.add(
                        Brand.builder()
                                .name(fields[0])
                                .releaseDate(LocalDate.parse(fields[1], pattern))
                                .build()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.unmodifiableList(brands);
    }


}
