package mapsapi;

import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class complexJsonParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js = new JsonPath(Payload.CoursePrice());
		
		
		//1. Print No of courses returned by API
		
		int courseSize = js.getInt("courses.size()");
		System.out.println(courseSize);
		
		//2.Print Purchase Amount
		
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//3. Print Title of the first course
		
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println(titleFirstCourse);
		
		//4. Print All course titles and their respective Prices
		
		for (int i=1;i<=courseSize;i++)
		{
			System.out.println("Title of the course is - " + js.get("courses["+i+"-1].title"));
			System.out.println("Price of the course is - " + js.get("courses["+i+"-1].price"));
		}
		
		//5. Print no of copies sold by RPA Course
		
		
		for (int i=1;i<=courseSize;i++)
		{
			if((js.get("courses["+i+"-1].title").equals("RPA")))
					{
			System.out.println("RPA course's sold copies " + js.get("courses["+i+"-1].copies"));
			break;
					}
		}
		
		//6. Verify if Sum of all Course prices matches with Purchase Amount
		
		int sumCoursePrice = 0;
		for (int i=1;i<=courseSize;i++)
		{
			sumCoursePrice = sumCoursePrice + js.getInt("courses["+i+"-1].price")*js.getInt("courses["+i+"-1].copies");			
		}
		if(sumCoursePrice==purchaseAmount)
		{
			System.out.println("Sum of all Course prices matches with Purchase Amount i.e - " + sumCoursePrice);
		}
		
		

	}

}
