package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import com.testautomation.apitesting.utils.FileNameConstanns;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PostApiRequestUsingPojos {
	
	
	@Test
	public void postApiRequest() {

		try {
		
		String jsonSchema=FileUtils.readFileToString(new File(FileNameConstanns.JSON_SCHEMA), "UTF-8");
		
		BookingDates bookingDates = new BookingDates("2024-01-05","2024-01-10");
		Booking booking = new Booking("api testing","totalprice","breakfast", 1000,true,bookingDates);
		
		//serialization
		ObjectMapper objestMapper = new ObjectMapper();
		String requestBody=objestMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
		System.out.println(requestBody);
			
//		de-serialization JSON to JAVA Object
		Booking bookingDetails=objestMapper.readValue(requestBody, Booking.class);
		System.out.println(bookingDetails.getFirstname());
		System.out.println(bookingDetails.getTotalprice());
			
		System.out.println(bookingDetails.getBookingdates().getCheckin());
		System.out.println(bookingDetails.getBookingdates().getCheckout());
		
		
		Response response =
		RestAssured
		
		.given()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			
		
		.when()
			.post()
		
		.then()
			.assertThat()
			.statusCode(200)
		.extract()
			.response();
		
		
		
		int bookingId = response.path("bookingid");
		//System.out.println(jsonSchema);
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			
			.when()
				.get("/{bookingId}" ,bookingId)
				
			
			.then()
				.assertThat()
				.statusCode(200);
				//.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
		
		
		
				
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

}


