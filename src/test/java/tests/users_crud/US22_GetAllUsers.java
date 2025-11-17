package tests.users_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class US22_GetAllUsers {



    @Test
    public void US22_TC01_getAllUsersSuccess() throws JsonProcessingException {

        Response response = given()
                .spec(BazaarStoresBaseUrl.adminSpec())
                .when()
                .get("/users");

        response.then().statusCode(200);

        // Convert response to JsonNode using your utility
        JsonNode root = new ObjectMapper().readTree(response.asString());

        // 1. Ensure it's a JSON array
        assert root.isArray();

        // 2. Ensure list > 0
        assert root.size() > 0;

        // 3. Validate each user object
        for (JsonNode user : root) {

            assert user.get("id").isInt();
            assert user.get("name").isTextual();
            assert user.get("email").isTextual();
            assert user.get("role").isTextual();
            assert user.get("created_at").isTextual();
            assert user.get("updated_at").isTextual();

            // Email format check
            assert user.get("email").asText().contains("@");

            // Date format optional check
            assert isValidDate(user.get("created_at").asText());
            assert isValidDate(user.get("updated_at").asText());
        }
    }
    private boolean isValidDate(String date) {
        try {
            return !Double.isNaN((double) java.sql.Timestamp.valueOf(date.replace("T", " ").replace("Z", "")).getTime());
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void US22_TC02_missingToken() throws JsonProcessingException {

        Response response = given()
                .baseUri("https://bazaarstores.com/api")
                .header("Accept", "application/json")
                .when()
                .get("/users");

        response.then().statusCode(401);

        // Parse JSON error
        JsonNode json = new ObjectMapper().readTree(response.asString());

        // Assert message exists
        assert json.toString().toLowerCase().contains("unauth")
                || json.toString().toLowerCase().contains("token")
                || json.toString().toLowerCase().contains("forbidden");
    }

    @Test
    public void US22_TC03_invalidToken() throws JsonProcessingException {

        Response response = given()
                .baseUri("https://bazaarstores.com/api")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer INVALID_1234")
                .when()
                .get("/users");

        response.then().statusCode(401);

        JsonNode json = new ObjectMapper().readTree(response.asString());

        assert json.toString().toLowerCase().contains("unauth")
                || json.toString().toLowerCase().contains("invalid")
                || json.toString().toLowerCase().contains("token");
    }


}
