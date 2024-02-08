package oAuthPractice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoClasses.GetCourse;
import pojoClasses_Serialization.Location;
import pojoClasses_Serialization.SendPlace;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.testng.Assert;

public class ReqSpecification_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		// Request and response specification will be used for the code portion which will be common
		// for all requests or responses
		
		// This request specification can be used for any api call whether its get, post etc which required this query parameter
		RequestSpecification req = new RequestSpecBuilder().addQueryParam("key","qaclick123").setBaseUri("https://rahulshettyacademy.com").build();
		
		SendPlace p = new SendPlace();
		
		p.setAccuracy(50);
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://google.com");
		p.setLanguage("French-IN");
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);
		ArrayList<String> t = new ArrayList<String>();
		t.add(0,"shoe park");
		t.add(1, "shop");
		p.setTypes(t);
		
		// these type of response specification can be used for any response where we expect status code as 200
		ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(200).build();
		
		//Breaking request and response 
		RequestSpecification sepreq = given().spec(req).body(p);
		
		sepreq.when().post("/maps/api/place/add/json")
		.then().log().all().spec(res);

	}

}
