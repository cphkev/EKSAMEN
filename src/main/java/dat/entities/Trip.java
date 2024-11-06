package dat.entities;

import dat.dto.TripDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int startPostion;
    private String name;
    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private Guide guide;


    public Trip(int id, LocalTime startTime, LocalTime endTime, int startPostion, String name, int price, Category category, Guide guide) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPostion = startPostion;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guide = guide;
    }

    public Trip(LocalTime startTime, LocalTime endTime, int startPostion, String name, int price, Category category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPostion = startPostion;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Trip(TripDTO tripDTO){
        this.id = tripDTO.getId();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.startPostion = tripDTO.getStartPostion();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id &&
                startPostion == trip.startPostion &&
                price == trip.price &&
                Objects.equals(startTime, trip.startTime) &&
                Objects.equals(endTime, trip.endTime) &&
                Objects.equals(name, trip.name) &&
                category == trip.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, startPostion, name, price, category);
    }

}
