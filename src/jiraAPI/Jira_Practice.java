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

public class Jira_Practice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "http://localhost:8080";
		
		//Using Session filter to store the sessionId for subsequent steps
		
		SessionFilter session = new SessionFilter(); 
		given().log().all().header("Content-Type","application/json").body("{ \"username\": \"nitinchauhan86\", \"password\": \"B472bmt\" }")
		.filter(session).when().post("/rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200);
		
		//Add the comment
		
		String commentResponse = given().log().all().header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \"New comment - Adding an attachment this time.\"\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue/10000/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(commentResponse);
		String commentId = js.get("id");
		String sentComment = js.getString("body");
		System.out.println(commentId);
		
		// Add attachment to the issue
		
		given().log().all().header("X-Atlassian-Token", "nocheck").filter(session)
		.multiPart("file", new File("jira.txt")).header("Content-Type","multipart/form-data")
		.when().post("/rest/api/2/issue/10000/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		// Get Issue
		//limiting json response through query parameters
		String issueResponse = given().log().all().queryParam("fields","comment").filter(session)
		.when().get("/rest/api/2/issue/10000")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = new JsonPath(issueResponse);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		System.out.println(commentsCount);
		
		for(int i=0;i<commentsCount;i++)
		{
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id");
			if(commentIdIssue.equalsIgnoreCase(commentId))
			{
				String message = js1.get("fields.comment.comments["+i+"].body");
				System.out.println(message);
				Assert.assertEquals(message,sentComment);
			}
		}
	

	}

}
