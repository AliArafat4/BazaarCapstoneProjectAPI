package tests.Favorites_crud;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class US09_AddFavoriteTests {

    int productId = 211;

    @BeforeMethod
    public void cleanFavoriteBeforeAdd() {
        given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .pathParam("product_id", productId)
                .delete("/favorites/delete/{product_id}");
    }

    @Test
    public void TC_US010_001_addProductToFavorites_success() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .body("{\"product_id\": " + productId + "}")
                .post("/favorites/create")
                .then().log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 200);
        assertEquals(res.jsonPath().getString("success"), "Product added favorites successfully!");
    }


    @Test
    public void TC_US010_002_addProductAlreadyInFavorites() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .body("{\"product_id\": " + productId + "}")
                .when()
                .post("/favorites/create")
                .then()
                .log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 400);
        assertEquals(res.jsonPath().getString("error"), "Product is already in favorites.");
    }

    @Test
    public void TC_US010_003_missingProductId() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.customerSpec())
                .body("{}")
                .when()
                .post("/favorites/create")
                .then()
                .log().body()
                .extract().response();

        assertEquals(res.getStatusCode(), 500);
        assertEquals(res.jsonPath().getString("error"), "Product added favorites failed!");
    }

    @Test
    public void TC_US010_004_noToken() {
        Response res = given()
                .spec(BazaarStoresBaseUrl.spec())
                .body("{\"product_id\": " + productId + "}")
                .when()
                .post("/favorites/create")
                .then()
                .extract().response();

        int status = res.getStatusCode();
        assertTrue(status == 401 || status == 403, "Unexpected status code: " + status);


        String message = res.jsonPath().getString("message");
        System.out.println("Response message: " + message);

        // Optional: assertion
        String msgLower = message.toLowerCase();
        assertTrue(msgLower.contains("unauth") || msgLower.contains("auth"), "Unexpected message: " + message);
    }

}
