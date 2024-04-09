package org.main.handlers;

import org.main.dao.EventDAO;
import org.main.dao.RegistrationDAO;
import org.main.dao.UserDAO;
import org.main.dto.RegistrationDTO;
import org.main.exception.ApiException;
import io.javalin.http.Handler;
import org.main.ressources.Event;
import org.main.ressources.Registration;
import org.main.ressources.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistrationHandler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    public RegistrationHandler(RegistrationDAO registrationDAO) {
    }


    private static RegistrationDTO convertToDTO(Registration reg){
        return RegistrationDTO.builder()
                .regId(reg.getId())
                .userId(Math.toIntExact(reg.getUser().getId()))
                .userName(reg.getUser().getName())
                .eventId(reg.getEvent().getId())
                .eventName(reg.getEvent().getDescription())
                .build();
    }

    public static Handler readAll(RegistrationDAO registrationDAO) {
        return ctx -> {
            List<Registration> registrations = registrationDAO.readAll();
            if (registrations.isEmpty()) {
                throw new ApiException(404, "No registrations available. " + timestamp);
            } else {
                List<RegistrationDTO> registrationDTOS = new ArrayList<>();
                for (Registration r : registrations){
                    registrationDTOS.add(convertToDTO(r));
                }

                ctx.status(200).json(registrationDTOS);
            }
        };
    }

    public static Handler getById(RegistrationDAO registrationDAO){
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Registration registration = (Registration) registrationDAO.getById(id);

            if(registration != null){
                ctx.json(registration);
            }else{
                throw new ApiException(404, "Registration with ID: "+id+" was not found " + timestamp); // Use ApiException
            }
        };
    }

    public static Handler getRegistrationsByEventId(RegistrationDAO registrationDAO) {
        return ctx -> {
            int eventId = Integer.parseInt(ctx.pathParam("id"));
            List<Registration> registrations = registrationDAO.getByEventId(eventId);
            if (registrations.isEmpty()) {
                throw new ApiException(404, "No registrations available for this event. " + timestamp);
            } else {
                List<RegistrationDTO> registrationDTOS = new ArrayList<>();
                for (Registration r : registrations){
                    registrationDTOS.add(convertToDTO(r));
                }
                ctx.status(200).json(registrationDTOS);
            }
        };
    }

    public static Handler registerUserForEvent(RegistrationDAO registrationDAO, EventDAO eventDAO, UserDAO userDAO) {
        return ctx -> {
            int eventId = Integer.parseInt(ctx.pathParam("id"));
            int userId = Integer.parseInt(ctx.body()); // Assuming the body contains the user ID as a string
            User user = userDAO.getById(userId);
            if (user == null) {
                throw new ApiException(404, "User with ID: " + userId + " was not found " + timestamp);
            }
            Event event = eventDAO.getById(eventId);
            if (event == null) {
                throw new ApiException(404, "Event with ID: " + eventId + " was not found " + timestamp);
            }
            Registration registration = registrationDAO.registerUserForEvent(user, event);
            if (registration == null) {
                throw new ApiException(400, "Unable to register user for this event. " + timestamp);
            } else {
                ctx.status(201).json(convertToDTO(registration));
            }
        };
    }

    public static Handler cancelUserRegistration(RegistrationDAO registrationDAO) {
        return ctx -> {
            int registrationId = Integer.parseInt(ctx.pathParam("id"));
            boolean isCancelled = registrationDAO.cancelRegistration(registrationId);
            if (!isCancelled) {
                throw new ApiException(400, "Unable to cancel registration. " + timestamp);
            } else {
                ctx.status(204);
            }
        };
    }


}
