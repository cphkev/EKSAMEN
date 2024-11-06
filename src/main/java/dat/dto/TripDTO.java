package dat.dto;

import dat.entities.Category;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TripDTO {
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int startPostion;
    private String name;
    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;
    private GuideDTO guide;
    private List<PackingItemDTO> packingItems;
    public TripDTO(int id, LocalTime startTime, LocalTime endTime, int startPostion, String name, int price, Category category) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPostion = startPostion;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public TripDTO(LocalTime startTime, LocalTime endTime, int startPostion, String name, int price, Category category, GuideDTO guide, List<PackingItemDTO> packingItems) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPostion = startPostion;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guide = guide;
        this.packingItems = packingItems;
    }

    public TripDTO(Trip trip){
        this.id = trip.getId();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.startPostion = trip.getStartPostion();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guide = new GuideDTO(trip.getGuide());
    }



    public void addGuide(GuideDTO guide) {
        guide.add(this);
    }
}
