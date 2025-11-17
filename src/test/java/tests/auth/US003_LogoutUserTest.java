package tests.auth;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class US003_LogoutUserTest {

    private final String logoutEndpoint = "https://bazaarstores.com/api/logout";

    /** Helper to ACCEPT 302 as Unauthorized also */
    private void assertUnauthorized(int actualStatus) {
        Assert.assertTrue(
                actualStatus == 401 || actualStatus == 302,
                "Expected 401 or 302 for unauthorized, but got: " + actualStatus
        );
    }

    // 1) Successful Logout
    @Test
    public void logoutSuccessfully() {

        String token = BazaarStoresBaseUrl.getCustomerToken();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .post(logoutEndpoint);

        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("message"),
                "Successfully logged out");
    }

    // 2) Logout WITHOUT token → should return Unauthorized
    @Test
    public void logoutWithoutToken_Fail() {

        Response response = given()
                .post(logoutEndpoint);

        int status = response.statusCode();
        assertUnauthorized(status);
    }

    // 3) Logout WITH invalid token → should return Unauthorized
    @Test
    public void logoutWithInvalidToken_Fail() {

        Response response = given()
                .header("Authorization", "Bearer invalidtoken123")
                .post(logoutEndpoint);

        int status = response.statusCode();
        assertUnauthorized(status);
    }
}
