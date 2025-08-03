package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import endpoints.UserEndpoints2;
import io.restassured.response.Response;
import payloads.User;
import payloads.UserPayload;
import utilties.ExtentManager;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class userTest2 {

    Faker faker;
    User userPayload;
    public Logger logger;
    public Gson gson;

    @BeforeClass
    public void setUpData() {
        this.userPayload = UserPayload.setUserPayload();
        logger = LogManager.getLogger(this.getClass());
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test(priority = 1)
    public void testPostUser() {
    	logger.info("Creating new user...");
        ExtentManager.logInfo("Creating new user...");
        ExtentManager.logApiRequest(gson.toJson(this.userPayload));

        Response response = UserEndpoints2.createUser(this.userPayload,logger);
        ExtentManager.logApiResponse(response.asPrettyString());

        ExtentManager.logApiTest("/user", "POST", gson.toJson(this.userPayload), response.asString(), response.getStatusCode());
        response.then().body(matchesJsonSchemaInClasspath("schemas/user_post_response_schema.json"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(Integer.parseInt(response.jsonPath().get("message")), this.userPayload.getId());
        
        logger.info("new user is created");
    }

    @Test(priority = 2)
    public void testGetUserByName(){
    
    	logger.info("GET new user details");
        ExtentManager.logInfo("Retrieving user: " + this.userPayload.getUsername());
        ExtentManager.logApiRequest(this.userPayload.getUsername());

        Response response = UserEndpoints2.readUser(this.userPayload.getUsername(),logger);
        ExtentManager.logApiResponse(response.asPrettyString());

        ExtentManager.logApiTest("/user/" + this.userPayload.getUsername(), "GET", "", response.asString(), response.getStatusCode());
        response.then().body(matchesJsonSchemaInClasspath("schemas/user_get_response_schema.json"));
        Assert.assertEquals(response.getStatusCode(), 200);
        
        logger.info("New user details is sucessfully fetched");
    }

    @Test(priority = 3)
    public void testUpdateUserByName()  {
    	logger.info("Updating the user: "+ this.userPayload.getUsername());
        ExtentManager.logInfo("Updating user: " + this.userPayload.getUsername());
        logger.info("before update fname:"+ this.userPayload.getFirstName());
        logger.info("before update lname:"+ this.userPayload.getLastName());
        logger.info("before update fname:"+ this.userPayload.getEmail());

        User updatedPayload = UserPayload.setUpdateUserPayload(); // use a new one
        String username = this.userPayload.getUsername(); // save old one for future tests
        this.userPayload = updatedPayload; // update payload
        
        logger.info("After update fname:"+ updatedPayload.getFirstName());
        logger.info("After update fname:"+ this.userPayload.getFirstName());
        logger.info("After update lname:"+ this.userPayload.getLastName());
        logger.info("After update fname:"+ this.userPayload.getEmail());
        
        ExtentManager.logApiRequest(gson.toJson(updatedPayload));
        
        

        Response response = UserEndpoints2.updateUser(username, this.userPayload,logger);
        ExtentManager.logApiTest("/user/" + username, "PUT", gson.toJson(updatedPayload), response.asString(), response.getStatusCode());
        ExtentManager.logApiResponse(response.asString());
        response.then().body(matchesJsonSchemaInClasspath("schemas/user_put_response_schema.json"));
        Assert.assertEquals(response.getStatusCode(), 200);

        // Verify updated user
        Response responseAfterUpdate = UserEndpoints2.readUser(this.userPayload.getUsername(),logger);
        ExtentManager.logApiTest("/user/" + this.userPayload.getUsername(), "GET", "", responseAfterUpdate.asString(), responseAfterUpdate.getStatusCode());
        responseAfterUpdate.then().body(matchesJsonSchemaInClasspath("schemas/user_get_response_schema.json"));
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
        
        logger.info("Sucessfully updated the user: "+ this.userPayload.getUsername());
    }

    @Test(priority = 4)
    public void testDeleteUserByName() {
    	logger.info("Deleting the user");
        ExtentManager.logInfo("Deleting user: " + this.userPayload.getUsername());
        ExtentManager.logApiRequest(this.userPayload.getUsername());

        Response response = UserEndpoints2.deleteUser(this.userPayload.getUsername(),logger);
        ExtentManager.logApiResponse(response.asString());
        ExtentManager.logApiTest("/user/" + this.userPayload.getUsername(), "DELETE", "", response.asString(), response.getStatusCode());
        response.then().body(matchesJsonSchemaInClasspath("schemas/user_delete_response_schema.json"));
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("new user is sucessfully deleted");
    }
}
