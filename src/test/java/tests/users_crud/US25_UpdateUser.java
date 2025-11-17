package tests.users_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class US25_UpdateUser extends BazaarStoresBaseUrl {



    public String randomString() {
        return "Name_" + new Random().nextInt(100000);
    }

    public String randomEmail() {
        return "user_" + new Random().nextInt(100000) + "@test.com";
    }

    RequestSpecification spec;

    @BeforeMethod
    public void setUp() {
        spec = adminSpec();
    }

    @Test
    public void updateUserSuccess() {

        // Load template JSON
        JsonNode payload = ObjectMapperUtils.getJsonNode("userUpdateValid");

        // Generate random name + email
        String newName = randomString();
        String newEmail = randomEmail();

        ObjectMapperUtils.updateJsonNode(payload, "name", newName);
        ObjectMapperUtils.updateJsonNode(payload, "email", newEmail);

        // Get ID
        int id = payload.get("id").asInt();
        spec.pathParam("id", id);

        Response response = given(spec)
                .contentType("application/json")
                .body(payload.toString())
                .put("/users/{id}");

        response.then().statusCode(200);

        // Body validations
        response.then()
                .body("data.name", equalTo(newName))
                .body("data.email", equalTo(newEmail));
    }

}
