package org.main.dao;

import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.main.ressources.User;
import org.main.ressources.Role;

import java.util.List;

public class UserDAO {
    EntityManagerFactory emf;

    public User create(User user) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        return user;
    }

    public List<User> getAll() {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("select u FROM User  u", User.class);
            List<User> users = q.getResultList();


            return users;
        }
    }

    public User getById(int id) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("FROM User h WHERE h.id = :id", User.class);
            q.setParameter("id", id);
            return q.getSingleResult();


        }

    }

    public User update(User user) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        }
        return user;
    }
    public void delete(User user) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User deleteUser = em.merge(user);
            em.remove(deleteUser);
            em.getTransaction().commit();
        }
    }


    public User createUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(username, password);
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

    public User getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = emf.createEntityManager()) {
            List<User> users = em.createQuery("SELECT u FROM User u").getResultList();
            users.stream().forEach(user -> System.out.println(user.getUsername() + " " + user.getPassword()));
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); //RuntimeException
            user.getRoles().size(); // force roles to be fetched from db
            if (!user.verifyPassword(password))
                throw new ValidationException("Wrong password");
            return user;
        }
    }



}