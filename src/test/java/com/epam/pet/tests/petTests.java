package com.epam.pet.tests;

import com.epam.pet.model.Category;
import com.epam.pet.model.Pet;
import com.epam.pet.model.Tags;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class petTests extends baseTests {
    @Test
    public void test_Complex_POJO() throws JsonProcessingException {
        Category category = new Category(1, "Dog");

        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add("https://www.pexels.com/search/dog/");
        photoUrls.add("https://pixabay.com/images/search/dog/");

        List<Tags> tags = new ArrayList<>();
        tags.add(new Tags(11, "snowdog"));
        tags.add(new Tags(12, "wilddog"));

        Pet petDog = new Pet(1, category, "scooby-doo", photoUrls, tags, "pending");

        ObjectMapper petMapper = new ObjectMapper();
        String jsonString = petMapper.writerWithDefaultPrettyPrinter().writeValueAsString(petDog);

        System.out.println(jsonString);

    }

    @Test
    public void test_Serialization_Deserialization() throws IOException {
        Pet petDog = getPetDog();

        //Serialization
        ObjectMapper petMapper = new ObjectMapper();
        String jsonString = petMapper.writerWithDefaultPrettyPrinter().writeValueAsString(petDog);

        Response petResponse = given().log().all().body(jsonString).when().post("/pet").then().log().all().assertThat().statusCode(200).extract().response();
        Assert.assertEquals(200, petResponse.statusCode());

        System.out.println("----Deserialization/toString------");
        Pet pet = petMapper.readValue(petResponse.asString(), Pet.class);
        System.out.println(pet);

        System.out.println("----Response Pretty Print------");
        petResponse.prettyPrint();

    }

    @Test
    public void test_Chaining() {
        // Post - Create a Pet
        Pet petDog = getPetDog();
        int id = given()
                .log().all()
                .body(petDog)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .extract()
                .as(Pet.class)
                .getId();
        System.out.println(id);

        // Get - Retrieve Pet Details
        given()
                .log().all()
                .pathParams("newPetId", String.valueOf(id))
                .when()
                .get("/pet/{newPetId}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("scooby-doo"))
                .body("status", equalTo("pending"));

        // Put - Update Pet Status to Available
        petDog.setStatus("available");
        petDog.setId(id);
        given()
                .log().all()
                .body(petDog)
                .when()
                .put("/pet")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);

        // Get - Retrieve Pet Details & Assert Updated Status
        given()
                .log().all()
                .pathParams("newPetId", String.valueOf(id))
                .when()
                .get("/pet/{newPetId}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("scooby-doo"))
                .body("status", equalTo("available"));

        // Delete - Remove the Pet
        given()
                .header("api_key", "special-key")
                .log().all()
                .pathParams("newPetId", String.valueOf(id))
                .when()
                .delete("/pet/{newPetId}")
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void test_FindByStatus() throws JsonProcessingException {
        String str = "available";
        Response availablePetsRes = given()
                //       .log().all()
                .queryParams("status", str)
                .when()
                .get("/pet/findByStatus")
                .then()
                //      .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        //availablePetsRes.prettyPrint();

        System.out.println("Available Pets with name == PUFF");
        String pet_Name_Puff = availablePetsRes.path("findAll {it.name == 'Puff'}").toString();
        System.out.println(pet_Name_Puff);

        System.out.println("Available Pet with maximum ID");
        String pet_Max_id =availablePetsRes.path("max {it.id}").toString();
        System.out.println(pet_Max_id);

        System.out.println("Available Pet with minimum ID");
        String pet_Min_id =availablePetsRes.path("min {it.id}").toString();
        System.out.println(pet_Min_id);

    }

    @Test
    public void test_UploadPhoto() {
        String petId = "9223372036854626604";
        given()
                .log().all()
                .contentType("multipart/form-data")
                .multiPart(new File("petstore.png"))
                .pathParams("petId", petId)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message",containsString("petstore.png"))
                .extract()
                .response()
                .prettyPrint();
    }

    private Pet getPetDog() {
        Category category = new Category(1, "Dog");

        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add("https://www.pexels.com/search/dog/");
        photoUrls.add("https://pixabay.com/images/search/dog/");

        List<Tags> tags = new ArrayList<>();
        tags.add(new Tags(11, "snowdog"));
        tags.add(new Tags(12, "wilddog"));

        Pet petDog = new Pet(54321, category, "scooby-doo", photoUrls, tags, "pending");
        return petDog;
    }

}
