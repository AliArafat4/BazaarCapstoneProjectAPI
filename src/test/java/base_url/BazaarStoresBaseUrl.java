package base_url;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class BazaarStoresBaseUrl {

    protected RequestSpecification spec;
    private static String baseUrl = "https://bazaarstores.com/api";

    @BeforeMethod//Before each test method, this will work and initialize the spec object.
    public void setStoreManagerSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + getStoreManagerToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    String getStoreManagerToken() {
        String credentials = """
                {
                    "email" : "storemanager@sda.com",
                    "password" : "Password.12345"
                }""";

        return given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(baseUrl + "/login")
                .jsonPath()
                .getString("authorisation.token");
    }

    public void setAdminSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + getAdminToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    String getAdminToken() {
        String credentials = """
                {
                    "email" : "admin@sda.com",
                    "password" : "Password.12345"
                }""";

        return given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(baseUrl + "/login")
                .jsonPath()
                .getString("authorisation.token");
    }

    public void setCustomerSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + getCustomerToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    String getCustomerToken() {
        String credentials = """
                {
                    "email" : "customer@sda.com",
                    "password" : "Password.12345"
                }""";

        return given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(baseUrl + "/login")
                .jsonPath()
                .getString("authorisation.token");
    }

}