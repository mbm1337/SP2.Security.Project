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



    private final UserDAO userDao;
    private  final ObjectMapper objectMapper = new ObjectMapper();

    public UserHandler(UserDAO userDao) {
        this.userDao = userDao;
    }


    public Handler getAllUsers(){
        return ctx -> {
            List<User> users = userDao.getAll();
            if (users.isEmpty()) {
                throw new ApiException(404, "users are not found ");

            } else {

                ctx.status(200).json(users);
            }
        };
    }
    public Handler getbyId(){
        return ctx -> {
            int id  = Integer.parseInt((ctx.pathParam("id")));
            User user = userDao.getById(id);
            if (user == null) {
                throw new ApiException( 404, "User not found");
            }


            ctx.status(200).json(id);
        };
    }

    public Handler create() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            user = userDao.registerUser(user.getName(), user.getEmail(), user.getPhone(), user.getPassword());


            ctx.status(201).json(user);
        };
    }


    public Handler delete() {
        return ctx -> {
           int id = Integer.parseInt(ctx.pathParam("id"));
            User user = userDao.getById(id);
            if (user == null) {
                throw new ApiException(404, "User not found");
            }
            userDao.delete(id);
            ctx.status(204).json("");
        };
    }

    public Handler update() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            user = userDao.update(user);

            ctx.status(200).json(user);
        };
    }






    }





