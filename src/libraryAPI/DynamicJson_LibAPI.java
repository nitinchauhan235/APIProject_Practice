package libraryAPI;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.Utilities;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DynamicJson_LibAPI {
	
	String Id = "";
	
	@BeforeMethod
	public void baseURI()
	{
		RestAssured.baseURI = "http://216.10.245.166";
	}
	
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle)
	{
		String response = given().log().all().header("Content-Type","application/json").body(Payload.Library_AddBook(isbn, aisle)).
		when().post("/Library/Addbook.php").
		then().statusCode(200).log().all().header("Server","Apache").body("Msg",equalTo("successfully added")).extract().asString();
		
		JsonPath js = Utilities.jsonPaser(response);
		Id = js.getString("ID");
		System.out.println(Id);		
	}
	
	@Test(dataProvider = "BooksData")
	public void deleteBook(String isbn, String aisle)
	{
		given().log().all().header("Content-Type","application/json").body(Payload.Library_DeleteBook(isbn, aisle))
		.when().post("/Library/DeleteBook.php")
		.then().log().all().statusCode(200).body("msg",equalTo("book is successfully deleted"));
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		return new Object[][] {{"Nick1","00"},{"Nick2","00"}};
	}

}
