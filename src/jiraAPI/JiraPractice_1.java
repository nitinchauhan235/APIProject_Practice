package jiraAPI;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

public class JiraPractice_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "http://localhost:8080";
		
		//Creating a session
		
		SessionFilter session = new SessionFilter();
		String response = given().log().all().header("Content-Type","application/json").body("{ \"username\": \"nitinchauhan86\", \"password\": \"B472bmt\" }")
		.filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().statusCode(200).extract().asString();
		
		// extracting the session id
		JsonPath js = new JsonPath(response);
		String sessionId = js.get("session.value");
		System.out.println(sessionId);
		
		//Adding a comment
		//Passing path parameters
		
		String commentResponse = given().log().all().header("Content-Type","application/json").filter(session).pathParam("key","10001")
		.body("{\r\n"
				+ "    \"body\": \"Latest comment - This is a comment regarding the quality of the response.\"\r\n"
				+ "}")
		.when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all().statusCode(201).extract().response().asString();
		
		JsonPath js2 = new JsonPath(commentResponse);
		String commentId = js2.get("id");

		// Get the issue
		String issueDetails = given().log().all().filter(session).pathParam("key", "10001")
				.when().get("/rest/api/2/issue/{key}")
				.then().log().all().statusCode(200).extract().response().asString();
		//System.out.println(issueDetails);
		
		//Print only the comments
		JsonPath js1 = new JsonPath(issueDetails);
		int allCommentsSize = js1.get("fields.comment.comments.size()");
		System.out.println(allCommentsSize);
		
		for (int i=0;i<allCommentsSize;i++)
		{
			//check if the latest id accoment id only oprinted basis on the id of teh comment
			String id = js1.get("fields.comment.comments[0" + i + "].id");
			if(commentId.equalsIgnoreCase(id))
			{
			String commentBody = js1.get("fields.comment.comments[0" + i + "].body");
			System.out.println(commentBody);
			}
		}
	}

}
