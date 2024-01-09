package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstanns;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import net.minidev.json.JSONArray;


public class PostAPIRequestUsingFile extends BaseTest{
	
	
	@Test
	public void postApiRequest(){
		
		try {
			String PostApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.POST_API_REQUEST_BODY), "UTF-8");
			System.out.println(PostApiReqBody);
			
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
			JSONArray totalprice = JsonPath.read(response.body().asString(), "$.booking..totalprice");
			System.out.println(totalprice);
			JSONArray jsonarraycheckin = JsonPath.read(response.body().asString(), "$.booking.bookingdates..checkin");
			String checkin=(String) jsonarraycheckin.get(0);
			Assert.assertEquals(checkin, "2024-01-01");
			
			
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
			
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

}
