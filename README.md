# SP2 Security Project

Morten Bomholt Mikkelsen, cph-mm769@cphbusiness.dk , GitHub:  mbm1337

Mustafa Altinkaya, cph-ma763@cphbusiness.dk , GitHub: altinkaya

Sumaia El-Kalache, cph-se160@cphbusiness.dk , GitHub: sumaiak

Mounir Salem, cph-ms848@cphbusiness.dk , GitHub: ETHMUNI

ERD:
![ERD](images/eerd.png)


all funtions are tested with http request and the tests are in the demo.http

most of the functions are tested and working as intended, but there are some issues with the update functions on user, that we are still working on.


GET /events: Retrieves all events.  
GET /events/{id}: Retrieves the event with the specified ID.  
POST /events: Creates a new event.  
PUT /events/{id}: Updates the event with the specified ID.  
DELETE /events/{id}: Deletes the event with the specified ID.  
GET /users: Retrieves all users.  
POST /user: Creates a new user.  
GET /user/{email}: Retrieves the user with the specified email.  
PUT /user/{email}: Updates the user with the specified email.  
DELETE /user/{email}: Deletes the user with the specified email.  
GET /registrations: Retrieves all registrations.  
GET /registrations/id/{id}: Retrieves the registration with the specified ID.  
POST /registrations/{id}: Creates a new registration.  
DELETE /registrations/{id}: Deletes the registration with the specified ID.  
POST /auth/login: Logs in a user.  
POST /auth/register: Registers a new user.  
POST /auth/reset-password: Resets a user's password.  
POST /auth/logout: Logs out a user.


# Security roles and functions for each role
    ANYONE:
        Functions:
            Log in (post("/login"))
            Register (post("/register"))
            Reset password (post("/reset-password"))
            Log out (post("/logout"))

    USER:
        Functions:
            Read all users (get(userHandler.getAllUsers()))
            Read user by email (get(userHandler.getByEmail()))
            Update user (put(userHandler.update()))
            Delete user (delete(userHandler.delete()))
            Read all events (get("/", eventHandler.getAll()))
            Read specific event by ID (get("/{id}", eventHandler.getById()))
            Register user for event (post("/{id}", RegistrationHandler.registerUserForEvent()))
            Cancel user registration for event (delete("/{id}", RegistrationHandler.cancelUserRegistration()))

    ADMIN:
        Functions:
            Create event (post("/"))
            Update event (put("/{id}"))
            Delete event (delete("/{id}"))
            Register user for event (post("/{id}", RegistrationHandler.registerUserForEvent()))
            Cancel user registration for event (delete("/{id}", RegistrationHandler.cancelUserRegistration()))
            Log out (post("/logout"))
