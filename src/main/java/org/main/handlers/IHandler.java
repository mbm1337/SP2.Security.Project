package org.main.handlers;


import io.javalin.http.Handler;

public interface IHandler {
    public Handler getAll();
    public Handler getById();
    public Handler create();
    public Handler delete();
    public Handler update();

}
