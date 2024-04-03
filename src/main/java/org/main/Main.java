package org.main;

import org.main.handlers.UserHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
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