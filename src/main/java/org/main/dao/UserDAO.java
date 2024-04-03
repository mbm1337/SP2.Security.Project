package org.main.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.main.ressources.User;

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



    }