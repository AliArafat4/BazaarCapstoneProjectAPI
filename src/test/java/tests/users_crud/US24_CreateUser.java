package tests.users_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class US24_CreateUser extends BazaarStoresBaseUrl{

    @Test
    public void createUserSuccessfully() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("validData");

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");


        response
                .then()
                .statusCode(201)
                .body(
                        "user.name", equalTo(newName),
                        "user.email", equalTo(newEmail),
                        "user.id", notNullValue()
                );

        int userId = response.jsonPath().getInt("user.id");

        ObjectMapperUtils.saveUserId(userId);

    }


    @Test
    public void createMissingName() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("missingName");


        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.then().statusCode(422);


        response.then()
                .statusCode(422)
                .body("name", notNullValue())
                .body("name", instanceOf(List.class))
                .body("name[0]", equalTo("The name field is required."));



    }

    @Test
    public void createMissingEmail() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("missingEmail");


        String newName = "Name_" + new Random().nextInt(100000);


        ObjectMapperUtils.updateJsonNode(payload, "name", newName);


        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.then().statusCode(422);


        response.then()
                .statusCode(422)
                .body("email", notNullValue())
                .body("email", instanceOf(List.class))
                .body("email[0]", equalTo("The email field is required."));



    }


    @Test
    public void createMissingPassword() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("missingPassword");

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("password", notNullValue())
                .body("password", instanceOf(List.class))
                .body("password[0]", equalTo("The password field is required."));


    }

    @Test
    public void createMissingPasswordConfirm() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("missingPasswordConfirm");


        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("password", notNullValue())
                .body("password", instanceOf(List.class))
                .body("password[0]", equalTo("The password field confirmation does not match."));


    }

    @Test
    public void createMismatchPassword() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("passwordMismatch");

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("password", notNullValue())
                .body("password", instanceOf(List.class))
                .body("password[0]", equalTo("The password field confirmation does not match."));


    }


    @Test
    public void createInvalidEmail() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("invalidEmail");

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("email", notNullValue())
                .body("email", instanceOf(List.class))
                .body("email[0]", equalTo("The email field must be a valid email address."));


    }

    @Test
    public void createDuplicateEmail() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("duplicateEmail");

        String newName = "Name_" + new Random().nextInt(100000);


        ObjectMapperUtils.updateJsonNode(payload, "name", newName);


        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("email", notNullValue())
                .body("email", instanceOf(List.class))
                .body("email[0]", equalTo("The email has already been taken."));


    }


    @Test
    public void createShortPassword() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/CreateUserData")
                .get("shortPassword");

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);


        response
                .then()
                .statusCode(422)
                .body("password", notNullValue())
                .body("password", instanceOf(List.class))
                .body("password[0]", equalTo("The password field must be at least 6 characters."));


    }


}
