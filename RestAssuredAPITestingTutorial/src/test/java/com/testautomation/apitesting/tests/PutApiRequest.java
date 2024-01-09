package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.FileNameConstanns;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PutApiRequest {
	
	
@Test	
public void putApiRequest(){
		
		try {
			String PostApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.POST_API_REQUEST_BODY), "UTF-8");
			System.out.println(PostApiReqBody);
			
			String TokenApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.TOKEN_REQUEST_BODY), "UTF-8");
			String PutApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.PUT_API_REQUEST_BODY), "UTF-8");
			
			
			
			Response response=
			RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(PostApiReqBody)
				.baseUri("https://restful-booker.herokuapp.com/booking")
				
			.when()
				.post()

			.then()
				.assertThat()
				.statusCode(200)
				
			.extract()
				.response();
//				.log().all()
			JSONArray jsonarray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String firstName = (String) jsonarray.get(0);
			Assert.assertEquals(firstName, "api testing");
			/*JSONArray totalprice = JsonPath.read(response.body().asString(), "$.booking..totalprice");
			System.out.println(totalprice);
			JSONArray jsonarraycheckin = JsonPath.read(response.body().asString(), "$.booking.bookingdates..checkin");
			String checkin=(String) jsonarraycheckin.get(0);
			Assert.assertEquals(checkin, "2024-01-01");
			*/
			
			int bookingid=JsonPath.read(response.body().asString(), "$.bookingid" );
			RestAssured
				.given()
				.contentType(ContentType.JSON)
				//.body(PostApiReqBody)
				.baseUri("https://restful-booker.herokuapp.com/booking")
				
				.when()
					.get("/{bookingid}", bookingid)
				
				.then()
					.assertThat()
					.statusCode(200);
			
			// token generation
			
			Response tokenResponse =
			RestAssured
			
			.given()
				.contentType(ContentType.JSON)
				.body(TokenApiReqBody)
				.baseUri("https://restful-booker.herokuapp.com/auth")
			
			.when()
			.post()
			
			.then()
				.assertThat()
				.statusCode(200)
				
			.extract()
				.response();
			
			String token=JsonPath.read(tokenResponse.body().asString(), "$.token");
			System.out.println(token);
			
			
			
			//PUT Reqest
			RestAssured
			
			.given()
				.contentType(ContentType.JSON)
				.body(PutApiReqBody)
				.header("Cookie", "token="+token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
				
			
			.when()
				.put("{bookingid}", bookingid)
			
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("Testers"));
				
				
			
			
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

}
