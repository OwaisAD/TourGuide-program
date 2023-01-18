package rest;

import dtos.PersonDTO;
import entities.Person;
import entities.Trip;
import entities.User;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TripResourceTest extends ResourceTestEnvironment {
    private final String BASE_URL = "/trips/";

    @Test
    public void getAllTripsTestAsAUserTest() {
        User user = createAndPersistUser();

        Trip trip = createAndPersistTrip();

        login(user);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("$", hasItem(hasEntry("id", trip.getId())));
    }

    @Test
    public void getAllTripsTestAsAnAdminTest() {
        User admin = createAndPersistAdmin();

        Trip trip = createAndPersistTrip();

        login(admin);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("$", hasItem(hasEntry("id", trip.getId())));
    }


    @Test
    public void getAllTripsWhenUnAuthenticatedTest() {
        given()
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void addPersonToTrip() {
        User user = createAndPersistUser();
        Trip trip = createAndPersistTrip();
        PersonDTO personDTO = new PersonDTO(createAndPersistPerson());

        login(user);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(GSON.toJson(personDTO))
                .when()
                .post(BASE_URL + trip.getId() + "/person")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON);
    }

}
