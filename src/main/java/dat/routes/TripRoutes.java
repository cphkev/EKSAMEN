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
            get("/", tripController::getAll, Role.ANYONE);
            get("/{id}", tripController::getById, Role.ANYONE);
            post("/create", tripController::create, Role.ADMIN);
            put("/update/{id}", tripController::update, Role.ADMIN);
            delete("/delete/{id}", tripController::delete, Role.ADMIN);
            put("/addGuideToTrip/{tripId}/{guideId}", tripController::addGuideToTrip, Role.ADMIN);
            get("/getTripsByGuide/{id}", tripController::getTripsByGuide, Role.ANYONE);
            get("/category/{category}", tripController::getTripsByCategory, Role.ANYONE);
            get("/guides/totalprice", tripController::getTotalPriceByGuide);
            get("/packingItemsWeightSum/{id}", tripController::getPackingItemsWeightSum, Role.ANYONE);
            get("/packingItemsByCategory/{category}", tripController::getPackingItemsByCategory, Role.ANYONE);
        };
    }


}
