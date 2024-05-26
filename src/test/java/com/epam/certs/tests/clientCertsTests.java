package com.epam.certs.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.config.SSLConfig.sslConfig;

public class clientCertsTests {
    @Test
    public void test_http_with_client_certs() {
        RestAssured.config().sslConfig(
                sslConfig().with()
                        .trustStore("truststore", "abcd@1234")
                        .trustStoreType("PKCS12")
                        .keyStore("badssl.p12", "badssl.com")
                        .keystoreType("PKCS12")
        );

        /*RestAssured.config().sslConfig(
                sslConfig().relaxedHTTPSValidation()
        );*/

        Response response = given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.HTML)
                .when()
                .get("https://client.badssl.com/")
                .then()
                .extract().response();
        response.prettyPrint();
    }
}
