package tests.products_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;

public class US012BrowsAllProducts extends BazaarStoresBaseUrl {


    RequestSpecification spec;
    @BeforeMethod
    public void setUp(){
        spec = StoreManagerSpec();
    }

    @Test
    public void VerifySuccessfulRetrieveOfAllProducts() {

        Response response = given(spec).get("/products");
//        response.prettyPrint();
        response.then()
                .statusCode(200)
                .body("", Matchers.notNullValue());
    }

    @Test
    public void VerifyFailGetProductsByInvalidToken(){

        RequestSpecification sp = new RequestSpecBuilder()
                .setBaseUri("https://bazaarstores.com/api")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer ")
                .setContentType(ContentType.JSON)
                .build();

        Response response = given(sp).get("/products");
//        response.prettyPrint();
        response.then()
                .statusCode(401)
                .body("message", Matchers.is("Unauthenticated."));
    }

}