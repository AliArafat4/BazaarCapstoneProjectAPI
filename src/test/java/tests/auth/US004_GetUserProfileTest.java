package tests.auth;

import base_url.BazaarStoresBaseUrl;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class US004_GetUserProfileTest {

    private final String profileEndpoint = "https://bazaarstores.com/api/me";

    // 1) Successful profile retrieval
    @Test
    public void getProfileSuccessfully() {

        String token = BazaarStoresBaseUrl.getCustomerToken();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .get(profileEndpoint);

        response.then().statusCode(200);

        Assert.assertNotNull(response.jsonPath().getString("id"));
        Assert.assertNotNull(response.jsonPath().getString("name"));
        Assert.assertNotNull(response.jsonPath().getString("email"));
        Assert.assertNotNull(response.jsonPath().getString("role"));
    }

    // 2) Profile request WITHOUT token → should return 401 Unauthorized
    @Test
    public void getProfileWithoutToken_Fail() {
        int status = RestAssured
                .given()
                .accept("application/json")
                .when()
                .get(profileEndpoint)
                .then()
                .extract()
                .statusCode();

        Assert.assertEquals(status, 401, "Expected 401 but got: " + status);
    }


    // 3) Profile request WITH invalid token → should return 401 Unauthorized
    @Test
    public void getProfileWithInvalidToken_Fail() {
        int status = RestAssured
                .given()
                .accept("application/json")
                .header("Authorization", "Bearer INVALID_TOKEN")
                .when()
                .get(profileEndpoint)
                .then()
                .extract()
                .statusCode();

        Assert.assertEquals(status, 401, "Expected 401 but got: " + status);
    }

}
