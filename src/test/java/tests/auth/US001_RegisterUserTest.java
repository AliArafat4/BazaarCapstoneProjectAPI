package tests.auth;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class US001_RegisterUserTest extends BazaarStoresBaseUrl {

    // Successful Registration
    @Test
    public void registerUserSuccessfully() {

        JsonNode payloadNode = ObjectMapperUtils.getJsonNode("auth_data/registerData");

        Map<String, Object> payload = new ObjectMapper().convertValue(payloadNode, Map.class);
        String randomEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        payload.put("email", randomEmail);
        Response response =

                given()
                        .baseUri("https://bazaarstores.com/api")
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/register");

        response.then().statusCode(201);

        String message = response.jsonPath().getString("message");
        assertEquals(message, "User created successfully");
    }

    //  Duplicate Email
    @Test
    public void registerWithExistingEmail_Fail() {

        JsonNode payloadNode = ObjectMapperUtils.getJsonNode("auth_data/registerData");

        Map<String, Object> payload =
                new ObjectMapper().convertValue(payloadNode, Map.class);

        Response response =
                given()
                        .baseUri("https://bazaarstores.com/api")
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/register");


        response.then().statusCode(422);
    }

    // Missing Required Field
    @Test
    public void registerMissingField_Fail() {

        JsonNode payloadNode = ObjectMapperUtils.getJsonNode("auth_data/registerData");
        Map<String, Object> payload =
                new ObjectMapper().convertValue(payloadNode, Map.class);

        payload.remove("email");  // remove required field

        Response response =
                given()
                        .baseUri("https://bazaarstores.com/api")
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/register");


        response.then().statusCode(422); // NOT 400
    }

    // Invalid Email Format
    @Test
    public void registerInvalidEmailFormat_Fail() {

        JsonNode payloadNode = ObjectMapperUtils.getJsonNode("auth_data/registerData");

        ObjectMapperUtils.updateJsonNode(payloadNode, "email", "invalid_email");

        Response response =
                given()
                        .baseUri("https://bazaarstores.com/api")
                        .contentType(ContentType.JSON)
                        .body(payloadNode)
                        .when()
                        .post("/register");

        response.prettyPrint();
        response.then().statusCode(422);

        String error = response.jsonPath().getString("email[0]");
        assertEquals(error, "The email field must be a valid email address.");
    }

}
