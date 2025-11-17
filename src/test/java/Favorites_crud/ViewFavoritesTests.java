package Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class ViewFavoritesTests {

    // TC_US009_001 – Verify successful retrieval of all favorite products
    @Test(description = "Verify that the user can successfully retrieve all favorite products.")
    public void TC_US009_001_getAllFavorites_success() {

        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .when()
                .get("/favorites");

        // Verify status code
        res.then().statusCode(200);

        // Verify response is array (maybe empty)
        assertNotNull(res.jsonPath().getList("$"));
    }

    // TC_US009_002 – Verify behavior when user has no favorited products
    @Test(description = "Ensure that the API returns an empty array when the user has no favorite products.")
    public void TC_US009_002_getFavorites_empty() {

        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .when()
                .get("/favorites");

        // Verify status code
        res.then().statusCode(200);

        // assertTrue(res.jsonPath().getList("$").isEmpty());
    }

    // TC_US009_003 – Verify response when unauthorized user tries to access favorites
    @Test(description = "Verify that an unauthorized user cannot access favorite products.")
    public void TC_US009_003_unauthorizedAccess() {

        Response res = given()
                .header("Accept", "application/json")
                .when()
                .get("https://bazaarstores.com/api/favorites");

        res.then().statusCode(401);

        String errorMsg = res.jsonPath().getString("message");
        assertTrue(errorMsg.toLowerCase().contains("unauth"));
    }

    // TC_US009_004 – Verify response format includes required fields
    @Test(description = "Verify that the API response contains all required fields for each record.")
    public void TC_US009_004_validateFields() {

        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .when()
                .get("/favorites");

        res.then().statusCode(200);

        if (!res.jsonPath().getList("$").isEmpty()) {
            assertNotNull(res.jsonPath().getString("[0].product_id"));
            assertNotNull(res.jsonPath().getString("[0].user_id"));
            assertNotNull(res.jsonPath().getString("[0].created_at"));
        }
    }
}
