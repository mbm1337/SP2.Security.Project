package org.main.security;

import com.nimbusds.jose.JOSEException;
import io.javalin.http.Handler;
import org.main.exception.NotAuthorizedException;
import org.main.dto.UserDTO;


import java.text.ParseException;
import java.util.Set;

public interface ISecurityController {
    Handler register();
    Handler login();

    String createToken(UserDTO user);


    boolean authorize(UserDTO user, Set<String> allowedRoles);

    Handler authenticate();
    UserDTO verifyToken(String token);

    boolean tokenIsValid(String token, String secret) throws ParseException, JOSEException, NotAuthorizedException;
}
