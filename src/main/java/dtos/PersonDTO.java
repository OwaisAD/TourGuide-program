package dtos;

import entities.Person;
import entities.Trip;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link Person} entity
 */
public class PersonDTO implements Serializable {
    private final Integer id;
    @Size(max = 45)
    @NotNull
    private final String address;
    @Size(max = 45)
    @NotNull
    private final String email;
    @NotNull
    private final Integer birthYear;
    @Size(max = 45)
    @NotNull
    private final String gender;
    private final List<TripInnerDTO> trips = new ArrayList<>();

    private final Integer userId;

    public PersonDTO(Integer id, String address, String email, Integer birthYear, String gender, Integer userId) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.birthYear = birthYear;
        this.gender = gender;
        this.userId = userId;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.address = person.getAddress();
        this.email = person.getEmail();
        this.birthYear = person.getBirthYear();
        this.userId = person.getUser().getId();
        this.gender = person.getGender();
        person.getTrips().forEach(trip -> {
            trips.add(new TripInnerDTO(trip));
        });
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public String getGender() {
        return gender;
    }

    public List<TripInnerDTO> getTrips() {
        return trips;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", birthYear=" + birthYear +
                ", gender='" + gender + '\'' +
                ", trips=" + trips +
                ", userId=" + userId +
                '}';
    }

    /**
     * A DTO for the {@link Trip} entity
     */
    public static class TripInnerDTO implements Serializable {
        private final Integer id;
        @NotNull
        private final LocalDate date;
        @NotNull
        private final LocalTime time;
        @Size(max = 45)
        @NotNull
        private final String location;
        @Size(max = 45)
        @NotNull
        private final String duration;
        @Size(max = 255)
        @NotNull
        private final String packingList;

        public TripInnerDTO(Integer id, LocalDate date, LocalTime time, String location, String duration, String packingList) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.location = location;
            this.duration = duration;
            this.packingList = packingList;
        }

        public TripInnerDTO(Trip trip) {
            this.id = trip.getId();
            this.date = trip.getDate();
            this.time = trip.getTime();
            this.location = trip.getLocation();
            this.duration = trip.getDuration();
            this.packingList = trip.getPackingList();
        }

        public Integer getId() {
            return id;
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getTime() {
            return time;
        }

        public String getLocation() {
            return location;
        }

        public String getDuration() {
            return duration;
        }

        public String getPackingList() {
            return packingList;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "date = " + date + ", " +
                    "time = " + time + ", " +
                    "location = " + location + ", " +
                    "duration = " + duration + ", " +
                    "packingList = " + packingList + ")";
        }
    }
}