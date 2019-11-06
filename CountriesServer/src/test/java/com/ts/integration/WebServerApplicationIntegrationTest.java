package com.ts.integration;

import java.io.IOException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class WebServerApplicationIntegrationTest {

	
	// Apache library plain Java based test
	@Test
	public void givenCountryDoesNotExists_whenCapitalIsRetrievedWithCountryCode_then500IsReceived() throws ClientProtocolException, IOException {
		 	String name = RandomStringUtils.randomAlphabetic(8);
		    HttpUriRequest request = new HttpGet("http://localhost:9000/api/countryCode/" + name );
		    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request );
		    
		    Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 500);
	}
	
	// REST ASSURED library based tests:
	
	// Harcoded Test
	@Test
	public void validCountryCode() {
		
		get("http://localhost:9000/api/countryCode/USA").then().statusCode(200).assertThat()
		.body(containsString("Washington, D.C."));
		
		
	}
	
	// Data Driven Tests
	@DataProvider
    public Object[][] getCountryNameAndCapital() {
        return new Object[][]{{"India", "New Delhi"}, 
        					  {"Australia", "Canberra"}};
    }
	@Test(dataProvider="getCountryNameAndCapital")
	public void validCountryName(String countryName, String capital) {
		
		get("http://localhost:9000/api/countryName/"+countryName).then().statusCode(200).assertThat()
		.header("Content-Type", "application/json")
		.body(countryName, equalTo(capital));
		
	}
	

	@DataProvider
    public Object[][] getinvalidCountryCode() {
        return new Object[][]{{"123"}, 
        					  {";"},{"y74"},{"DEF"}};
    }
	@Test(dataProvider="getinvalidCountryCode")
	public void invalidCountryCode(String invalidCode) {
		get("http://localhost:9000/api/countryCode/"+invalidCode).then().statusCode(500).assertThat()
		.body("message", equalTo("404 Not Found"))
		.body("error", equalTo("Internal Server Error"));
		
		
	}
	
	@DataProvider
    public Object[][] getBadRequestParams() {
        return new Object[][]{ 
        					  {","},{"^"},{"CFGH"}};
    }
	@Test(dataProvider="getBadRequestParams")
	public void invalidCountryCode_BadRequest(String invalidCode) {
		get("http://localhost:9000/api/countryCode/"+invalidCode).then().statusCode(500).assertThat()
		.body("message", equalTo("400 Bad Request"))
		.body("error", equalTo("Internal Server Error"));
		
		
	}
	
	
}
