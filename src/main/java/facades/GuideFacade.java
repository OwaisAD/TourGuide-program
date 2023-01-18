package facades;

import entities.Guide;
import entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GuideFacade {

    private static EntityManagerFactory emf;
    private static GuideFacade instance;

    private GuideFacade() {
    }

    public static GuideFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideFacade();
        }
        return instance;
    }


    public Guide getGuideById(Integer id) {
        EntityManager em = emf.createEntityManager();
        Guide guide;
        try {
            em.getTransaction().begin();
            guide = em.find(Guide.class, id);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return guide;
    }
}
