package dat.routes;

import dat.config.HibernateConfig;
import dat.controller.TripController;
import dat.dao.TripDAO;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final TripDAO tripDAO = new TripDAO(emf);

    private final TripController tripController = new TripController(tripDAO);

    SecurityController securityController = SecurityController.getInstance();

    protected EndpointGroup getRoutes(){
        return () -> {
            before(securityController.authenticate());
            post("/login", securityController.login(), Role.ANYONE);
            get("/trips", tripController::getAll, Role.ANYONE);
            get("/trips/{id}", tripController::getById, Role.ANYONE);
            post("/trips/create", tripController::create, Role.ADMIN);
            put("/trips/update/{id}", tripController::update, Role.ADMIN);
            delete("/trips/delete/{id}", tripController::delete, Role.ADMIN);
            put("/trips/addGuideToTrip/{tripId}/{guideId}", tripController::addGuideToTrip, Role.ADMIN);
            get("/trips/getTripsByGuide/{id}", tripController::getTripsByGuide, Role.ANYONE);
            get("/trips/category/{category}", tripController::getTripsByCategory, Role.ANYONE);
            get("/trips/totalPrice", tripController::getTotalPriceByGuide, Role.ANYONE);
            get("/trips/packingItemsWeightSum/{id}", tripController::getPackingItemsWeightSum, Role.ANYONE);
            get("/trips/packingItemsByCategory/{category}", tripController::getPackingItemsByCategory, Role.ANYONE);
        };
    }


}
