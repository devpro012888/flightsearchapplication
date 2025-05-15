package com.flightsearch.flight_app.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class XmlUserDetailsService implements UserDetailsService {
    // Use the absolute path to ensure the file is found in both dev and prod
    private static final String USERS_XML_PATH = "flight-app/users.xml";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            File xmlFile = new File(USERS_XML_PATH);
            if (!xmlFile.exists()) {
                throw new UsernameNotFoundException("User not found");
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList userList = doc.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String xmlUsername = userElement.getElementsByTagName("username").item(0).getTextContent();
                String xmlPassword = userElement.getElementsByTagName("password").item(0).getTextContent();
                if (xmlUsername.equals(username)) {
                    // Return the user with the hashed password (Spring Security will check the hash)
                    return User.withUsername(xmlUsername)
                            .password(xmlPassword)
                            .roles("USER")
                            .build();
                }
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
