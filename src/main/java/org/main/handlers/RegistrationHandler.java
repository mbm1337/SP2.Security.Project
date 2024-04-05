package org.main.handlers;

import org.main.dao.RegistrationDAO;
import org.main.dto.RegistrationDTO;
import org.main.exception.ApiException;
import io.javalin.http.Handler;
import org.main.ressources.Registration;

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
            List<jakarta.servlet.Registration> servletRegistrations = registrationDAO.readAll();
            List<org.main.ressources.Registration> registrations = new ArrayList<>();

            for (jakarta.servlet.Registration servletRegistration : servletRegistrations) {

            }

            List<RegistrationDTO> registrationDTOS = new ArrayList<>();

            for (Registration r : registrations){
                registrationDTOS.add(convertToDTO(r));
            }

            if (registrationDAO != null) {
                ctx.json(registrationDTOS);
            } else {
                throw new ApiException(404, "No registrations available. " + timestamp);
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


}
