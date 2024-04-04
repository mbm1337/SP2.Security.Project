package org.main;

import io.javalin.Javalin;
import org.main.Routes.Routes;
import org.main.handlers.UserHandler;
import io.javalin.apibuilder.EndpointGroup;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7008);

        Routes routes = new Routes();

        app.routes(routes.getSecurityRoutes());
        app.routes(routes.getSecuredRoutes());

        // Handle 404 Not Found for missing resources and routes
        app.error(404, ctx -> {
            ctx.json(Map.of("message", "Resource not found"));
        });

        //for incorrect JSON representations
        app.exception(IllegalStateException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of("message", e.getMessage())); // 400 Bad Request indicates a client error
        });
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