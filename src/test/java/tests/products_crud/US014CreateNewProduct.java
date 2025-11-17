package tests.products_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.*;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class US014CreateNewProduct extends BazaarStoresBaseUrl {



    RequestSpecification spec;
    @BeforeMethod
    public void setUp(){
        spec = StoreManagerSpec();
    }

    @Test
    public void createNewProductWithAllDetails()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();
        response.then()
                .statusCode(201)
                .body("product.name", equalTo(payload.get("name").textValue()),
                        "product.sku", equalTo(payload.get("sku").textValue()),
                        "product.category_id", equalTo(payload.get("category_id").intValue())

                );

        deleteProductByID(response);
    }

    @Test @Description("Known Issue") //@Ignore
    public void createNewProductWithRequiredFields()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        ObjectNode obj = (ObjectNode) payload;
        obj.remove("manufacturer");
        obj.remove("image_url");
//        obj.remove("discount");
        obj.remove("description");

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();
        response.then()
                .statusCode(201)
                .body("product.name", equalTo(payload.get("name").textValue()),
                        "product.sku", equalTo(payload.get("sku").textValue()),
                        "product.category_id", equalTo(payload.get("category_id").intValue())

                );
        deleteProductByID(response);
    }

    @Test
    public void createNewProductWithAllFieldsMissing()
    {
        Response response = given(spec)
                .body("")
                .post("/products/create");
        response.prettyPrint();
        Response res = response;
        res.then()
                .statusCode(422);
    }


    @DataProvider(name = "fields")
    public Object[][] getUserData() {
        return new Object[][]{
                {"name" },
                {"price" },
                {"stock" },
                {"sku"}
        };
    }

    @Test(dataProvider = "fields")
    public void createNewProductWithMissingRequiredData(String field)
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        ObjectNode obj = (ObjectNode) payload;
        obj.remove(field);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();

        response.then()
                .statusCode(422)
                .body("message", is("The " +  field + " field is required."));
    }

    @Test
    public void createNewProductWithTakenSku()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        String takenSku = given(spec).get("/products").jsonPath().getString("[0].sku");

        ObjectNode obj = (ObjectNode) payload;
        obj.put("sku", takenSku);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();

        response.then()
                .statusCode(422)
                .body("message", is("The sku has already been taken."));
    }

    @Test
    public void createNewProductWithNegativePrice()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        ObjectNode obj = (ObjectNode) payload;
        obj.put("price", -10);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();

        response.then()
                .statusCode(201);

            deleteProductByID(response);
//                .body("message", is("The sku has already been taken."));
    }

    @Test
    public void createNewProductWithNegativeStock()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        ObjectNode obj = (ObjectNode) payload;
        obj.put("stock", -10);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();

        response.then()
                .statusCode(201);

            deleteProductByID(response);
//                .body("message", is("The sku has already been taken."));
    }

    @Test
    public void createNewProductWithNegativeDiscount()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        ObjectNode obj = (ObjectNode) payload;
        obj.put("discount", -10);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");
        response.prettyPrint();

        response.then()
                .statusCode(201);

            deleteProductByID(response);
//                .body("message", is("The sku has already been taken."));
    }

    private void deleteProductByID(Response res) {
        //delete products after creation to ensure repeatability of tests
       Response response = given(spec).delete("/products/" + res.getBody().jsonPath().getInt("product.id"));
        response.then().statusCode(200);
    }
}