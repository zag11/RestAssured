package com.testautomation.apitesting.tests;
import java.util.regex.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import com.testautomation.apitesting.utils.BaseTest;

import groovy.util.logging.Log;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class PostApiRequest extends BaseTest {
	
	@Test
	public void createBooking() {
				
		//prepare request body
		JSONObject booking = new JSONObject();
		JSONObject bookingdates = new JSONObject();
	
		booking.put("firstname", "api testing");
		booking.put("lastname",  "tutorial");
		booking.put("totalprice", 1000);
		booking.put("depositpaid", true);
		booking.put("additionalneeds",  "breakfast");
		booking.put("bookingdates",  bookingdates);
		bookingdates.put("checkin", "2024-01-01");
		bookingdates.put("checkout", "2024-01-01");
		
		//class
		Response response =
		RestAssured
			.given()	
				.contentType(ContentType.JSON)
				.body(booking.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().headers()
				//.log().body()
				//.log().all()
			.when()
					.post()
			.then()
					.assertThat()
					//.log().body()
					//.log().headers()
					//.log().all()
					//.log().ifValidationFails()
					.statusCode(200)
					.body("booking.firstname", Matchers.equalTo("api testing"))
					.body("booking.totalprice", Matchers.equalTo(1000))
					.body("booking.bookingdates.checkin", Matchers.equalTo("2024-01-01"))
					.body("booking.bookingdates.checkout", Matchers.equalTo("2024-01-01"))
			
					
			.extract()
				.response();
				int bookingid=response.path("bookingid");
			
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.pathParam("bookingid", bookingid)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("{bookingid}")
				.then()
					.assertThat()
					.statusCode(200)
					.body("firstname", Matchers.equalTo("api testing"))
					.body("lastname", Matchers.equalTo("tutorial"));
					//.log().all();
			
					
				
			
			
				
					
		
		
		
		
	
		
		
		
		
		
	}

}
