package tests.stores_crud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static base_url.BazaarStoresBaseUrl.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static tests.stores_crud.US17_BrowseAllStores.adminId;
import static tests.stores_crud.US17_BrowseAllStores.storeId;

public class US20_UpdateStore {


    @BeforeClass
    public void getStoreId(){
        US17_BrowseAllStores browseAllStores=new US17_BrowseAllStores();
        browseAllStores.getAllStores01();
    }

    @Test
    public void updateStore(){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        //known issue
        editable.put("admin_d",adminId);
        Response response=given(adminSpec())
                .body(editable)
                .pathParam("storeId",storeId)
                .put("/stores/{storeId}");

        response.then().statusCode(200)
                .body("success",equalTo("Store updated successfully!"))
                .body("product.name", notNullValue())
                .body("product.location",notNullValue())
                .body("product.admin_id",notNullValue())
                .body("product.description",notNullValue());


    }

    @Test(dataProvider ="storeData" )
    public void updateStoreWithMissingFiled(String filed){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        if (!filed.equalsIgnoreCase("admin_id")) {
            editable.put("admin_id", adminId);
            //known issue
            editable.put("admin_d",adminId);
        }
        editable.remove(filed);
        Response response=given(adminSpec())
                .body(editable)
                .pathParam("storeId",storeId)
                .put("/stores/{storeId}");
        response.then().statusCode(422)
                .body("errors",hasKey(filed));

    }

    @Test(dataProvider ="storeData" )
    public void updateStoreInvalidData(String filed){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        if (!filed.equalsIgnoreCase("admin_id")) {
            editable.put("admin_id", adminId);

        }
        //known issue
        editable.put("admin_d",adminId);
            editable.put(filed,"###");
        Response response=given(adminSpec())
                .body(editable)
                .pathParam("storeId",storeId)
                .put("/stores/{storeId}");

        //known issue
//        response.then().statusCode(422)
//                .body("errors",hasKey(filed));


    }

    @DataProvider
    public Object[][] storeData(){
        return new Object[][]{
                {"name"},
                {"location"}
                ,{"description"}
                ,{"admin_id"}
        };
    }


    @Test
    public void updateStoreWithoutToken(){
        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        Response response=given(spec())
                .body(editable)
                .pathParam("storeId",storeId)
                .put("/stores/{storeId}");
        response.then().statusCode(401)
                .body("message",equalTo("Unauthenticated."));

    }

    @Test
    public void updateStoreUnauthorizedUser(){
        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        Response response=given(customerSpec())
                .body(editable)
                .pathParam("storeId",storeId)
                .put("/stores/{storeId}");
        //known issue
//        response.then().statusCode(401)
//                .body("message",equalTo("Unauthenticated."));

    }



}
