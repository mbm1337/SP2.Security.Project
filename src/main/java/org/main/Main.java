package org.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.main.HibernateConfig.ApplicationConfig;
import org.main.Routes.Routes;
import org.main.handlers.UserHandler;
import io.javalin.apibuilder.EndpointGroup;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.main.Routes.Routes.getSecuredRoutes;
import static org.main.Routes.Routes.getSecurityRoutes;

public class Main {
    public static void main(String[] args) {
        startServer(7070);
    }

    public static void startServer(int port) {
        ObjectMapper om = new ObjectMapper();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .setRoute(getSecurityRoutes())
                .setRoute(getSecuredRoutes())
                .checkSecurityRoles();
    }
}
