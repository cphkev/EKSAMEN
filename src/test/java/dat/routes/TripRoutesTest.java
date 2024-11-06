package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dao.GuideDAO;
import dat.dao.TripDAO;
import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.entities.Category;
import dat.entities.Guide;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripRoutesTest {
    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final SecurityDAO securityDAO = new SecurityDAO(emf);
    private static TripDAO tripDAO;
    private static GuideDAO guideDAO;


    private static final String BASE_URL = "http://localhost:7070/api";

    private static TripDTO t1, t2;

    private static GuideDTO g1, g2;
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;

    @BeforeAll
    void setUpAll() {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);
        guideDAO = GuideDAO.getInstance(emf);
        tripDAO = TripDAO.getInstance(emf);
    }

    @BeforeEach
    void setup() throws ApiException {
        System.out.println("Populating database");

        Populator.populateGuidesAndTrips(emf);


        g1 = guideDAO.getById(1);
        g2 = guideDAO.getById(2);


        t1 = tripDAO.getById(1);
        t2 = tripDAO.getById(2);


        UserDTO[] userDTOs = Populator.populateUsers(emf);
        userDTO = userDTOs[0];
        adminDTO = userDTOs[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDownAll() {
        ApplicationConfig.stopServer(app);
    }


    @Test
    void getAllTrips() {
        List<TripDTO> trips =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips")
                        .then()
                        .statusCode(200)
                        .body("size()", is(2))
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertThat(trips.size(), is(2));
    }

    @Test
    void getTripById() {
        TripDTO trip =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/" + t1.getId())
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertThat(trip.getName(), is(t1.getName()));
    }

    @Test
    void createTrip() {
        TripDTO newTrip = new TripDTO();
        newTrip.setName("Beach Getaway");
        newTrip.setStartTime(java.time.LocalTime.of(20, 6, 0));
        newTrip.setEndTime(java.time.LocalTime.of(20, 6, 0));
        newTrip.setStartPostion(2);
        newTrip.setPrice(50);
        newTrip.setCategory(Category.BEACH);
        newTrip.setGuide(g1);

        TripDTO createdTrip =
                given()
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(newTrip)
                        .when()
                        .post(BASE_URL + "/trips")
                        .then()
                        .statusCode(201)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertThat(createdTrip.getId(), is(notNullValue()));
        assertThat(createdTrip.getName(), is(newTrip.getName()));
    }

    @Test
    void updateTrip() {

        TripDTO updatedTrip = new TripDTO();
        updatedTrip.setId(t1.getId());
        updatedTrip.setName("City Tour 2.0");
        updatedTrip.setStartTime(java.time.LocalTime.of(20, 0, 0));
        updatedTrip.setStartTime(java.time.LocalTime.of(20, 0, 0));
        updatedTrip.setStartPostion(1);
        updatedTrip.setPrice(35);
        updatedTrip.setCategory(Category.CITY);
        updatedTrip.setGuide(g2);

        TripDTO updated =
                given()
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(updatedTrip)
                        .when()
                        .put(BASE_URL + "/trips/" + updatedTrip.getId())
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertThat(updated.getName(), is(updatedTrip.getName()));
    }


    @Test
    void deleteTrip() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete(BASE_URL + "/trips/" + t1.getId())
                .then()
                .statusCode(200);

        // Verification
        given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/trips/" + t1.getId())
                .then()
                .statusCode(404); // not found after deletion
    }


    @Test
    void getTripsByCategory() {
        List<TripDTO> trips =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/category/" + Category.CITY)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertThat(trips.size(), is(greaterThan(0)));
    }


}