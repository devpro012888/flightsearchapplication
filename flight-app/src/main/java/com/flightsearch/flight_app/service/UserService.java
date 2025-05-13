package com.flightsearch.flight_app.service;

import org.springframework.stereotype.Service;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

@Service
public class UserService {

    private static final String USERS_XML_PATH = "src/main/resources/users.xml";

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

            Element root = doc.getDocumentElement();
            Element user = doc.createElement("user");

            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(username));
            user.appendChild(usernameElement);

            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(password));
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
