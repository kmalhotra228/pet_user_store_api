
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import payloads.User;
import payloads.UserPayload;
import utilties.DataProviders;

public class DdUserTest {
	
	
	
	@Test(priority = 1,dataProvider = "datas",dataProviderClass = DataProviders.class)
	public void testPostUser(String userID, String userName, String fName,String lName, String useremail, String pwd,String phoneNumber )
	{
		User userpayload = UserPayload.setUserPayload(userID, userName, fName, lName, useremail, pwd, phoneNumber);
		
		Response response = UserEndpoints.createUser(userpayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 2,dataProvider = "usernames",dataProviderClass = DataProviders.class)
	public void testDeleteUserByName(String username)
	{
		Response response = UserEndpoints.deleteUser(username);
		
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}

}
