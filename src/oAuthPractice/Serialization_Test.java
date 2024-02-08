package oAuthPractice;

import io.restassured.RestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
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

public class Serialization_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		given().log().all().queryParam("key","qaclick123").body(p)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).log().all();

	}

}
