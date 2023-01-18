package entities;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "guide")
public class Guide implements entities.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "gender", nullable = false, length = 45)
    private String gender;

    @Size(max = 4)
    @NotNull
    @Column(name = "birthYear", nullable = false, length = 4)
    private String birthYear;

    @Size(max = 45)
    @NotNull
    @Column(name = "profile", nullable = false, length = 45)
    private String profile;

    @Size(max = 2048)
    @NotNull
    @Column(name = "image", nullable = false, length = 2048)
    private String image;

    @OneToMany(mappedBy = "guide")
    private Set<Trip> trips = new LinkedHashSet<>();

    public Guide(String gender, String birthYear, String profile, String image) {
        this.gender = gender;
        this.birthYear = birthYear;
        this.profile = profile;
        this.image = image;
    }

    public Guide() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide)) return false;
        Guide guide = (Guide) o;
        return getId().equals(guide.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Guide{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", profile='" + profile + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}