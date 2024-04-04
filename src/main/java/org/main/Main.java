package org.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.main.ApplicationConfig.ApplicationConfig;
import org.main.HibernateConfig.HibernateConfig;
import org.main.dao.UserDAO;
import org.main.handlers.UserHandler;
import io.javalin.apibuilder.EndpointGroup;
import org.main.ressources.Event;
import org.main.ressources.Role;
import org.main.ressources.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

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


    public static EndpointGroup getEventRoutes() {
        return () -> {
            path("events", () -> {
                get("/", ctx ->{} );
                get("/:id", ctx ->{});
                post("/", ctx ->{});
                put("/:id", ctx ->{});
                delete("/:id", ctx ->{});

            });
        };
    }

    public static EndpointGroup getUserRoutes() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        var em = emf.createEntityManager();

        UserHandler userHandler = new UserHandler();
        return () -> {
            path("users", () -> {
                get(userHandler.getAllUsers());

                post("/user",userHandler.create());

                path("/user/{id}", () -> {
                    get(userHandler.getById());

                    put(userHandler.update());

                    delete(userHandler.delete());
                });
            });
        };
    }


    public static EndpointGroup getRegistrationRoutes() {
        return () -> {
            path("registrations", () -> {
                get("/:id", ctx ->{});
                post("/:id", ctx ->{});
                delete("/:id", ctx ->{});

            });
            path("registration", () -> {
                get("/:id", ctx ->{});
            });
        };
    }

    public static EndpointGroup getAuthenticationRoutes() {
        return () -> {
            path("login", () -> {
                post("/", ctx ->{});
            });
            path("logout", () -> {
                post("/", ctx ->{});
            });
            path("register", () -> {
                post("/", ctx ->{});
            });
            path("reset-password", () -> {
                post("/", ctx ->{});
            });

        };
    }

}