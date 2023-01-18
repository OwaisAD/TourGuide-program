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
    private static PersonFacade personFacade;

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
    public void removePersonFromTripTest() {
        Trip trip = createAndPersistTrip();
        Person person = createPerson();

        TripDTO updatedTrip = facade.createAndAddPersonToTrip(person, trip);
        assertDatabaseHasEntity(trip, trip.getId());
        assertDatabaseHasEntity(person, person.getId());

        assertEquals(updatedTrip.getPeople().size(), 1);
        assertEquals(updatedTrip.getPeople().get(0).getId(), person.getId());


        trip = (Trip) update(trip);
        person = (Person) update(person);

        TripDTO updatedAgainTrip = facade.removePersonFromTrip(trip, person);

        assertEquals(updatedAgainTrip.getPeople().size(), 0);
    }

    @Test
    public void getTripByIdTest() {
        TripDTO actual = new TripDTO(createAndPersistTrip());

        TripDTO expected = new TripDTO(facade.getTripById(actual.getId()));

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void removeTripByIdTest() {
        Trip actual = createAndPersistTrip();

        assertDatabaseHasEntity(actual, actual.getId());
        assertEquals(1, facade.getAllTrips().size());

        List<TripDTO> allTripsUpdated = facade.removeTripById(actual.getId());

        assertDatabaseDoesNotHaveEntity(actual, actual.getId());
        assertEquals(0, facade.getAllTrips().size());
    }


}
