package facades;

import dtos.TripDTO;
import entities.Person;
import entities.Trip;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class PersonFacade {

    private static EntityManagerFactory emf;
    private static PersonFacade instance;

    private PersonFacade() {
    }

    public static PersonFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }


    public Person getPersonById(int personId) {
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, personId);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return person;
    }
}
