package dtos;

import entities.Guide;
import entities.Trip;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link entities.Guide} entity
 */
public class GuideDTO implements Serializable {
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
    private final List<TripInnerDTO> trips = new ArrayList<>();

    public GuideDTO(Integer id, String gender, String birthYear, String profile, String image) {
        this.id = id;
        this.gender = gender;
        this.birthYear = birthYear;
        this.profile = profile;
        this.image = image;
    }

    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.gender = guide.getGender();
        this.birthYear = guide.getBirthYear();
        this.profile = guide.getProfile();
        this.image = guide.getImage();
        guide.getTrips().forEach(trip -> {
            trips.add(new TripInnerDTO(trip));
        });
    }

    public static List<GuideDTO> getDTOs(List<Guide> allGuides) {
        List<GuideDTO> guideDTOList = new ArrayList<>();
        allGuides.forEach(guide -> {
            guideDTOList.add(new GuideDTO(guide));
        });
        return guideDTOList;
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

    public List<TripInnerDTO> getTrips() {
        return trips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuideDTO)) return false;
        GuideDTO guideDTO = (GuideDTO) o;
        return getId().equals(guideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "gender = " + gender + ", " +
                "birthYear = " + birthYear + ", " +
                "profile = " + profile + ", " +
                "image = " + image + ", " +
                "trips = " + trips + ")";
    }

    /**
     * A DTO for the {@link entities.Trip} entity
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