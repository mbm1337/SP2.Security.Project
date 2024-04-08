package org.main;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;
import org.main.config.HibernateConfig;
import org.main.config.ApplicationConfig;
import org.main.routes.Routes;

import static org.main.routes.Routes.*;

public class Main {


    public static void main(String[] args) {
        Main.startServer(7000);
    }

    public static void startServer(int port) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .checkSecurityRoles()
                .setRoute(getSecurityRoutes())
                //.setRoute(getSecuredRoutes())
                .setRoute(getUserRoutes(emf))
                .setRoute(getEventRoutes(emf))
                .setRoute(getRegistrationRoutes(emf));

    }
}
