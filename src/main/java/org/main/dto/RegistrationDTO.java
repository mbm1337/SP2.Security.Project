package org.main.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RegistrationDTO {
    private int regId;
    private int userId;
    private String userName;
    private int eventId;
    private String eventName;
}
