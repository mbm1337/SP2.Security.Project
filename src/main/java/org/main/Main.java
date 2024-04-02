package org.main;

import io.javalin.apibuilder.EndpointGroup;
import org.main.ressources.Role;
import org.main.ressources.User;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        User user = new User("ahmad","sdw");
        Role role = new Role("student ");
        user.addRole(role);
        System.out.println(user.toString());
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
        return () -> {
            path("users", () -> {
                get("/", ctx ->{});
                get("/:id", ctx ->{});
                post("/", ctx ->{});
                put("/:id", ctx ->{});
                delete("/:id", ctx ->{});

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