package mapsapi;

import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParser1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonPath js = new JsonPath(Payload.CoursePrice());		
	
		
		//1. Print No of courses returned by API
		
		int courseSize = js.get("courses.size()");
		System.out.println("Number of courses :" + courseSize);
		
		//2.Print Purchase Amount
		
		int purchaseAmount = js.get("dashboard.purchaseAmount");
		System.out.println("Purchase amount is :" + purchaseAmount);
		
		//3. Print Title of the first course
		
		System.out.println("Title of the First Course is :" + js.get("courses[0].title").toString());
				
		//4. Print All course titles and their respective Prices
		

		for (int i=1;i<=courseSize;i++)
		{
			System.out.println("Title of the Course " + i + js.get("courses[" + (i-1) + " ].title").toString());
			System.out.println("Price of the Course " + i + js.get("courses[" + (i-1) + " ].price").toString());
		}
		
		//5. Print no of copies sold by RPA Course
		
		for (int i=1;i<=courseSize;i++)
		{
			if (js.get("courses[" + (i-1) + " ].title").toString().equalsIgnoreCase("rpa"))
			{
				System.out.println("Number of copies by RPA course are :  " + js.get("courses[" + (i-1) + " ].copies").toString());
			}			
		}		
		
		//6. Verify if Sum of all Course prices matches with Purchase Amount
		
		int sumAllCoursePrice = 0;
		for (int i=1;i<=courseSize;i++)
		{			
			int coursePrice = js.get("courses[" + (i-1) + " ].price");
			int courseCopies = js.get("courses[" + (i-1) + " ].copies");
			sumAllCoursePrice = sumAllCoursePrice + coursePrice*courseCopies;			
		}
		
		
				
	}

}
