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

public class US25_UpdateUser extends BazaarStoresBaseUrl {


    @Test
    public void updateUserSuccessfully() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/updateData")
                                                                                        .get("validUpdate");

        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();


        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/"+id);

        response.then().statusCode(500);


    }

    @Test
    public void updateInvalidId() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/updateData")
                .get("validUpdate");


        int id = 9999999;

        String newName = "Name_" + new Random().nextInt(100000);
        String newEmail = "user_" + new Random().nextInt(100000) + "@test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/"+id);

        response.then().statusCode(500);


    }

    @Test
    public void updateInvalidEmail() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/updateData")
                .get("invalidEmail");

        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();

        String newName = "Name_" + new Random().nextInt(100000);


        ObjectMapperUtils.updateJsonNode(payload, "name", newName);


        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/"+id);


        response
                .then()
                .statusCode(422)
                .body("errors.email", notNullValue())
                .body("errors.email", instanceOf(java.util.List.class))
                .body("errors.email[0]", equalTo("The email field must be a valid email address."));

    }


    @Test
    public void updateDuplicatedEmail() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/updateData")
                .get("duplicatedEmail");

        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();

        String newName = "Name_" + new Random().nextInt(100000);
       // String newEmail = "user_" + new Random().nextInt(100000) + "test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
       // ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/"+id);


        response
                .then()
                .statusCode(422)
                .body("errors.email", notNullValue())
                .body("errors.email", instanceOf(java.util.List.class))
                .body("errors.email[0]", equalTo("The email has already been taken."));

    }


    @Test
    public void updateMismatchedPassword() {

        JsonNode payload = ObjectMapperUtils.getJsonNode("users_data/updateData")
                .get("mismatchedPassword");

        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();

        String newName = "Name_" + new Random().nextInt(100000);
         String newEmail = "user_" + new Random().nextInt(100000) + "test.com";

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
         ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/"+id);


        response
                .then()
                .statusCode(422)
                .body("errors.password", notNullValue())
                .body("errors.password", instanceOf(java.util.List.class))
                .body("errors.password[0]", equalTo("The password field confirmation does not match."));

    }




}
