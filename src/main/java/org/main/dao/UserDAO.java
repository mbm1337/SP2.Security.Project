package org.main.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import org.main.exception.NotAuthorizedException;
import org.main.ressources.Role;
import org.main.ressources.User;

import java.util.List;

public class UserDAO  {
    private EntityManagerFactory emf;
    public UserDAO(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

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

    public User getById(String email) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("FROM User h WHERE h.email = :email", User.class);
            q.setParameter("email", email);
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


    public User createUser(String name, String email, String password, String phone) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(name, email, password, phone);
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

    public User getVerifiedUser(String email, String password) throws NotAuthorizedException {
        try (EntityManager em = emf.createEntityManager()) {
            List<User> users = em.createQuery("SELECT u FROM User u").getResultList();
            users.stream().forEach(user -> System.out.println(user.getName() + " " + user.getPassword()));
            User user = em.find(User.class, email);
            if (user == null)
                throw new EntityNotFoundException("No user found with email: " + email); //RuntimeException
            user.getRoles().size(); // force roles to be fetched from db
            if (!user.verifyUser(password)) {
                throw new NotAuthorizedException(401, "Wrong password");
            }
            return user;
        }
    }



}