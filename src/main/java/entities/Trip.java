package entities;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trip")
public class Trip implements entities.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Size(max = 45)
    @NotNull
    @Column(name = "location", nullable = false, length = 45)
    private String location;

    @Size(max = 45)
    @NotNull
    @Column(name = "duration", nullable = false, length = 45)
    private String duration;

    @Size(max = 255)
    @NotNull
    @Column(name = "packingList", nullable = false)
    private String packingList;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @ManyToMany
    @JoinTable(name = "person_has_trip",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> people = new LinkedHashSet<>();

    public Trip(LocalDate date, LocalTime time, String location, String duration, String packingList, Guide guide) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.duration = duration;
        this.packingList = packingList;
        this.guide = guide;
    }

    public Trip() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return getId().equals(trip.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                ", packingList='" + packingList + '\'' +
                ", guide=" + guide +
                '}';
    }
}