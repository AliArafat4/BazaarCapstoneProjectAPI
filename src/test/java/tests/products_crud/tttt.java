package tests.products_crud;

import base_url.BazaarStoresBaseUrl;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class tttt extends BazaarStoresBaseUrl {

    @Test
    public void test1() {
        BazaarStoresBaseUrl bazaarStoresBaseUrl = new BazaarStoresBaseUrl();
        bazaarStoresBaseUrl.setSpec();

        Response response = given(spec).get("/products");
        response.prettyPrint();
    }
}