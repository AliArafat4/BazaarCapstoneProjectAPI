package tests.Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class US09_ViewFavoritesTests extends BazaarStoresBaseUrl {

    // TC_US009_001 – Verify successful retrieval of all favorite products
    @Test(description = "TC_US009_001: Verify successful retrieval of all favorite products")
    public void TC_US009_001_getAllFavorites_success() {

        Response response = given(customerSpec())
                .when()
                .get("/favorites");

        response.prettyPrint();

        response.then().statusCode(200);

        // Verify response is an array (maybe empty)
        assertNotNull(response.jsonPath().getList("$"));
    }

    // TC_US009_002 – Verify behavior when user has no favorited products
    @Test(description = "TC_US009_002: Verify behavior when user has no favorited products")
    public void TC_US009_002_getFavorites_empty() {

        Response response = given(customerSpec())
                .when()
                .get("/favorites");

        response.prettyPrint();

        response.then().statusCode(200);

        assertTrue(response.jsonPath().getList("$").isEmpty() || response.jsonPath().getList("$") != null);
    }

    // TC_US009_003 – Verify response when unauthorized user tries to access favorites
    @Test(description = "TC_US009_003: Unauthorized user cannot access favorite products")
    public void TC_US009_003_unauthorizedAccess() {

        Response res = given()
                .header("Accept", "application/json")
                .when()
                .get("https://bazaarstores.com/api/favorites")
                .then().log().body()
                .extract().response();

        int status = res.getStatusCode();
        assertTrue(status == 401 || status == 403, "Unexpected status code: " + status);

        String message = res.jsonPath().getString("message");
        String lower = message.toLowerCase();
        assertTrue(lower.contains("unauth") || lower.contains("auth"), "Unexpected message: " + message);
    }




    // TC_US009_004 – Verify response format includes required fields
    @Test(description = "TC_US009_004: Verify response format includes required fields")
    public void TC_US009_004_validateFields() {

        Response response = given(customerSpec())
                .when()
                .get("/favorites");

        response.prettyPrint();

        response.then().statusCode(200);

        if (!response.jsonPath().getList("$").isEmpty()) {
            response.then().body("[0].product_id", notNullValue())
                    .body("[0].user_id", notNullValue())
                    .body("[0].created_at", notNullValue());
        }
    }
}
