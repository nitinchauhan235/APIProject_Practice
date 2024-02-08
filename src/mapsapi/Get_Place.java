package mapsapi;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class Get_Place {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", "a35c449af2f96408e82f48d1d2669ee4")
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200);
	}

}
