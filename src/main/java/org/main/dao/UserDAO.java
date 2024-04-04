package org.main.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.main.ressources.Role;

import org.main.ressources.User;

import java.util.List;

public class UserDAO {
    EntityManagerFactory emf;
    public UserDAO(EntityManagerFactory _emf)
    {
        this.emf = _emf;
    }

    public User createUser(String name ,String email,String phone,String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(name,email,phone ,password);
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
    public User verifyUser(String username, String password) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);//retrieve entity from DAtabase find is part from Hibernate framework
        if (user == null)
            throw new EntityNotFoundException("No user found with username: " + username);
        if (!user.verifyUser(password))
            throw new EntityNotFoundException("Wrong password");
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



    }