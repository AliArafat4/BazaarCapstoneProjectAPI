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

public class US013ViewProductDetails extends BazaarStoresBaseUrl {

    RequestSpecification spec;
    @BeforeMethod
    public void setUp(){
        spec = StoreManagerSpec();
    }

    @Test
    public void VerifySuccessfulRetrieveOfSpecificProduct() {
        SetProductIDInJsonFile();

        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/productID");

        int id = payload.get("id").asInt();
        spec.pathParam("id",id);

        Response response = given(spec).get("/products/{id}");
//        response.prettyPrint();
        response.then()
                .statusCode(200)
                .body("id", Matchers.equalTo(id));
    }



    @Test
    public void VerifyFailGetProductByInvalidId(){

        Response response = given(spec).get("/products/" + "0000000000000000");
//        response.prettyPrint();
        response.then()
                .statusCode(404)
                .body("error", Matchers.is("Product not found"));

    }

    @Test
    public void VerifyFailOnInvalidToken() {

        RequestSpecification sp = new RequestSpecBuilder()
                .setBaseUri("https://bazaarstores.com/api")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer ")
                .setContentType(ContentType.JSON)
                .build();

        Response response = given(sp).get("/products/0000000000000000");

//        response.prettyPrint();
        response.then()
                .statusCode(401)
                .body("message", Matchers.is("Unauthenticated."));
    }

    @Test
    public void VerifyFailGetProductById_InvalidToken(){
        SetProductIDInJsonFile();

        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/productID");

        int id = payload.get("id").asInt();

        RequestSpecification sp = new RequestSpecBuilder()
                .setBaseUri("https://bazaarstores.com/api")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer ")
                .setContentType(ContentType.JSON)
                .build();

        Response response = given(sp).get("/products/" + id );
//        response.prettyPrint();
        response.then()
                .statusCode(401)
                .body("message", Matchers.is("Unauthenticated."));
    }

    public void SetProductIDInJsonFile() {

        //Get products ids to make test independent and dynamic
        Response res = given(spec).get("/products");
        try{
            ObjectMapperUtils.writeJsonToFiles("products_data/productID",res, "[0].id", "id");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}