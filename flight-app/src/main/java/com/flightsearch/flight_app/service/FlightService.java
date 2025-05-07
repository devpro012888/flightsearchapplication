package com.flightsearch.flight_app.service;

import com.flightsearch.flight_app.model.Flight;
import org.springframework.stereotype.Service;
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

@Service
public class FlightService {

    private final List<Flight> flights = new ArrayList<>();

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