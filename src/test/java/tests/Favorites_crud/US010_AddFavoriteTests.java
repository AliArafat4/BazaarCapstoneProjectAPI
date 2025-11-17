package tests.Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;


import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class US010_AddFavoriteTests extends BazaarStoresBaseUrl {


    @Test
    public void TC_US010_001_addProductToFavorites_success() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/add_favorite");
        Response res = given()
                .spec(customerSpec())
                .body(payload)
                .post("/favorites/create")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 200);
        assertEquals(res.jsonPath().getString("success"), "Product added favorites successfully!");
    }

    @Test
    public void TC_US010_002_addProductAlreadyInFavorites() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/add_favorite");
        Response res = given()
                .spec(customerSpec())
                .body(payload)
                .post("/favorites/create")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 400);
        assertEquals(res.jsonPath().getString("error"), "Product is already in favorites.");
    }

    @Test
    public void TC_US010_003_missingProductId() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/add_favorite_missing");
        Response res = given()
                .spec(customerSpec())
                .body(payload)
                .post("/favorites/create")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 500);
        assertEquals(res.jsonPath().getString("error"), "Product added favorites failed!");
    }

    @Test
    public void TC_US010_004_noToken() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("favorites_data/add_favorite");
        Response res = given()
                .spec(spec()) // no customer token
                .body(payload)
                .post("/favorites/create")
                .then().extract().response();

        int status = res.getStatusCode();
        assertTrue(status == 401 || status == 403, "Unexpected status code: " + status);

        String message = res.jsonPath().getString("message");
        System.out.println("Response message: " + message);

        String msgLower = message.toLowerCase();
        assertTrue(msgLower.contains("unauth") || msgLower.contains("auth"), "Unexpected message: " + message);
    }
}
