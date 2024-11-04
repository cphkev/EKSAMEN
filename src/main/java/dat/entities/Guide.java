package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.dto.GuideDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Guide {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;

    @OneToMany
    @JsonIgnore
    private Set<Trip> trips = new HashSet<>();

    public Guide(String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Guide(GuideDTO guideDTO) {
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guide guide = (Guide) o;
        return id == guide.id &&
                yearsOfExperience == guide.yearsOfExperience &&
                Objects.equals(firstName, guide.firstName) &&
                Objects.equals(lastName, guide.lastName) &&
                Objects.equals(email, guide.email) &&
                Objects.equals(phone, guide.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, yearsOfExperience);
    }
}
