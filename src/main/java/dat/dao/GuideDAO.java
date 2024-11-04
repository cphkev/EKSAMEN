package dat.dao;

import dat.dto.GuideDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exception.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;



public class GuideDAO implements IDao<GuideDTO> {

    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO(emf);
        }
        return instance;
    }


    @Override
    public List<GuideDTO> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<GuideDTO> query = em.createQuery("SELECT new dat.dto.GuideDTO(g) FROM Guide g", GuideDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public GuideDTO getById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            Guide guide = em.find(Guide.class, id);
            return new GuideDTO(guide);
        }
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        }
    }

    @Override
    public void update(GuideDTO guideDTO, GuideDTO update) {

    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            for (Trip trip : guide.getTrips()) {
                trip.setGuide(null);
                em.merge(trip);
            }
            em.remove(guide);
            em.getTransaction().commit();
        }
    }
}
