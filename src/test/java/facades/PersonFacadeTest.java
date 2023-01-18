package facades;

import TestEnvironment.TestEnvironment;
import dtos.PersonDTO;
import dtos.TripDTO;
import entities.Person;
import entities.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonFacadeTest extends TestEnvironment {

    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = PersonFacade.getFacade(emf);
    }

    @Test
    public void getPersonByIdTest() {
        PersonDTO expected = new PersonDTO(createAndPersistPerson());

        PersonDTO actual = new PersonDTO(facade.getPersonById(expected.getId()));

        assertEquals(expected.getId(), actual.getId());
    }



}
