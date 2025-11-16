package tests.cart_crud;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static base_url.BazaarStoresBaseUrl.customerSpec;
import static io.restassured.RestAssured.given;

public class C04_ClearCart {

    @Test
    public void clearCartTest(){

        //send request
        Response response = given(customerSpec()).post("/cart/clear");
        response.prettyPrint();

        //do assertion
        response
                .then()
                .statusCode(200);
    }
}
