package org.main.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.main.dao.EventDAO;
import org.main.ressources.Event;

import java.util.List;

public class EventHandler implements IHandler {

     private EventDAO eventDAO;

     private final ObjectMapper objectMapper = new ObjectMapper();

        public EventHandler(EventDAO eventDAO){
            this.eventDAO = eventDAO;
        }

    @Override
    public Handler getAll() {
        List<Event> eventList = eventDAO.getAll();
        return ctx -> {
            String json = objectMapper.writeValueAsString(eventList);
            ctx.status(200).json(json);
        };
    }


    @Override
    public Handler getById(){
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Event event = eventDAO.getById(id);
            if (event == null) {
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            String json = objectMapper.writeValueAsString(event);
            ctx.status(200).json(json);
        };

    }

    @Override
    public Handler create() {
        return ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
            event = eventDAO.create(event);

            String json = objectMapper.writeValueAsString(event);
            ctx.status(201).json(json);
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Event event = eventDAO.getById(id);
            eventDAO.delete(event);

            String json = objectMapper.writeValueAsString(event);
            ctx.status(204).json(json);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
            event = eventDAO.update(event);

            String json = objectMapper.writeValueAsString(event);
            ctx.status(200).json(json);
        };
    }

}
