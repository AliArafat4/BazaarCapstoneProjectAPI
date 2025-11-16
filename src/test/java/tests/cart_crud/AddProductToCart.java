package tests.cart_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;

public class AddProductToCart extends BazaarStoresBaseUrl {

    @Test
    public void addProductToCartTest(){

        //Prepare the payload
        JsonNode payload = ObjectMapperUtils.getJsonNode("cart_data/add_product");
        System.out.println("payload = " + payload);

        //Send the request
        Response response = given(customerSpec()).body(payload).post("/cart/add");
        response.prettyPrint();


    }
}
