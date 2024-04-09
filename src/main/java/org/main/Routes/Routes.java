package org.main.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManagerFactory;
import org.main.config.HibernateConfig;
import org.main.dao.EventDAO;
import org.main.dao.RegistrationDAO;
import org.main.dao.UserDAO;
import org.main.handlers.EventHandler;
import org.main.handlers.RegistrationHandler;
import org.main.handlers.SecurityHandler;
import org.main.handlers.UserHandler;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {

    private static SecurityHandler securityHandler = new SecurityHandler();



    public static EndpointGroup getEventRoutes(EntityManagerFactory emf) {

        EventDAO eventDAO = new EventDAO(emf);
        EventHandler eventHandler = new EventHandler(eventDAO);
        return () -> {
            path("events", () -> {
                get("/", eventHandler.getAll());
                get("/{id}", eventHandler.getById());
                post("/", eventHandler.create(), Role.ANYONE);
                put("/{id}", eventHandler.update());
                delete("/{id}", eventHandler.delete());

            });
        };
    }

    public static EndpointGroup getUserRoutes(EntityManagerFactory emf){

        UserDAO userDAO = new UserDAO(emf);
        UserHandler userHandler = new UserHandler(userDAO);
        return () -> {
            path("users", () -> {
                get(userHandler.getAllUsers());

                post("/user",userHandler.create());

                path("/user/{email}", () -> {
                    get(userHandler.getByEmail());

                    put(userHandler.update());

                    delete(userHandler.delete());
                });
            });
        };
    }


    public static EndpointGroup getRegistrationRoutes(EntityManagerFactory emf) {

        RegistrationDAO registrationDAO = new RegistrationDAO(emf);
        EventDAO eventDAO = new EventDAO(emf);
        UserDAO userDAO = new UserDAO(emf); // Add this line
        return () -> {
            path("registrations", () -> {
                before(securityHandler.authenticate());
                get(RegistrationHandler.readAll(registrationDAO), Role.USER);

                get("/id/{id}",RegistrationHandler.getRegistrationsByEventId(registrationDAO), Role.USER);

                post("/{id}", RegistrationHandler.registerUserForEvent(registrationDAO, eventDAO, userDAO), Role.USER); // Update this line
                delete("/{id}", RegistrationHandler.cancelUserRegistration(registrationDAO), Role.USER);

            });
        };
    }

    public static EndpointGroup getSecurityRoutes() {
        return ()->{
            path("/auth", ()->{
                post("/login", securityHandler.login(),Role.ANYONE);
                post("/register", securityHandler.register(),Role.ANYONE);
                before(securityHandler.authenticate());
                post("/reset-password", securityHandler.resetPassword(), Role.USER, Role.ADMIN);
                post("/logout", securityHandler.logout(), Role.USER, Role.ADMIN);
            });
        };
    }

    public enum Role implements RouteRole { ANYONE, USER, ADMIN }

}
