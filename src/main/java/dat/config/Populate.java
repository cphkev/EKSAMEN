package dat.config;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.entities.Category;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;
import java.util.Set;


public class Populate {

    public static void populate() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Trip> trips = getTrips();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide();
            guide.setFirstName("John");
            guide.setLastName("Doe");
            guide.setEmail("john.doe@example.com");
            guide.setPhone("123456789");
            guide.setYearsOfExperience(10);
            guide.setTrips(trips);
            em.persist(guide);
            for (Trip trip : trips) {
                trip.setGuide(guide);
                em.persist(trip);
            }
            em.getTransaction().commit();
        }
    }

    private static Set<Trip> getTrips() {
        LocalTime startTime1 = LocalTime.of(9, 0);
        LocalTime endTime1 = LocalTime.of(11, 0);

        LocalTime startTime2 = LocalTime.of(12, 0);
        LocalTime endTime2 = LocalTime.of(14, 0);

        LocalTime startTime3 = LocalTime.of(15, 0);
        LocalTime endTime3 = LocalTime.of(17, 0);

        Trip trip1 = new Trip();
        trip1.setStartTime(startTime1);
        trip1.setEndTime(endTime1);
        trip1.setStartPostion(1);
        trip1.setName("Trip to the Beach");
        trip1.setPrice(100);
        trip1.setCategory(Category.BEACH);

        Trip trip2 = new Trip();
        trip2.setStartTime(startTime2);
        trip2.setEndTime(endTime2);
        trip2.setStartPostion(2);
        trip2.setName("City Tour");
        trip2.setPrice(150);
        trip2.setCategory(Category.CITY);

        Trip trip3 = new Trip();
        trip3.setStartTime(startTime3);
        trip3.setEndTime(endTime3);
        trip3.setStartPostion(3);
        trip3.setName("Forest Hike");
        trip3.setPrice(200);
        trip3.setCategory(Category.FOREST);

        Trip[] trips = {trip1, trip2, trip3};
        return Set.of(trips);
    }
}