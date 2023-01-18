package facades;

import dtos.GuideDTO;
import entities.Guide;
import entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

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


    public List<GuideDTO> getAllGuides() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g", Guide.class);
            List<Guide> allGuides = query.getResultList();
            return GuideDTO.getDTOs(allGuides);
        } finally {
            em.close();
        }
    }

    public Guide createGuide(Guide guide) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return guide;
    }
}
