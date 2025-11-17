package tests.stores_crud;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static base_url.BazaarStoresBaseUrl.adminSpec;
import static base_url.BazaarStoresBaseUrl.spec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static tests.stores_crud.US17_BrowseAllStores.storeId;

public class US18_ViewStoreDetails {

    @BeforeClass
    public void getStoreId(){
        US17_BrowseAllStores browseAllStores=new US17_BrowseAllStores();
        browseAllStores.getAllStores01();
    }

    @Test
    public void viewStoreDetails01(){
        Response response=given(adminSpec()).pathParam("storeId",storeId).get("/stores/{storeId}");
        response.then().statusCode(200)
                .body("name", notNullValue())
                .body("location",notNullValue())
                .body("admin_id",notNullValue())
                .body("description",notNullValue());

    }

    @Test
    public void viewStoreDetailsInvalidID02(){
        Response response=given(adminSpec()).pathParam("storeId","0a00").get("/stores/{storeId}");
       response.then().statusCode(404)
                .body("error",equalTo("Store not found"));

    }

    @Test
    public void viewStoreDetailsWithoutToken03(){
        Response response=given(spec()).pathParam("storeId",storeId).get("/stores/{storeId}");
       response.then().statusCode(401)
                .body("message",equalTo("Unauthenticated."));

    }
}
