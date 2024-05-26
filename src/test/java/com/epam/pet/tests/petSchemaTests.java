package com.epam.pet.tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class petSchemaTests extends baseTests {

    @Test
    public void testJsonSchemaData() {
        String str_PetId = "1";
        given()
                .log().all()
                .pathParams("newPetId", str_PetId)
                .when()
                .get("/pet/{newPetId}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("PetSchema.json"));


    }
}
