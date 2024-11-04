package dat.controller;

import dat.dao.TripDAO;
import dat.dto.PackingItemDTO;
import dat.dto.TripDTO;
import dat.entities.Category;
import dat.entities.Trip;
import dat.exception.ApiException;
import dat.exception.Message;
import dat.services.PackingService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TripController implements IController {

    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    private final TripDAO tripDAO;

    public TripController(TripDAO tripDAO) {
        this.tripDAO = tripDAO;
    }



    public void getById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            TripDTO tripDTO = tripDAO.getById(id);
            if (tripDTO == null) {
                ctx.json(new Message(404, "Trip not found"));
                throw new ApiException(404, "Trip not found");
            }
            List<PackingItemDTO> packingItems = PackingService.getPackingItems(tripDTO.getCategory().name().toLowerCase());
            tripDTO.setPackingItems(packingItems);
            ctx.res().setStatus(200);
            ctx.json(tripDTO);
        } catch (Exception e) {
            log.error("Error reading trip: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }
    @Override
    public void create(Context ctx) {
        try {
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO createdTrip = tripDAO.create(tripDTO);
            ctx.res().setStatus(201);
            ctx.json(createdTrip);
        } catch (ApiException e) {
            log.error("Error creating trip: {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }

    }

    @Override
    public void update(Context ctx) {
        try {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        TripDTO updatedTrip = ctx.bodyAsClass(TripDTO.class);
        TripDTO existingTrip = tripDAO.getById(id);
        if (existingTrip == null) {
            throw new ApiException(404, "Trip not found");
        }
        tripDAO.update(existingTrip, updatedTrip);
        TripDTO updatedTripDTO = tripDAO.getById(id);
        ctx.res().setStatus(200);
        ctx.json(updatedTripDTO);
        } catch (ApiException e) {
            log.error("Error updating trip: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }



    @Override
    public void delete(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            tripDAO.delete(id);
            ctx.res().setStatus(200);
            ctx.result("Trip with id " + id + " has been successfully deleted.");
            log.info("Trip with id {} has been successfully deleted.", id);
            if (tripDAO.getById(id) == null){
                ctx.json(new Message(404, "Trip not found"));
                throw new ApiException(404, "Trip not found");
            }
        } catch (ApiException e) {
            log.error("Error deleting trip: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {
        try{
            ctx.res().setStatus(200);
            ctx.json(tripDAO.getAll());
        } catch (ApiException e) {
            log.error("Error reading all trips: {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }

    }

    public void getTripsByGuide(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            ctx.res().setStatus(200);
            ctx.json(tripDAO.getTripsByGuide(id));
        } catch (ApiException e) {
            log.error("Error reading trips by guide: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }

    public void addGuideToTrip(Context ctx) {
        try {
            int tripId = ctx.pathParamAsClass("tripId", Integer.class).check(this::validatePrimaryKey, "Not a valid trip id").get();
            int guideId = ctx.pathParamAsClass("guideId", Integer.class).check(this::validatePrimaryKey, "Not a valid guide id").get();
            tripDAO.addGuideToTrip(tripId, guideId);
            ctx.res().setStatus(204);
            log.info("Guide with id {} has been successfully added to trip with id {}.", guideId, tripId);
            System.out.println("Guide with id " + guideId + " has been successfully added to trip with id " + tripId + ".");
        } catch (ApiException e) {
            log.error("Error adding guide to trip: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }


    public void getTripsByCategory(Context ctx){
        try {
            String categoryParam = ctx.pathParam("category");
            Category category;
            try {
                category = Category.valueOf(categoryParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Invalid category");
            }
            ctx.res().setStatus(200);
            ctx.json(tripDAO.getTripsByCategory(category));
        } catch (ApiException e) {
            log.error("Error reading trips by category: {}", e.getMessage());
            throw new ApiException(404, e.getMessage());
        }
    }


    public void getPackingItemsByCategory(Context ctx) {
        try {
            String categoryParam = ctx.pathParam("category");
            Category category;
            try {
                category = Category.valueOf(categoryParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Invalid category");
            }
            List<PackingItemDTO> packingItems = PackingService.getPackingItems(category.name().toLowerCase());
            ctx.res().setStatus(200);
            ctx.json(packingItems);
        } catch (Exception e) {
            log.error("Error fetching packing items by category: {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    public void getTotalPriceByGuide(Context ctx) {
        try {
            List<Map<String, Object>> totalPriceByGuide = tripDAO.getTotalPriceByGuide();
            ctx.res().setStatus(200);
            ctx.json(totalPriceByGuide);
        } catch (ApiException e) {
            log.error("Error getting total price by guide: {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }




    public void getPackingItemsWeightSum(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            TripDTO tripDTO = tripDAO.getById(id);
            if (tripDTO == null) {
                ctx.json(new Message(404, "Trip not found"));
                throw new ApiException(404, "Trip not found");
            }
            List<PackingItemDTO> packingItems = PackingService.getPackingItems(tripDTO.getCategory().name().toLowerCase());
            int totalWeight = packingItems.stream().mapToInt(PackingItemDTO::getWeightInGrams).sum();
            ctx.res().setStatus(200);
            ctx.json(Map.of("totalWeight", totalWeight));
        } catch (Exception e) {
            log.error("Error calculating packing items weight sum: {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    private Boolean validatePrimaryKey(Integer integer) {
       return tripDAO.validatePrimaryKey(integer);
    }
}
