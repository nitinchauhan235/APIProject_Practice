package ecomm_practice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoClasses.GetCourse;
import pojoClasses_Ecomm.CreateProduct_Response;
import pojoClasses_Ecomm.Login;
import pojoClasses_Ecomm.Login_Response;
import pojoClasses_Ecomm.Order;
import pojoClasses_Ecomm.OrderDetails;
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
import org.testng.annotations.Test;

public class Ecomm_Test {
	
	
	static String token;
	static String userId;
	static String productId;
	static String orderId;
	static RequestSpecification req_Auth;
	
	@Test(priority=0)
	public void login()
	{
	// Common details for all requests
	RequestSpecification req = (RequestSpecification) new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
			setContentType("application/json").build();
	
	//Calling getters setters to give login details
	
	Login logincred = new Login();
	logincred.setUserEmail("nitinchauhan86@gmail.com");
	logincred.setUserPassword("Hello@123");
	
	// Seggregating login body
	RequestSpecification reqLogin = given().spec(req).body(logincred);	
	
	//Common check for all apis
	ResponseSpecification res = (ResponseSpecification) new ResponseSpecBuilder().expectStatusCode(200).build();
	
	// Posting the request	and getting the response as the object of the pojo class
	Login_Response logresp = reqLogin.when().post("/api/ecom/auth/login")
	.then().log().all().spec(res).extract().as(Login_Response.class);	
	
	 token = logresp.getToken();
	 userId = logresp.getUserId();
	
	}
	
	@Test(priority=1)
	public void create_Product()
	{
		
		 req_Auth = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization",token ).build();
		
		RequestSpecification req_create_Product = given().log().all().spec(req_Auth)
		.param("productName", "NC_Product")
		.param("productAddedBy", userId)
		.param("productCategory", "fashion")
		.param("productSubCategory", "shirts")
		.param("productPrice", "11500")
		.param("productDescription", "Addias Originals")
		.param("productFor", "women")
		.multiPart("productImage",new File("C://Users//nitin//OneDrive//Desktop//Softwares//API Automation//Add_Product.png"));
		
		ResponseSpecification resp_createPro = new ResponseSpecBuilder().build();
		
		
		  CreateProduct_Response creat_resp =
		  req_create_Product.when().post("/api/ecom/product/add-product")
		  .then().log().all().spec(resp_createPro).extract().as(CreateProduct_Response.
		  class); 		
		
		  productId = creat_resp.getProductId();
				
	}
	
	@Test(priority=2)
	public void validate_Product() 
	{
		
		RequestSpecification req_validate_Product = given().log().all().spec(req_Auth);
		 String resp = 	req_validate_Product.when().get("/api/ecom/product/get-product-detail/"+productId)
		.then().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(resp);
		String msg = js.getString("message");
		System.out.println(msg);
		Assert.assertEquals("Product Details fetched Successfully", msg);
	}
	
	@Test(priority=3)
	public void create_Order()
	{
		OrderDetails od = new OrderDetails();
		od.setCountry("British Indian Ocean Territory");
		od.setProductOrderedId(productId);
		ArrayList<OrderDetails> orderDetailList = new ArrayList<OrderDetails>(); 
		orderDetailList.add(od);
		Order order = new Order();
		order.setOrders(orderDetailList);
		
		RequestSpecification req_create_ord= given().log().all().spec(req_Auth).contentType(ContentType.JSON).
				 body(order);
		 
		String resp = req_create_ord.when().post("/api/ecom/order/create-order")
		.then().log().all().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(resp);
		String msg = js.getString("message");
		System.out.println(msg);
		orderId = js.getString("orders[0]");
		Assert.assertEquals("Order Placed Successfully", msg);		
	}
	
	@Test(priority=4)
	public void delete_Order()
	{
		RequestSpecification req_delete_order = given().log().all().spec(req_Auth).pathParam("orderId", orderId);
		String resp = req_delete_order.when().delete("/api/ecom/order/delete-order/{orderId}")
				.then().log().all().statusCode(200).extract().response().asString();
		JsonPath js = new JsonPath(resp);
		Assert.assertEquals("Orders Deleted Successfully", js.getString("message"));
		
	}
	
	@Test(priority=5)
	public void delete_Product()
	{
		//Passing path parameter
		
		RequestSpecification req_delete_prod = given().log().all().spec(req_Auth).pathParam("productId", productId);
		String resp = req_delete_prod.when().delete("/api/ecom/product/delete-product/{productId}")
				.then().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(resp);
		Assert.assertEquals("Product Deleted Successfully", js.getString("message"));
	}
}
