package org.main.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.main.Exception.NotAuthorizedException;
import org.main.ressources.Role;
import org.main.ressources.User;


import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserDAO {

    private static UserDAO instance;
    private static EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory _emf) {
        this.emf = _emf;
    }


    public User getVerifiedUser(String email, String password) throws NotAuthorizedException {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, email);

            if (user == null || !user.verifyUser(password)) {
                throw new NotAuthorizedException(401, "Invalid user name or password");
            }
            em.getTransaction().commit();
            return user;
        }
    }

    public User registerUser(String name, String email, String phone, String password) throws NotAuthorizedException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(name,email, phone,password);
        Role userRole = em.find(Role.class, "user");
        if (userRole == null) {
            userRole = new Role("user");
            em.persist(userRole);
        }
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }



    public Role createRole(String role) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Role newRole = new Role(role);
            em.persist(newRole);
            em.getTransaction().commit();
            return newRole;
        }
    }

    public User read(String userName) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, userName);
            em.getTransaction().commit();
            return user;
        }
    }

    public List<User> getAll() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            for(User u:users){
                u.getRoles().size();
            }
            em.getTransaction().commit();
            return users;
        }
    }


    public User update(User user) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        }
        return user;
    }
    public void delete(int id ) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            em.remove(user);
            em.getTransaction().commit();
        }
    }



    public User getById(int id) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("FROM User h WHERE h.id = :id", User.class);
            q.setParameter("id", id);
            return q.getSingleResult();


        }

    }

}