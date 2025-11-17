package tests.Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class US011_DeleteFavoriteTests extends BazaarStoresBaseUrl {

    @Test
    public void TC_US011_001_deleteFavorite_success() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/delete_favorite");

        given()
                .spec(customerSpec())
                .body(payload)
                .post("/favorites/create");

        Response res = given()
                .spec(customerSpec())
                .pathParam("favorite_id", payload.get("product_id").asInt())
                .delete("/favorites/{favorite_id}")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 200);
        assertEquals(res.jsonPath().getString("success"), "Favorite product deleted successfully!");
    }

    @Test
    public void TC_US011_002_deleteNonExistingFavorite() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/delete_favorite");

        Response res = given()
                .spec(customerSpec())
                .pathParam("favorite_id", payload.get("product_id").asInt())
                .delete("/favorites/{favorite_id}")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 404);
        assertEquals(res.jsonPath().getString("error"), "Favorite not found.");
    }

    @Test
    public void TC_US011_003_deleteFavorite_serverError() {
        Response res = given()
                .spec(customerSpec())
                .pathParam("favorite_id", "abc")
                .delete("/favorites/{favorite_id}")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 500);
        assertEquals(res.jsonPath().getString("error"), "Favorite product deletion failed. Please try again.");
    }

    @Test
    public void TC_US011_004_deleteFavorite_noToken() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/delete_favorite");

        Response res = given()
                .spec(spec())
                .pathParam("favorite_id", payload.get("product_id").asInt())
                .delete("/favorites/{favorite_id}")
                .then().log().body()
                .extract().response();

        int status = res.getStatusCode();
        assertTrue(status == 401 || status == 403, "Unexpected status code: " + status);

        String message = res.jsonPath().getString("message");
        String msgLower = message.toLowerCase();
        assertTrue(msgLower.contains("unauth") || msgLower.contains("auth"), "Unexpected message: " + message);
    }
}
