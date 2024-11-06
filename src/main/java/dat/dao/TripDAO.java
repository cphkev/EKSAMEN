package dat.dao;


import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.entities.Category;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exception.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManagerFactory;
public class TripDAO implements IDao<TripDTO> , ITripGuideDAO{

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO(emf);
        }
        return instance;
    }
    @Override
    public List<TripDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t JOIN FETCH t.guide g LEFT JOIN FETCH g.trips", Trip.class);
            List<Trip> trips = query.getResultList();
            return trips.stream().map(TripDTO::new).toList();
        }
    }
    @Override
    public TripDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            return new TripDTO(trip);
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO);
            if (trip.getId() != 0) {
                trip = em.merge(trip);
            } else {
                em.persist(trip);
            }
            em.getTransaction().commit();
            return new TripDTO(trip);
        }
    }

    @Override
    public void update(TripDTO tripDTO, TripDTO update) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripDTO.getId());
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            trip.setName(update.getName());
            trip.setPrice(update.getPrice());
            trip.setStartTime(update.getStartTime());
            trip.setEndTime(update.getEndTime());
            trip.setStartPostion(update.getStartPostion());
            trip.setCategory(update.getCategory());
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            Guide guide = trip.getGuide();
            if (guide != null) {
                guide.getTrips().remove(trip);
                em.merge(guide);
            }
            em.remove(trip);
            em.getTransaction().commit();
        }
    }
    @Override
    public void addGuideToTrip(int tripId, int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            } else if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            trip.setGuide(guide);
            em.persist(trip);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            return guide.getTrips().stream().map(TripDTO::new).collect(Collectors.toSet());
        }
    }

    public List<TripDTO> getTripsByCategory(Category category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t WHERE t.category = :category", Trip.class);
            query.setParameter("category", category);
            return query.getResultStream()
                    .map(TripDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public List<Map<String, Object>> getTotalPriceByGuide() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT g.id, SUM(t.price) FROM Trip t JOIN t.guide g GROUP BY g.id", Object[].class);
            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(result -> Map.of("guideId", result[0], "totalPrice", result[1]))
                    .collect(Collectors.toList());
        }
    }

    public Boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, integer);
            return trip != null;
        }
    }


}