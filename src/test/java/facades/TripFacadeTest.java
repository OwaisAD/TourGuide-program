package facades;

import TestEnvironment.TestEnvironment;
import dtos.TripDTO;
import entities.Person;
import entities.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripFacadeTest extends TestEnvironment {

    private static TripFacade facade;

    public TripFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = TripFacade.getFacade(emf);
    }

    @Test
    public void getAllTripsTest() {
        TripDTO tripDTOA = new TripDTO(createAndPersistTrip());
        TripDTO tripDTOB = new TripDTO(createAndPersistTrip());

        List<TripDTO> actual = facade.getAllTrips();
        assertEquals(2, actual.size());

        assertTrue(actual.contains(tripDTOA));
        assertTrue(actual.contains(tripDTOB));
    }

    @Test
    public void getAllTripsWhenNoTripsTest() {
        List<TripDTO> actual = facade.getAllTrips();
        assertEquals(0, actual.size());
    }

    @Test
    public void addPersonToTripTest() {
        Trip trip = createAndPersistTrip();
        Person person = createPerson();

        TripDTO updatedTrip = facade.createAndAddPersonToTrip(person, trip);

        assertDatabaseHasEntity(trip, trip.getId());
        assertDatabaseHasEntity(person, person.getId());

        assertEquals(updatedTrip.getPeople().size(), 1);
        assertEquals(updatedTrip.getPeople().get(0).getId(), person.getId());
    }

    @Test
    public void getTripByIdTest() {
        Trip actual = createAndPersistTrip();

        Trip expected = facade.getTripById(actual.getId());

        assertEquals(expected, actual);
    }



}
