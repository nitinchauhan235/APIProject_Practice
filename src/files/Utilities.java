package files;

import io.restassured.path.json.JsonPath;

public class Utilities {
	
	public static JsonPath jsonPaser(String res)
	{
		JsonPath js = new JsonPath(res);
		return js;
	}

}
