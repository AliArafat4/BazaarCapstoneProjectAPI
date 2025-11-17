package tests.auth;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class US002_LoginUserTest {

    String endpoint = "https://bazaarstores.com/api/login";


    private Map<String, Object> convert(JsonNode node) {
        return new ObjectMapper().convertValue(node, Map.class);
    }

    //  SUCCESS
    @Test
    public void loginUserSuccessfully() {

        JsonNode data = ObjectMapperUtils
                .getJsonNode("auth_data/loginData")
                .get("validLogin");

        Map<String, Object> payload = convert(data);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint);

        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("status"), "success");
    }

    //  WRONG EMAIL
    @Test
    public void loginWrongEmail_Fail() {

        JsonNode data = ObjectMapperUtils
                .getJsonNode("auth_data/loginData")
                .get("wrongEmail");

        Map<String, Object> payload = convert(data);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint);

        response.prettyPrint();

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid credentials");
    }

    //  WRONG PASSWORD
    @Test
    public void loginWrongPassword_Fail() {

        JsonNode data = ObjectMapperUtils
                .getJsonNode("auth_data/loginData")
                .get("wrongPassword");

        Map<String, Object> payload = convert(data);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid credentials");
    }

    //  INVALID EMAIL FORMAT
    @Test
    public void loginInvalidEmailFormat_Fail() {

        JsonNode data = ObjectMapperUtils
                .getJsonNode("auth_data/loginData")
                .get("invalidEmailFormat");

        Map<String, Object> payload = convert(data);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid credentials");
    }

    //  MISSING FIELD
    @Test
    public void loginMissingField_Fail() {

        JsonNode data = ObjectMapperUtils
                .getJsonNode("auth_data/loginData")
                .get("missingEmail");

        Map<String, Object> payload = convert(data);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid credentials");
    }
}
