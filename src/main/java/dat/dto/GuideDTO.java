package dat.dto;

import dat.entities.Guide;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
public class GuideDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;
    private Set<TripDTO> trips = new HashSet<>();

    public GuideDTO(int id, String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public GuideDTO(Guide guide){
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearsOfExperience = guide.getYearsOfExperience();
    }



    public void add(TripDTO tripDTO){
        trips.add(tripDTO);
    }
}
