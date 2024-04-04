package org.main.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import org.main.dao.UserDAO;
import org.main.exception.ApiException;
import org.main.ressources.User;

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
    public Handler getByEmail(){
        return ctx -> {
            String email  = (ctx.pathParam("email"));
            User user = userDao.getById(email);
            if (user == null) {
                throw new ApiException( 404, "User not found");
            }


            ctx.status(200).json(email);
        };
    }

    public Handler create() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            user = userDao.createUser(user.getName(), user.getEmail(), user.getPhone(), user.getPassword());


            ctx.status(201).json(user);
        };
    }


    public Handler delete() {
        return ctx -> {
            String email =ctx.pathParam("email");
            User user = userDao.getById(email);
            if (user == null) {
                throw new ApiException(404, "User not found");
            }
            userDao.delete(user);
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





