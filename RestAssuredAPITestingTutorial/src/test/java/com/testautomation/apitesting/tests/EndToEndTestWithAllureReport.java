package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstanns;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;



@Epic("epic-01")
@Feature("Create Upadte,Patch and delete")
public class EndToEndTestWithAllureReport extends BaseTest {
	

	private static final Logger logger = LogManager.getLogger(EndToEndTestWithAllureReport.class);
	
	@Story("Story-01")
	@Test(description = "E2E Apisting")
	@Description(" e2e-test with allure Report")
	@Severity(SeverityLevel.CRITICAL)
	
	public void e2eApiRequest (){
		logger.info("e2eApiRequest strat");
			try {
				String PostApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.POST_API_REQUEST_BODY), "UTF-8");
				System.out.println(PostApiReqBody);
				
				String TokenApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.TOKEN_REQUEST_BODY), "UTF-8");
				String PutApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.PUT_API_REQUEST_BODY), "UTF-8");
				String PatchApiReqBody=FileUtils.readFileToString(new File(FileNameConstanns.PATCH_REQUEST_BODY), "UTF-8");
				
				
				
				Response response=
				RestAssured
				.given().filter(new AllureRestAssured())
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
//					.log().all()
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
					.given().filter(new AllureRestAssured())
					
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
				
				.given().filter(new AllureRestAssured())
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
				
				.given().filter(new AllureRestAssured())
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
					
					
				
				//PATCH API REQ
				
				RestAssured
				
					.given().filter(new AllureRestAssured())
					.contentType(ContentType.JSON)
					.body(PatchApiReqBody)
					.header("Cookie", "token="+token)
					.baseUri("https://restful-booker.herokuapp.com/booking")
					
					.when()
						.patch("/{bookingid}", bookingid)
					
					.then()
						.assertThat()
						.statusCode(200)
						.body("firstname", Matchers.equalTo("ApiPostman"));
				
				
				RestAssured
				
				.given().filter(new AllureRestAssured())
				.contentType(ContentType.JSON)
				.header("Cookie", "token="+token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
				
				.when()
					.delete("/{bookingid}", bookingid)
				
				.then()
					.assertThat()
					.statusCode(201);
					
						
				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("e2eApiRequest End");
			
		}

}
