package oAuthPractice;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import pojoClasses.GetCourse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

public class OAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Getting OAuth session token
		
		String response = given().log().all()
				.formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.formParams("grant_type", "client_credentials")
				.formParams("scope","trust")
				.when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
				.then().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String access_token = js.get("access_token");
		System.out.println("access token is : " + access_token);
		
		//Get the course Details
		
		// using Oauth session toke in Query parameter
		
		GetCourse gc = given().log().all().queryParam("access_token", access_token)
		.when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class);
		
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getWebAutomation().get(0).getCourseTitle());
		
		for (int i=0; i<gc.getCourses().getWebAutomation().size();i++)
		{
			if(gc.getCourses().getWebAutomation().get(i).getCourseTitle().equalsIgnoreCase("Cypress"))
			{
				System.out.println("Price for Cypress course is : " + gc.getCourses().getWebAutomation().get(i).getPrice());
			}
			
		}
		
		for(int i=0;i<gc.getCourses().getApi().size();i++)
		{
			//Print all course title under API
			
			System.out.println(gc.getCourses().getApi().get(i).getCourseTitle());
		}
	}

}
