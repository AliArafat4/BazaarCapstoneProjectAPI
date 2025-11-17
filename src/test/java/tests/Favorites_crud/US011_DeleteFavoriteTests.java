package tests.Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class US011_DeleteFavoriteTests {

    int favoriteId =211;

    @Test
    public void TC_US011_001_deleteFavorite_success() {
        given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .body("{\"product_id\": 211}")
                .post("/favorites/create");

        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .pathParam("favorite_id", favoriteId)
                .delete("/favorites/{favorite_id}")
                .then()
                .log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 200);
        assertEquals(res.jsonPath().getString("success"), "Favorite product deleted successfully!");

    }

    @Test
    public void TC_US011_002_deleteNonExistingFavorite() {
        given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .pathParam("favorite_id", favoriteId)
                .delete("/favorites/{favorite_id}");

        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .pathParam("favorite_id", favoriteId)
                .delete("/favorites/{favorite_id}")
                .then()
                .log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 404);
        assertEquals(res.jsonPath().getString("error"), "Favorite not found.");
    }

    @Test
    public void TC_US011_003_deleteFavorite_serverError() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .pathParam("favorite_id", "abc")
                .delete("/favorites/{favorite_id}")
                .then()
                .log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 500);
        assertEquals(res.jsonPath().getString("error"), "Favorite product deletion failed. Please try again.");
    }

    @Test
    public void TC_US011_004_deleteFavorite_noToken() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.spec())
                .pathParam("favorite_id", favoriteId)
                .delete("/favorites/{favorite_id}")
                .then()
                .log().body()
                .extract().response();

        int status = res.getStatusCode();
        assertTrue(status == 401 || status == 403, "Unexpected status code: " + status);

        String msg = res.getBody().asString().toLowerCase();
        assertTrue(msg.contains("unauth") || msg.contains("auth"), "Unexpected message: " + msg);
    }
}
