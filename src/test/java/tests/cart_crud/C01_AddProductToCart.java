package tests.cart_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class C01_AddProductToCart extends BazaarStoresBaseUrl {

    @Test
    public void addProductToCartTest(){

        //Prepare the payload
        Response getProductsIds = given(customerSpec()).get("/products");
        try {
            ObjectMapperUtils.writeJsonToFiles("cart_data/add_product", getProductsIds, "[0].id", "product_id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode payload = ObjectMapperUtils.getJsonNode("cart_data/add_product");
        System.out.println("payload = " + payload);

        //Send the request
        Response response = given(customerSpec()).body(payload).post("/cart/add");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200)
                .body(
                        "success", equalTo(true),
                        "message", equalTo("Product added to cart successfully"),
                        "cart.product_id", equalTo(payload.get("product_id").asInt()),
                        "cart.quantity", equalTo(payload.get("quantity").asInt())
                );

    }

    @Test
    public void addSameProductMultipleTimesTest(){
        //Prepare the payload
        Response getProductsIds = given(customerSpec()).get("/products");
        try {
            ObjectMapperUtils.writeJsonToFiles("cart_data/add_product_multiple", getProductsIds, "[1].id", "product_id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode payload = ObjectMapperUtils.getJsonNode("cart_data/add_product_multiple");
        System.out.println("payload = " + payload);

        //Send the request
        Response response = given(customerSpec()).body(payload).post("/cart/add");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200)
                .body(
                        "success", equalTo(true),
                        "message", equalTo("Product added to cart successfully"),
                        "cart.product_id", equalTo(payload.get("product_id").asInt()),
                        "cart.quantity", equalTo(payload.get("quantity").asInt())
                );
    }

    @Test
    public void addProductUsingDefaultQuantity(){

        //Prepare the payload
        Response getProductsIds = given(customerSpec()).get("/products");
        try {
            ObjectMapperUtils.writeJsonToFiles("cart_data/add_product_without_quantity", getProductsIds, "[2].id", "product_id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode payload = ObjectMapperUtils.getJsonNode("cart_data/add_product_without_quantity");
        System.out.println("payload = " + payload);

        //Send the request
        Response response = given(customerSpec()).body(payload).post("/cart/add");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(401)
                .body(
                        "success", equalTo(true),
                        "message", equalTo("Product added to cart successfully"),
                        "cart.product_id", equalTo(payload.get("product_id").asInt()),
                        "cart.quantity", equalTo(1)
                );

    }

    @Test
    public void addInvalidProduct(){

        //Prepare the payload
        JsonNode payload = ObjectMapperUtils.getJsonNode("cart_data/add_invalid_product");
        System.out.println("payload = " + payload);

        //Send the request
        Response response = given(customerSpec()).body(payload).post("/cart/add");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(400)
                .body(
                        "success", equalTo(false),
                        "message", equalTo("Failed to add product to cart")
                );

    }
}
