package com.scprojekt

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`


class GreetingResourceTest {

    fun testHelloEndpoint() {
        given()
          .`when`().get("/hello")
          .then()
             .statusCode(200)
             .body(`is`("Hello RESTEasy"))
    }

}