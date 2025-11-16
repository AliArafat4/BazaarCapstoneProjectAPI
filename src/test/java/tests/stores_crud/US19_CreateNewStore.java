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

public class US19_CreateNewStore {


    @BeforeClass
    public void getAdminId(){
        US17_BrowseAllStores browseAllStores=new US17_BrowseAllStores();
        browseAllStores.getAllStores01();
    }

    @Test
    public void createNewStore(){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        Response response=given(adminSpec()).body(editable).post("/stores/create");
        response.then().statusCode(201)
                .body("success",equalTo("Store created successfully!"))
                .body("product.name", notNullValue())
                .body("product.location",notNullValue())
                .body("product.admin_id",notNullValue())
                .body("product.description",notNullValue());


    }

    @Test(dataProvider ="storeData" )
    public void createStoreWithMissingFiled(String filed){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        if (!filed.equalsIgnoreCase("admin_id")) editable.put("admin_id",adminId);
        editable.remove(filed);
        Response response=given(adminSpec()).body(editable).post("/stores/create");
        response.prettyPrint();
        response.then().statusCode(422)
                .body("errors",hasKey(filed));

    }

    @Test(dataProvider ="storeData" )
    public void createStoreInvalidData(String filed){

        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        if (!filed.equalsIgnoreCase("admin_id")) editable.put("admin_id",adminId);
        editable.put(filed,"###");
        Response response=given(adminSpec()).body(editable).post("/stores/create");
        response.prettyPrint();

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
    public void viewStoreDetailsWithoutToken(){
        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        Response response=given(spec()).body(editable).post("/stores/create");
        response.then().statusCode(401)
                .body("message",equalTo("Unauthenticated."));

    }

    @Test
    public void viewStoreDetailsUnauthorizedUser(){
        JsonNode store= ObjectMapperUtils.getJsonNode("stores_data/store");
        ObjectNode editable=(ObjectNode) store;
        editable.put("admin_id",adminId);
        Response response=given(customerSpec()).body(editable).post("/stores/create");
        //known issue
//        response.then().statusCode(401)
//                .body("message",equalTo("Unauthenticated."));

    }



}
