package org.main;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.main.Routes.Routes.getSecuredRoutes;
import static org.main.Routes.Routes.getSecurityRoutes;
import static org.main.Routes.Routes.getUserRoutes;
import static org.main.Routes.Routes.getEventRoutes;

import jakarta.persistence.EntityManagerFactory;
import org.main.config.HibernateConfig;
import org.main.config.ApplicationConfig;

import static org.main.routes.Routes.*;

public class Main {


    public static void main(String[] args) {
        Main.startServer(7000);
    }

    public static void startServer(int port) {
        ObjectMapper om = new ObjectMapper();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .setRoute(getSecurityRoutes())
                .setRoute(getSecuredRoutes())
                .checkSecurityRoles()
                .setRoute(getUserRoutes(emf))
                .setRoute(getEventRoutes(emf))
                .setRoute(getRegistrationRoutes(emf));
    }
}
