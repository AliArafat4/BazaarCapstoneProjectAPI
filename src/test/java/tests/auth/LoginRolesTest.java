package tests.auth;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginRolesTest {

    String endpoint = "https://bazaarstores.com/api/login";

    @Test
    public void customerLoginTest() {

        String credentials = """
        {
            "email": "customer@sda.com",
            "password": "Password.12345"
        }
        """;

        Response response = given()
                .spec(BazaarStoresBaseUrl.spec())
                .body(credentials)
                .post("/login");

        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("status"), "success");
    }

    @Test
    public void storeManagerLoginTest() {

        String credentials = """
        {
            "email": "storemanager@sda.com",
            "password": "Password.12345"
        }
        """;

        Response response = given()
                .spec(BazaarStoresBaseUrl.spec())
                .body(credentials)
                .post("/login");

        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("status"), "success");
    }

    @Test
    public void adminLoginTest() {

        String credentials = """
        {
            "email": "admin@sda.com",
            "password": "Password.12345"
        }
        """;

        Response response = given()
                .spec(BazaarStoresBaseUrl.spec())
                .body(credentials)
                .post("/login");

        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("status"), "success");
        Assert.assertNotNull(response.jsonPath().getString("authorisation.token"));
    }
}
