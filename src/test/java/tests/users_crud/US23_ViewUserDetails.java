package tests.users_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.util.regex.Pattern;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

public class US23_ViewUserDetails extends BazaarStoresBaseUrl {

    RequestSpecification spec;

    @BeforeMethod
    public void setUp() {
        spec = adminSpec();
    }

    @Test(priority = 0)
    public void RetrieveUserSuccessfully() {

        // Load id from JSON file
        JsonNode payload = ObjectMapperUtils.getJsonNode("/users_data/userID");
        int id = payload.get("id").asInt();

        spec.pathParam("id", id);

        // Send request
        Response response = given(spec).get("/users/{id}");
        response.prettyPrint();

        // Validate response
        response.then()
                .statusCode(200)
                .contentType("application/json");

        JsonNode json = response.as(JsonNode.class);

        // Field presence
        assert json.has("id");
        assert json.has("name");
        assert json.has("email");
        assert json.has("role");

        // Types
        assert json.get("id").isInt();
        assert json.get("name").isTextual();
        assert json.get("email").isTextual();
        assert json.get("role").isTextual();

        // Non-empty
        assert json.get("name").asText().length() > 0;
        assert json.get("email").asText().length() > 0;
        assert json.get("role").asText().length() > 0;

        // Email format
        String email = json.get("email").asText();
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        assert Pattern.matches(emailRegex, email) : "Invalid email format: " + email;
    }

    @Test
    public void RetrieveUserFailure() {

        int invalidId = 9999999;

        Response response = given(spec).get("/users/" + invalidId);
        // response.prettyPrint();


        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("error", notNullValue())
                .body("error", equalTo("User not found"));


    }
}


