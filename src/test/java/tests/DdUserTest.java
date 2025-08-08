
package tests;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import endpoints.UserEndpoints2;
import io.restassured.response.Response;
import payloads.User;
import payloads.UserPayload;
import utilties.DataProviders;
import utilties.ExtentManager;

public class DdUserTest {
	
	public Logger logger;
	public Gson gson;
	
	@BeforeClass
	public void setUp() {
		
		logger = LogManager.getLogger(this.getClass());
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		logger.info("Before setUp is done logger & gson object is intialised");
	}
	

	
	
	@Test(priority = 1,dataProvider = "datas",dataProviderClass = DataProviders.class, description = "create a user with valid payload")
	public void testPostUser(String userID, String userName, String fName,String lName, String useremail, String pwd,String phoneNumber )
	
	{
		logger.info("Create a new user is intialised...");
		User userpayload = UserPayload.setUserPayload(userID, userName, fName, lName, useremail, pwd, phoneNumber);
		logger.info("User payload is created");
		logger.info(gson.toJson(userpayload));
		ExtentManager.logInfo("Create a New User");
		ExtentManager.logApiRequest(gson.toJson(userpayload));
		
		Response response = UserEndpoints2.createUser(userpayload, logger);
		response.then().body(matchesJsonSchemaInClasspath("schemas/user_post_response_schema.json"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		ExtentManager.logApiResponse(response.asPrettyString());
		ExtentManager.logApiTest("/user", "POST", gson.toJson(userpayload), response.asPrettyString(), response.getStatusCode());
		
		logger.info("create user is completed");
	}
	
	@Test(priority = 2,dataProvider = "usernames",dataProviderClass = DataProviders.class)
	public void testDeleteUserByName(String username)
	
	{
		logger.info("delete user is intialised...");
		ExtentManager.logInfo("Delete a User");
		Response response = UserEndpoints2.deleteUser(username,logger);
		ExtentManager.logApiRequest(gson.toJson(username));
		
		response.then().body(matchesJsonSchemaInClasspath("schemas/user_delete_response_schema.json"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		ExtentManager.logApiResponse(response.asPrettyString());
		ExtentManager.logApiTest("/user/"+username, "DELETE", username, response.asPrettyString(), response.getStatusCode());
		logger.info("delete user is completed");
	}

}
