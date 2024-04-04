package org.main;

import jakarta.persistence.EntityManagerFactory;
import org.main.config.HibernateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.main.config.ApplicationConfig;
import static org.main.routes.Routes.getUserRoutes;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

        Main.startServer(7000);

    }

    public static void startServer(int port) {
        ObjectMapper om = new ObjectMapper();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .setRoute(getUserRoutes());


    }
}