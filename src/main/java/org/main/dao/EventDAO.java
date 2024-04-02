package org.main.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.main.ressources.Event;

import java.util.List;

public class EventDAO {

    EntityManagerFactory emf;

    public EventDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Event> getAll() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Event> q = em.createQuery("FROM Event e", Event.class);
            List<Event> events = q.getResultList();
            return events;

        }
    }

    public Event getById(int id) {
        try(var em = emf.createEntityManager()){
            TypedQuery<Event> q = em.createQuery("FROM Event e WHERE e.id = :id", Event.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        }
    }

    public Event create(Event event) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
        }
        return event;
    }

    public void delete(Event event) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.remove(event);
            em.getTransaction().commit();
        }
    }

    public Event update(Event event) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(event);
            em.getTransaction().commit();
        }
        return event;
    }
}
