package mapsapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import files.Payload;

public class Add_Place {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//given - all input details
		//when - submit the API
		//then - validate the response		
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\nitin\\OneDrive\\Desktop\\Softwares\\API Automation\\AddPlace.json"))))
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server","Apache/2.4.52 (Ubuntu)").extract().asString();
		
		// parsing the string json response
		
		JsonPath js = new JsonPath(response);
		String place_id = js.getString("place_id");
		
		// Update the address of above added place
		
		given().log().all().queryParam("key", "qaclick123").header("Content_Type","application/json")
		.body(Payload.Modify_Place(place_id))
		.when().put("maps/api/place/update/json")
		.then().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
			
		// Get the place updated above
		
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).body("address",equalTo("C 301 Marvel Zephyr, INDIA"));		
		
	}

}
