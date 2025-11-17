package tests.products_crud;

import base_url.BazaarStoresBaseUrl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class US015UpdateExistingProduct extends BazaarStoresBaseUrl {

    RequestSpecification spec;
    @BeforeMethod
    public void setUp(){
        spec = StoreManagerSpec();
    }

    @Test
    public void VerifySuccessfulUpdateExistingProduct()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
        Response response = given(spec)
                .body(payload)
                .post("/products/create");


        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");

        Response res = given(spec).body(updatedPayload).put("/products/" + response.jsonPath().getString("product.id"));

        res.then()
                .statusCode(200)
                .body("product.name", equalTo(updatedPayload.get("name").textValue()),
                        "product.sku", equalTo(updatedPayload.get("sku").textValue()),
                        "product.category_id", equalTo(updatedPayload.get("category_id").intValue()),
                        "product.price", equalTo(updatedPayload.get("price").intValue())
                );

        deleteProductByID(res);
    }



    @DataProvider(name = "fields")
    public Object[][] fields() {
        return new Object[][]{
                {"name" },
                {"price" },
                {"stock" },
                {"sku"}
        };
    }

    @Test(dataProvider = "fields")
    public void VerifyFailUpdateProductRequiredDetails(String field)
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
        Response response = given(spec)
                .body(payload)
                .post("/products/create");

        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");
        ObjectNode objectNode = (ObjectNode) updatedPayload;
        objectNode.put(field, "");

        Response res = given(spec).body(updatedPayload).put("/products/" + response.jsonPath().getString("product.id"));

        res.then()
                .statusCode(422)
                .body("message", is("The " +  field + " field is required."));
        deleteProductByID(response);
    }

    @Test
    public void VerifyFailUpdateProductImage()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
        Response response = given(spec)
                .body(payload)
                .post("/products/create");

        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");
        ObjectNode objectNode = (ObjectNode) updatedPayload;
        objectNode.put("image", "book.txt");

        Response res = given(spec).body(updatedPayload).put("/products/" + response.jsonPath().getString("product.id"));

        res.prettyPrint();
        res.then()
                .statusCode(422)
                .body("message", is("The image field must be an image. (and 1 more error)"));

        deleteProductByID(response);
    }

    @DataProvider(name = "negative amount")
    public Object[][] negativeAmount() {
        return new Object[][]{
                {"price" , "0000000003"},
                {"stock" ,"0000000004"},
        };
    }

    @Test(dataProvider = "negative amount")
    public void VerifyFailUpdateProductAmounts(String field, String newSku)
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");
        ObjectNode changeSku = (ObjectNode) payload;
        changeSku.put("sku", newSku);

        Response response = given(spec)
                .body(payload)
                .post("/products/create");

        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");
        ObjectNode objectNode = (ObjectNode) updatedPayload;
        objectNode.put(field, -10);
        objectNode.put("sku", newSku);

        Response res = given(spec).body(updatedPayload).put("/products/" + response.jsonPath().getString("product.id"));

        res.then()
                .statusCode(200);
        deleteProductByID(response);
    }


    @Test
    public void VerifyFailUpdateProductDiscount()
    {
        JsonNode payload = ObjectMapperUtils.getJsonNode("/products_data/newProduct");

        Response response = given(spec)
                .body(payload)
                .post("/products/create");

        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");
        ObjectNode objectNode = (ObjectNode) updatedPayload;
        objectNode.put("discount", -10);

        Response res = given(spec).body(updatedPayload).put("/products/" + response.jsonPath().getString("product.id"));

        res.then()
                .statusCode(422)
                .body("message", is("The discount field must be at least 0."));
        deleteProductByID(response);
    }


    @Test
    public void VerifyFailUpdatingNonExistingProduct()
    {

        JsonNode updatedPayload = ObjectMapperUtils.getJsonNode("/products_data/updateProductDetails");

        Response res = given(spec).body(updatedPayload).put("/products/00000000000000000000000");

        res.then()
                .statusCode(500)
                .body("error", is( "Product update failed. Please try again."));

    }




    private void deleteProductByID(Response res) {
        //delete products after creation to ensure repeatability of tests
        Response response = given(spec).delete("/products/" + res.getBody().jsonPath().getInt("product.id"));
        response.then().statusCode(200);
    }

}