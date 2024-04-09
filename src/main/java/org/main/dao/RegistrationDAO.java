package org.main.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.main.dto.RegistrationDTO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.main.ressources.Registration;
import org.main.ressources.User;
import org.main.ressources.Event;
import java.util.List;

public class RegistrationDAO {

    EntityManagerFactory emf;
    private static RegistrationDAO instance;


    public RegistrationDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static synchronized RegistrationDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new RegistrationDAO(_emf);
        }
        return instance;
    }

    public List<Registration> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Registration> query = em.createQuery("SELECT r From Registration r", Registration.class);
            return query.getResultList();
        }
    }

    public Registration getRegistrationNameEvent(int userId, int eventId){

        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Registration> query = em.createQuery("Select r From Registration r " +
                    "join r.user " +
                    "join r.event where r.user.id=:userId and r.event.id=:eventId", Registration.class);
            query.setParameter("userId",userId);
            query.setParameter("eventId",eventId);
            return query.getSingleResult();
        }
    }

    public Registration getById(int id) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Registration> q = em.createQuery("Select r From Registration r WHERE r.id = :id", Registration.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        }
    }

    public List<Registration> getByEventId(int eventId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Registration> query = em.createQuery("SELECT r From Registration r WHERE r.event.id = :eventId", Registration.class);
            query.setParameter("eventId", eventId);
            return query.getResultList();
        }
    }

    public Registration registerUserForEvent(User user, Event event) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Registration registration = new Registration();
            registration.setUser(user);
            registration.setEvent(event);
            em.persist(registration);
            em.getTransaction().commit();
            return registration;
        }
    }

    public boolean cancelRegistration(int registrationId) {
        try (EntityManager em = emf.createEntityManager()) {
            Registration registration = em.find(Registration.class, registrationId);
            if (registration != null) {
                em.getTransaction().begin();
                em.remove(registration);
                em.getTransaction().commit();
                return true;
            }
            return false;
        }
    }
}
