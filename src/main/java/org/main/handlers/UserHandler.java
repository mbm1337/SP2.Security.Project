package org.main.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import org.main.Exception.ApiException;
import org.main.dao.UserDAO;
import org.main.ressources.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserHandler {



    UserDAO userDao;
    private  final ObjectMapper objectMapper = new ObjectMapper();

    public Handler getAllUsers(){
        return ctx -> {
            List<User> users = userDao.getAll();
            if (users.isEmpty()) {
                throw new ApiException(404, "users are not found ");

            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(users);
                ctx.result(json);
                ctx.status(200);
            }
        };
    }
    public Handler getById(){
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            User user = userDao.getById(id);
            if (user == null) {
                throw new ApiException( 404, "User not found");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            ctx.result(json);
            ctx.status(200);
        };
    }

    public Handler create() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);

            user = userDao.createUser(user.getName(), user.getEmail(), user.getPhone(), user.getPassword());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            ctx.result(json);
            ctx.status(201);
        };
    }


    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            User user = userDao.getById(id);
            if (user == null) {
                throw new ApiException(404, "User not found");
            }
            userDao.delete(user);
            ctx.status(204);
        };
    }

    public Handler update() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            user = userDao.update(user);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            ctx.result(json);
            ctx.status(200);
        };
    }






    }





