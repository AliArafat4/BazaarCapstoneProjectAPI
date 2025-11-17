package tests.cart_crud;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static base_url.BazaarStoresBaseUrl.customerSpec;
import static io.restassured.RestAssured.given;

public class US06_GetProductsFromCart {


    @Test
    public void getCartItemsTest(){

        //send request
        Response response = given(customerSpec()).get("/cart");
        response.prettyPrint();

        //do assertion
        response
                .then()
                .statusCode(500);

    }
}
