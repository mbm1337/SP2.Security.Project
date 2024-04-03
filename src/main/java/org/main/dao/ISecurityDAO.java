package org.main.dao;

import org.main.Exceptions.NotAuthorizedException;
import org.main.ressources.User;
import org.main.ressources.Role;

    public interface ISecurityDAO {
        User getVerifiedUser(String username, String password) throws NotAuthorizedException;
        User createUser(String username, String password);
        Role createRole(String role);
        User addUserRole(String username, String role);}
}