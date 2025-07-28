package endpoints;
import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.User;


public class UserEndpoints {
	
	public static Response createUser(User payload){
		
		Response response =
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(payload)
		.when()
			.post(Routes.post_url_user);
		
		return response;
		
	}
	
	public static Response readUser(String username){
		
		Response response =
		given()
			.pathParam("username", username)
		.when()
			.get(Routes.get_url_user);
		
		return response;
		
	}
	
	public static Response updateUser(String userName, User payload){
		
		Response response =
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", userName)
			.body(payload)
		.when()
			.put(Routes.update_url_user);
		
		return response;
		
	}
	
	public static Response deleteUser(String username){
		
		Response response =
		given()
			.pathParam("username", username)
		.when()
			.delete(Routes.delete_url_user);
		
		return response;
		
	}
	
	
	

}
