package com.epam.pet.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;

public class baseTests {
    @BeforeClass
    public static void setup() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io")
                .setBasePath("/v2")
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
