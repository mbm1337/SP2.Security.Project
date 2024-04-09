package org.main.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import org.main.ressources.Role;
import org.main.ressources.User;

import java.util.List;

import static org.main.config.HibernateConfig.getEntityManagerFactory;

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
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u JOIN FETCH u.roles r JOIN FETCH r.users", User.class);
            List<User> users = q.getResultList();
            return users;
        }
    }

    public User getByEmail(String email) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("FROM User h WHERE h.email = :email", User.class);
            q.setParameter("email", email);
            return q.getSingleResult();


        }

    }

    public User update(User user) {
        User updatedUser;
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedUser = em.merge(user);
            em.getTransaction().commit();
        }
        return updatedUser;
    }
    public void delete(User user) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User deleteUser = em.merge(user);
            em.remove(deleteUser);
            em.getTransaction().commit();
        }
    }
    public User getById(int id) {
        try (var em = emf.createEntityManager()) {
            return em.find(User.class, id);
        } catch (Exception e) {
            throw new EntityNotFoundException("No user found with ID: " + id);
        }
    }

    public User createUser(String name, String email, String password, String phone) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(name, email, password, phone);
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", "user");
        List<Role> roles = query.getResultList();
        Role userRole;
        if (roles.isEmpty()) {
            userRole = new Role("user");
            em.persist(userRole);
        } else {
            userRole = roles.get(0);
        }
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    public User verifyUser(String email, String password) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> q = em.createQuery("FROM User u WHERE u.email = :email", User.class);
        q.setParameter("email", email);
        User user = q.getSingleResult();
        if (user == null)
            throw new EntityNotFoundException("No user found with email: " + email);
        if (!user.verifyUser(password))
            throw new EntityNotFoundException("Wrong password");
        return user;
    }







    public void updatePassword(String email, String newPassword) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            user.updatePassword(newPassword);

            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






}
