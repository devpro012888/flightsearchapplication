package com.flightsearch.flight_app.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;

@Service
public class UserService {
    private static final String USERS_XML_PATH = "src/main/resources/users.xml";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(String username, String password) {
        try {
            File xmlFile = new File(USERS_XML_PATH);
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

            // Check if username already exists
            NodeList users = doc.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String storedUsername = user.getElementsByTagName("username").item(0).getTextContent();
                if (storedUsername.equals(username)) {
                    return false; // Username already exists
                }
            }

            Element root = doc.getDocumentElement();
            Element user = doc.createElement("user");

            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(username));
            user.appendChild(usernameElement);

            // Encode the password before storing
            String encodedPassword = passwordEncoder.encode(password);
            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(encodedPassword));
            user.appendChild(passwordElement);

            root.appendChild(user);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
