package tests.cart_crud;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static base_url.BazaarStoresBaseUrl.customerSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utilities.ObjectMapperUtils.getJsonNode;

public class US07_RemoveProductFromCart {

    @Test
    public void removeProductFromCartTest(){

        //send request
        Response response = given(customerSpec()).delete("/cart/"+ getJsonNode("cart_data/add_product").get("product_id").asInt());
        response.prettyPrint();

        //do assertion
        response
                .then()
                .statusCode(200)
                .body(
                        "success", equalTo(true),
                        "message", equalTo("Product removed from cart successfully")
                );
    }


    @Test //A bug, it should not remove anything and give an error message
    public void removeInvalidProductFromCartTest(){

        //send request
        Response response = given(customerSpec()).delete("/cart/0");
        response.prettyPrint();

        //do assertion
        response
                .then()
                .statusCode(200);
    }


    @Test //this is just because clear endpoint in a bug
    public void clearCartAfterTests(){

        //send request
        Response response01 = given(customerSpec()).delete("/cart/"+getJsonNode("cart_data/add_product_multiple").get("product_id").asInt());
        Response response02 = given(customerSpec()).delete("/cart/"+getJsonNode("cart_data/add_product_without_quantity").get("product_id").asInt());


    }
}
