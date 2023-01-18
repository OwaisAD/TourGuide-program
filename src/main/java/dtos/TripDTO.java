package dtos;

import entities.Guide;
import entities.Person;
import entities.Trip;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A DTO for the {@link entities.Trip} entity
 */
public class TripDTO implements Serializable {
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
    @NotNull
    private final GuideInnerDTO guide;
    private final List<PersonInnerDTO> people = new ArrayList<>();

    public TripDTO(Integer id, LocalDate date, LocalTime time, String location, String duration, String packingList, GuideInnerDTO guide) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.duration = duration;
        this.packingList = packingList;
        this.guide = guide;
    }

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.date = trip.getDate();
        this.time = trip.getTime();
        this.location = trip.getLocation();
        this.duration = trip.getDuration();
        this.packingList = trip.getPackingList();
        this.guide = new GuideInnerDTO(trip.getGuide());
        trip.getPeople().forEach(person -> {
            people.add(new PersonInnerDTO(person));
        });
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

    public GuideInnerDTO getGuide() {
        return guide;
    }

    public List<PersonInnerDTO> getPeople() {
        return people;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "date = " + date + ", " +
                "time = " + time + ", " +
                "location = " + location + ", " +
                "duration = " + duration + ", " +
                "packingList = " + packingList + ", " +
                "guide = " + guide + ", " +
                "people = " + people + ")";
    }

    /**
     * A DTO for the {@link entities.Guide} entity
     */
    public static class GuideInnerDTO implements Serializable {
        private final Integer id;
        @Size(max = 45)
        @NotNull
        private final String gender;
        @Size(max = 4)
        @NotNull
        private final String birthYear;
        @Size(max = 45)
        @NotNull
        private final String profile;
        @Size(max = 2048)
        @NotNull
        private final String image;

        public GuideInnerDTO(Integer id, String gender, String birthYear, String profile, String image) {
            this.id = id;
            this.gender = gender;
            this.birthYear = birthYear;
            this.profile = profile;
            this.image = image;
        }

        public GuideInnerDTO(Guide guide) {
            this.id = guide.getId();
            this.gender = guide.getGender();
            this.birthYear = guide.getBirthYear();
            this.profile = guide.getProfile();
            this.image = guide.getImage();
        }

        public Integer getId() {
            return id;
        }

        public String getGender() {
            return gender;
        }

        public String getBirthYear() {
            return birthYear;
        }

        public String getProfile() {
            return profile;
        }

        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "gender = " + gender + ", " +
                    "birthYear = " + birthYear + ", " +
                    "profile = " + profile + ", " +
                    "image = " + image + ")";
        }
    }

    /**
     * A DTO for the {@link entities.Person} entity
     */
    public static class PersonInnerDTO implements Serializable {
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

        public PersonInnerDTO(Integer id, String address, String email, Integer birthYear, String gender) {
            this.id = id;
            this.address = address;
            this.email = email;
            this.birthYear = birthYear;
            this.gender = gender;
        }

        public PersonInnerDTO(Person person) {
            this.id = person.getId();
            this.address = person.getAddress();
            this.email = person.getEmail();
            this.birthYear = person.getBirthYear();
            this.gender = person.getGender();
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

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "address = " + address + ", " +
                    "email = " + email + ", " +
                    "birthYear = " + birthYear + ", " +
                    "gender = " + gender + ")";
        }
    }
}