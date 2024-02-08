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

public class DynamicJson {
	
	String response;
	
	@BeforeMethod
	public void baseURI()
	{
		RestAssured.baseURI = "http://216.10.245.166";
	}
	
	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle)
	{		
		 response = given().log().all().header("Content-Type", "application/json")
		.body(Payload.Library_AddBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().statusCode(200).header("Server","Apache").body("Msg", equalTo("successfully added")).extract().asString();
	}
	
	@Test
	public void getbook()
	{	
		given().log().all().queryParam("ID",getBookId(response))
		.when().get("/Library/GetBook.php")
		.then().log().all().statusCode(200).extract().asString();				
	}
	
	@Test(dataProvider="BooksData")
	public void deleteBook(String isbn,String aisle)
	{
		given().log().all().body(Payload.Library_DeleteBook(isbn, aisle))
		.when().post("/Library/DeleteBook.php")
		.then().log().all().statusCode(200).body("msg",equalTo("book is successfully deleted"));
	}
	
	public String getBookId(String res)
	{
		JsonPath js = Utilities.jsonPaser(res);
		String bookId = js.get("ID");
		System.out.println(bookId);
		return bookId;
	}
	
		
	@DataProvider (name = "BooksData")
	public Object[][] getData()
	{
		return new Object[][] {{"nitin","15"},{"nitin","16"},{"nitin","17"}};
	}
	


}
