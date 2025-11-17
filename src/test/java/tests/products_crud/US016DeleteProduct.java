package tests.products_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class US016DeleteProduct extends BazaarStoresBaseUrl {

    RequestSpecification spec;
    @BeforeMethod
    public void setUp(){
        spec = StoreManagerSpec();
    }


    @Test
    public void deleteProduct()
        {
            JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
            Response res = given(spec)
                    .body(payload)
                    .post("/products/create");

            Response response = given(spec).delete("/products/" + res.getBody().jsonPath().getInt("product.id"));
            response.then().statusCode(200)
                    .body("success", is("Product deleted successfully!"));

        }

        @Test
        public void failDeleteProduct()
        {
            Response response = given(spec).delete("/products/000000000000000000000");
            response.then().statusCode(500)
                    .body("error", is( "Product deletion failed. Please try again."));

        }
}