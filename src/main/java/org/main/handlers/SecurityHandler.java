package org.main.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.main.Utils.TokenUtils;
import org.main.Utils.Utils;
import org.main.config.HibernateConfig;
import org.main.dao.UserDAO;
import org.main.dto.TokenDTO;
import org.main.dto.UserDTO;
import org.main.exception.ApiException;
import org.main.exception.NotAuthorizedException;
import org.main.ressources.User;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SecurityHandler implements ISecurityHandler {
    UserDAO securityDAO = new UserDAO(HibernateConfig.getEntityManagerFactory());
    ObjectMapper objectMapper = new ObjectMapper();
    private final String SECRET_KEY = "DetteErEnHemmeligNøgleTilAtDanneJWT_Tokensmed";
    @Override
    public Handler register() {
        return (ctx) -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try {
                UserDTO userInput = ctx.bodyAsClass(UserDTO.class);
                User created = securityDAO.createUser(userInput.getName(), userInput.getEmail(), userInput.getPhone(), userInput.getPassword());

                String token = createToken(new UserDTO(created));
                ctx.status(HttpStatus.CREATED).json(new TokenDTO(token, userInput.getEmail()));
            } catch (EntityExistsException e) {
                ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
                ctx.json(returnObject.put("msg", "User already exists"));
            }
        };
    }
    @Override
    public Handler login() {
        return (ctx) -> {
            ObjectNode returnObject = objectMapper.createObjectNode(); // for sending json messages back to the client
            try {
                UserDTO user = ctx.bodyAsClass(UserDTO.class);
                System.out.println("USER IN LOGIN: " + user);

                User verifiedUserEntity = securityDAO.getVerifiedUser(user.getEmail(), user.getPassword());
                String token = createToken(new UserDTO(verifiedUserEntity));
                ctx.status(200).json(new TokenDTO(token, user.getEmail()));

            } catch (EntityNotFoundException | ValidationException e) {
                ctx.status(401);
                System.out.println(e.getMessage());
                ctx.json(returnObject.put("msg", e.getMessage()));
            }
        };
    }

    @Override
    public String createToken(UserDTO user) {
        String ISSUER;
        String TOKEN_EXPIRE_TIME;
        String SECRET_KEY;

        if (System.getenv("DEPLOYED") != null) {
            ISSUER = System.getenv("ISSUER");
            TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
            SECRET_KEY = System.getenv("SECRET_KEY");
        } else {
            ISSUER = "Thomas Hartmann";
            TOKEN_EXPIRE_TIME = "1800000"; // 30 minutes in milliseconds
            SECRET_KEY = Utils.getPropertyValue("SECRET_KEY","config.properties");
        }
        return TokenUtils.createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
    }

    @Override
    public boolean authorize(UserDTO user, Set<String> allowedRoles) {
        // Called from the ApplicationConfig.setSecurityRoles

        AtomicBoolean hasAccess = new AtomicBoolean(false); // Since we update this in a lambda expression, we need to use an AtomicBoolean
        if (user != null) {
            user.getRoles().stream().forEach(role -> {
                if (allowedRoles.contains(role.toUpperCase())) {
                    hasAccess.set(true);
                }
            });
        }
        return hasAccess.get();
    }

    @Override
    public Handler authenticate() {
        // To check the users roles against the allowed roles for the endpoint (managed by javalins accessManager)
        // Checked in 'before filter' -> Check for Authorization header to find token.
        // Find user inside the token, forward the ctx object with userDTO on attribute
        // When ctx hits the endpoint it will have the user on the attribute to check for roles (ApplicationConfig -> accessManager)
        ObjectNode returnObject = objectMapper.createObjectNode();
        return (ctx) -> {
            if(ctx.method().toString().equals("OPTIONS")) {
                ctx.status(200);
                return;
            }
            String header = ctx.header("Authorization");
            if (header == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Authorization header missing"));
                return;
            }
            String token = header.split(" ")[1];
            if (token == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Authorization header malformed"));
                return;
            }
            UserDTO verifiedTokenUser = verifyToken(token);
            if (verifiedTokenUser == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Invalid User or Token"));
            }
            System.out.println("USER IN AUTHENTICATE: " + verifiedTokenUser);
            ctx.attribute("user", verifiedTokenUser);
        };
    }

    @Override
    public UserDTO verifyToken(String token) {
            boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
            String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY") : Utils.getPropertyValue("SECRET_KEY","config.properties");

            try {
                if (TokenUtils.tokenIsValid(token, SECRET) && TokenUtils.tokenNotExpired(token)) {
                    return TokenUtils.getUserWithRolesFromToken(token);
                } else {
                    throw new NotAuthorizedException(403, "Token is not valid");
                }
            } catch (ParseException | JOSEException | NotAuthorizedException e) {
                e.printStackTrace();
                throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Could not verify token");
            }
    }

    @Override
    public boolean tokenIsValid(String token, String secret) throws ParseException, JOSEException, NotAuthorizedException {
        SignedJWT jwt = SignedJWT.parse(token);
        if (jwt.verify(new MACVerifier(secret)))
            return true;
        else
            throw new NotAuthorizedException(403, "Token is not valid");
    }

    @Override
    public boolean tokenNotExpired(String token) throws ParseException, NotAuthorizedException {
        if (timeToExpire(token) > 0)
            return true;
        else
            throw new NotAuthorizedException(403, "Token has expired");
    }

    @Override
    public UserDTO getUserWithRolesFromToken(String token) throws ParseException {
        // Return a user with Set of roles as strings
        SignedJWT jwt = SignedJWT.parse(token);
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("username").toString();

        Set<String> rolesSet = Arrays
                .stream(roles.split(","))
                .collect(Collectors.toSet());
        return new UserDTO(username, rolesSet);
    }

    @Override
    public int timeToExpire(String token) throws ParseException, NotAuthorizedException {
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime());
    }
}
