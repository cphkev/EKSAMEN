package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.dto.TripDTO;
import dat.entities.Category;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripRoutesTest {
    private static Javalin app;
    private static EntityManagerFactory emf;
    private static final String BASE_URL = "http://localhost:7000/api";
    private static final String TRIP_URL = "/trips";
    private TripDTO t1, t2, t3;

    @BeforeAll
    public static void setupClass() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(7000);
    }

    @BeforeEach
    public void setup() {
        Populate.populate();
        try (EntityManager em = emf.createEntityManager()) {
            t1 = em.createQuery("SELECT new dat.dto.TripDTO(t) FROM Trip t WHERE t.name = 'Trip to the Beach'", TripDTO.class).getSingleResult();
            t2 = em.createQuery("SELECT new dat.dto.TripDTO(t) FROM Trip t WHERE t.name = 'City Tour'", TripDTO.class).getSingleResult();
            t3 = em.createQuery("SELECT new dat.dto.TripDTO(t) FROM Trip t WHERE t.name = 'Forest Hike'", TripDTO.class).getSingleResult();
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("UPDATE Trip t SET t.guide = null WHERE t.guide IS NOT NULL").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    void tearDownAll(){
        if (app != null){
            app.stop();
        }
    }

    @Test
    void testGetAllTrips() {
        given()
                .when()
                .get(BASE_URL + "/trips")
                .then()
                .statusCode(200);
    }

    @Test
    void getTripById() {
        given()
                .when()
                .get(BASE_URL + TRIP_URL + "/" + t1.getId())
                .then()
                .statusCode(200)
                .body("name", is(t1.getName()));
    }

    @Test
    void createTrip() {
        TripDTO trip = new TripDTO(4, LocalTime.of(9, 0), LocalTime.of(11, 0), 1, "New Trip", 100, Category.BEACH);
        given()
                .contentType("application/json")
                .body(trip)
                .when()
                .post(BASE_URL + TRIP_URL + "/create")
                .then()
                .statusCode(201);
        given()
                .when()
                .get(BASE_URL + TRIP_URL)
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void updateTrip() {
        Category Category = dat.entities.Category.BEACH;
        TripDTO trip = new TripDTO(t1.getId(), LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "Updated Trip", 150, Category.BEACH);
        given()
                .contentType("application/json")
                .body(trip)
                .when()
                .put(BASE_URL + TRIP_URL + "/update/" + t1.getId())
                .then()
                .statusCode(200);
        given()
                .when()
                .get(BASE_URL + TRIP_URL + "/" + t1.getId())
                .then()
                .statusCode(200)
                .body("name", is("Updated Trip"));
    }

    @Test
    void deleteTrip() {
        given()
                .when()
                .delete(BASE_URL + TRIP_URL + "/delete/" + t1.getId())
                .then()
                .statusCode(204);
        given()
                .when()
                .get(BASE_URL + TRIP_URL)
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

}