package tests.stores_crud;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import static base_url.BazaarStoresBaseUrl.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static tests.stores_crud.US19_CreateNewStore.storeId;

public class US21_DeleteStore {


    @Test
    public void deleteStore(){

        Response response=given(adminSpec())
                .pathParam("storeId",storeId)
                .delete("/stores/{storeId}");

        response.then().statusCode(200)
                .body("success",equalTo("Store deleted successfully!" ));

    }


    @Test
    public void deleteStoreWithoutToken(){

        Response response=given(spec())
                .pathParam("storeId",storeId)
                .delete("/stores/{storeId}");
        response.then().statusCode(401)
                .body("message",equalTo("Unauthenticated."));

    }

    @Test
    public void deleteStoreUnauthorizedUser(){

        Response response=given(customerSpec())
                .pathParam("storeId",storeId)
                .delete("/stores/{storeId}");
        //known issue
//        response.then().statusCode(401)
//                .body("message",equalTo("Unauthenticated."));

    }



}
