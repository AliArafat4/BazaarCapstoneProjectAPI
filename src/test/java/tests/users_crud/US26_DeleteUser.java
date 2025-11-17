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

public class US26_DeleteUser extends BazaarStoresBaseUrl{


    @Test(priority = 0)
    public void deleteUserSuccessfully() {

        // Read user ID from json file
        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .delete("/users/" + id);

        response.then()
                .statusCode(200)
                .body("success", notNullValue())
                .body("success", equalTo("User deleted successfully!"));
    }

    @Test (priority = 1)
    public void deleteUserFailure() {

        // Read user ID from json file
        JsonNode IdPayload = ObjectMapperUtils.getJsonNode("users_data/userID");
        int id = IdPayload.get("id").asInt();

        Response response = given()
                .spec(adminSpec())
                .contentType("application/json")
                .delete("/users/" + id);

        response.then()
                .statusCode(500);
    }
}
