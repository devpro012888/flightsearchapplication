package com.flightsearch.flight_app.service;

import com.flightsearch.flight_app.model.Flight;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;

@Service
public class FlightService {

    private final List<Flight> flights = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${aviationstack.api.key}")
    private String apiKey;

    // Log the injected API key to verify property loading
    @PostConstruct
    public void logApiKey() {
        System.out.println("Injected API Key: " + apiKey);
    }

    // Initialize with sample data
    public FlightService() {
        flights.add(new Flight(1, "New York", "Los Angeles", "2025-04-15", "2025-04-20", 2, 300));
        flights.add(new Flight(2, "Chicago", "Miami", "2025-04-18", "2025-04-25", 1, 200));
        flights.add(new Flight(3, "San Francisco", "Seattle", "2025-04-10", "2025-04-15", 3, 150));
    }

    // Search for flights based on criteria
    public List<Flight> searchFlights(String departure, String destination, String departureDate, String returnDate, int passengers) {
        return flights.stream()
                .filter(flight -> flight.getDeparture().equalsIgnoreCase(departure) &&
                                  flight.getDestination().equalsIgnoreCase(destination) &&
                                  flight.getDepartureDate().equals(departureDate) &&
                                  flight.getReturnDate().equals(returnDate) &&
                                  flight.getPassengers() >= passengers)
                .collect(Collectors.toList());
    }

    public List<Flight> fetchFlightsFromAviationStack(String departure, String destination) {
        String url = String.format("http://api.aviationstack.com/v1/flights?access_key=%s&dep_iata=%s&arr_iata=%s", apiKey, departure, destination);
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data");

            List<Flight> flights = new ArrayList<>();
            for (JsonNode flightNode : dataNode) {
                Flight flight = new Flight();
                flight.setDeparture(flightNode.path("departure").path("iata").asText());
                flight.setDestination(flightNode.path("arrival").path("iata").asText());
                flight.setDepartureDate(flightNode.path("departure").path("scheduled").asText());
                flight.setReturnDate(flightNode.path("arrival").path("scheduled").asText());
                flights.add(flight);
            }
            return flights;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Flight> fetchCurrentFlights() {
        String url = "https://api.aviationstack.com/v1/flights?access_key=" + apiKey;
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Raw API Response: " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data");

            List<Flight> flights = new ArrayList<>();
            for (JsonNode flightNode : dataNode) {
                Flight flight = new Flight();
                flight.setDeparture(flightNode.path("departure").path("airport").asText());
                flight.setDestination(flightNode.path("arrival").path("airport").asText());
                flight.setDepartureDate(flightNode.path("departure").path("scheduled").asText());
                flight.setReturnDate(flightNode.path("arrival").path("scheduled").asText());
                flight.setPassengers(0); // Placeholder, as passenger data is not available in the API
                flight.setPrice(0); // Placeholder, as price data is not available in the API
                flights.add(flight);
            }

            return flights;
        } catch (Exception e) {
            System.err.println("Error fetching flight data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            File xmlFile = new File("users.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList users = doc.getElementsByTagName("user");

            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String storedUsername = user.getElementsByTagName("username").item(0).getTextContent();
                String storedPassword = user.getElementsByTagName("password").item(0).getTextContent();

                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        try {
            File xmlFile = new File("users.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            if (xmlFile.exists()) {
                doc = builder.parse(xmlFile);
            } else {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("users");
                doc.appendChild(rootElement);
            }

            NodeList users = doc.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String storedUsername = user.getElementsByTagName("username").item(0).getTextContent();
                if (storedUsername.equals(username)) {
                    return false; // Username already exists
                }
            }

            Element newUser = doc.createElement("user");
            Element newUsername = doc.createElement("username");
            newUsername.setTextContent(username);
            Element newPassword = doc.createElement("password");
            newPassword.setTextContent(password);

            newUser.appendChild(newUsername);
            newUser.appendChild(newPassword);
            doc.getDocumentElement().appendChild(newUser);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}