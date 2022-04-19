package com.orhunakkan.apiTests;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BasicRestTest {

    String url = "https://reqres.in/api/users?page=2";

    @Test
    public void basicGetTest(){
        Response response = RestAssured.get(url);
        System.out.println("response.statusCode() = " + response.statusCode());
        response.prettyPrint();
        Assert.assertEquals(200,response.statusCode());
    }
}
