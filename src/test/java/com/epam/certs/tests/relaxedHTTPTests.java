package com.epam.certs.tests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class relaxedHTTPTests {
    //@Test
    public void test_RelaxedHTTP() {
        given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.HTML)
                .when()
                .get("https://client.badssl.com/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }


}
