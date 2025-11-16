package tests.stores_crud;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

import static base_url.BazaarStoresBaseUrl.adminSpec;
import static base_url.BazaarStoresBaseUrl.spec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class US17_BrowseAllStores {

    public static String storeId;
    @Test
    public void getAllStores01(){

        Response response=given(adminSpec()).get("/stores");
        response.then().statusCode(200)
                .body("", instanceOf(List.class));

        List<Map<String, Object>> stores = response.jsonPath().getList("");
        Map<String, Object> store = stores.get(0);

         storeId = store.get("id").toString();
    }

    @Test
    public void getStoresInvalidToken02(){

        RequestSpecification spec = spec()
                .header("Authorization", "123456");  // this overrides the old one

        Response response = given()
                .spec(spec)
                .get("/stores");
        response.then().statusCode(401)
                .body("message", equalTo("Unauthenticated."));

    }


    @Test
    public void getStoresWithOutToken03(){

        Response response = given()
                .spec(spec())
                .get("/stores");
        response.then().statusCode(401)
                .body("message", equalTo("Unauthenticated."));

    }

    @Test
    public void getStoresExpiredToken04(){

        RequestSpecification spec = spec()
                //expired token
                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2JhemFhcnN0b3Jlcy5jb20vYXBpL2xvZ2luIiwiaWF0IjoxNzYzMjMyMDEwLCJleHAiOjE3NjMyMzU2MTAsIm5iZiI6MTc2MzIzMjAxMCwianRpIjoiMFlIVklLTW1IbnA0TVdNaSIsInN1YiI6IjM1MiIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.TwBJ0k9CmWl8zldDby9ue5CTCj_jpIzfOkcnrklKpeY");  // this overrides the old one

        Response response = given()
                .spec(spec)
                .get("/stores");
        response.then().statusCode(401)
                .body("message", equalTo("Unauthenticated."));

    }


}
