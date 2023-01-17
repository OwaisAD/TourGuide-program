package rest;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserResourceTest extends ResourceTestEnvironment {
    private final String BASE_URL = "/users/";

    @Test
    void createUserTest() {
        UserDTO userDTO = createUserDTO();

        int id = given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201.getStatusCode())
                .contentType(ContentType.JSON)
                .body("username", equalTo(userDTO.getUsername()))
                .body("age", equalTo(userDTO.getAge()))
                .body("id", notNullValue())
                .body("roles",hasSize(1))
                .body("roles",hasItem("user"))
                .extract().path("id");

        assertDatabaseHasEntity(new User(), id);
    }

    @Test
    void createUserInvalidUsernameTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setUsername(faker.letterify("??"))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

    @Test
    void createUserWithUsernameThatAlreadyExistTest() {
        User existingUser = createAndPersistUser();
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setUsername(existingUser.getUsername())
                .build();

        given()
                .header("Content-type",ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

    @Test
    void createUserInvalidPasswordTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setPassword(faker.letterify("??"))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    void createUserIllegalAgeTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setAge(faker.random().nextInt(121,300))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    void getUserTest() {
        User user = createAndPersistUser();

        int id = user.getId();
        login(user);
        given()
            .header("Content-type", ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get(BASE_URL+"me")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .contentType(ContentType.JSON)
            .body("username", equalTo(user.getUsername()))
            .body("age", equalTo(user.getAge()))
            .body("id", equalTo(id))
            .body("roles",hasSize(1))
            .body("roles",hasItem("user"));
    }

    @Test
    void getAllUsersTest() {
        User admin = createAndPersistUser();
        Role role = new Role("admin");
        admin.addRole(role);
        admin = (User) update(admin);
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
                .body("$",hasItem(hasEntry("username",admin.getUsername())));
    }

    @Test
    void getAllUsersWhenUnauthenticatedTest() {
        given()
                .header("Content-type", ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    void getAllUsersWhenUnauthorizedTest() {
        User user = createAndPersistUser();
        login(user);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED_401.getStatusCode());
    }

    @Test
    public void putUserTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(faker.letterify("????"))
                .setEmail(faker.bothify("??####@hotmail.com"))
                .setAge(faker.random().nextInt(18,80))
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("username", equalTo(userDTO.getUsername()))
                .body("email", equalTo(userDTO.getEmail()))
                .body("age", equalTo(userDTO.getAge()));

        assertDatabaseHasEntityWith(user,"username",userDTO.getUsername());
        assertDatabaseHasEntityWith(user,"email",userDTO.getEmail());
        assertDatabaseHasEntityWith(user,"age",userDTO.getAge());
    }

    @Test
    public void putOnlyUsernameTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(faker.letterify("????"))
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("username", equalTo(userDTO.getUsername()))
                .body("email", equalTo(user.getEmail()))
                .body("age", equalTo(user.getAge()));

        assertDatabaseHasEntityWith(user,"username",userDTO.getUsername());
        assertDatabaseHasEntityWith(user,"email",user.getEmail());
        assertDatabaseHasEntityWith(user,"age",user.getAge());
    }

    @Test
    public void putOnlyEmailTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setEmail(faker.bothify("????##@hotmail.com"))
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("email", equalTo(userDTO.getEmail()))
                .body("username", equalTo(user.getUsername()))
                .body("age", equalTo(user.getAge()));

        assertDatabaseHasEntityWith(user,"username",user.getUsername());
        assertDatabaseHasEntityWith(user,"email",userDTO.getEmail());
        assertDatabaseHasEntityWith(user,"age",user.getAge());
    }

    @Test
    public void putUsernameAlreadyInUseTest() {
        User userA = createAndPersistUser();
        User userB = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(userB.getUsername())
                .setEmail(userA.getEmail())
                .setAge(userA.getAge())
                .build();
        login(userA);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }

    @Test
    public void putEmailAlreadyInUseTest() {
        User userA = createAndPersistUser();
        User userB = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(userA.getUsername())
                .setEmail(userB.getEmail())
                .setAge(userA.getAge())
                .build();

        login(userA);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }

    @Test
    public void putIllegalMinimumAgeTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setAge(faker.random().nextInt(0,12))
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }

    @Test
    public void putIllegalMaximumAgeTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setAge(121)
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }

    @Test
    public void putInvalidUsernameTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(faker.bothify("?#"))
                .setEmail(user.getEmail())
                .setAge(user.getAge())
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }

    @Test
    public void putInvalidEmailTest() {
        User user = createAndPersistUser();
        UserDTO userDTO = new UserDTO.Builder()
                .setUsername(user.getUsername())
                .setEmail(faker.bothify("??##?#"))
                .setAge(user.getAge())
                .build();

        login(user);

        given()
                .header("Content-Type",ContentType.JSON)
                .header("x-access-token",securityToken)
                .body(GSON.toJson(userDTO))
                .put(BASE_URL+"me/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode());
    }
}
