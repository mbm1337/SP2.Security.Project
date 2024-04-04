package org.main.dao;

import org.main.ressources.Role;
import org.main.ressources.User;

public class SecurityDAO {

    public interface ISecurityDAO {
        User createUser(String username, String password);

        Role createRole(String role);

        User addRoleToUser(String username, String role);
    }
}