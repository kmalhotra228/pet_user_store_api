package stepDefinition;


import io.cucumber.java.en.*;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import payloads.User;
import payloads.UserPayload;
import endpoints.UserEndpoints2;
import utilties.ExtentManager;

public class UserStepDefs {

    public static User userPayload;
    Response response;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Logger logger = LogManager.getLogger(UserStepDefs.class);

    @Given("I have user details with id {string}, username {string}, firstName {string}, lastName {string}, email {string}, password {string}, and phone {string}")
    public void i_have_user_details(String id, String username, String firstName, String lastName, String email, String password, String phone) {
        UserStepDefs.userPayload = UserPayload.setUserPayload(id, username, firstName, lastName, email, password, phone);
        
    }

    @When("I create the user")
    public void i_create_the_user() {
        response = UserEndpoints2.createUser(UserStepDefs.userPayload,logger);
        ExtentManager.logApiTest("/user", "POST", gson.toJson(UserStepDefs.userPayload), response.asString(), response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        Assert.assertEquals((int) response.getStatusCode(), statusCode.intValue());
    }
    
    @Given("I have a username {string}")
    public void i_have_a_username(String username) {
        
        logger.info("the username of user is " + username);
        Assert.assertEquals(UserStepDefs.userPayload.getUsername(), username);
    }

    @When("I retrieve the user details")
    public void i_retrieve_the_user_details() {
        response = UserEndpoints2.readUser(UserStepDefs.userPayload.getUsername(),logger);
        ExtentManager.logApiTest("/user/" + userPayload.getUsername(), "GET", null, response.asString(), response.getStatusCode());
    }

    @Then("the user response should contain firstName {string} and lastName {string}")
    public void the_user_response_should_contain(String expectedFirstName, String expectedLastName) {
        String actualFirstName = response.jsonPath().getString("firstName");
        String actualLastName = response.jsonPath().getString("lastName");
        Assert.assertEquals(actualFirstName, expectedFirstName);
        Assert.assertEquals(actualLastName, expectedLastName);
    }
    
    

    @Given("I have updated user details with username {string} and firstName {string} and lastName {string}")
    public void i_have_updated_user_details(String username, String newFirstName, String newLastName) {
        UserStepDefs.userPayload.setFirstName(newFirstName);
        UserStepDefs.userPayload.setLastName(newLastName);
        
        Assert.assertEquals(UserStepDefs.userPayload.getFirstName(), newFirstName);
        Assert.assertEquals(UserStepDefs.userPayload.getLastName(), newLastName);
    }

    @When("I update the user")
    public void i_update_the_user() {
        response = UserEndpoints2.updateUser(UserStepDefs.userPayload.getUsername(), UserStepDefs.userPayload,logger);
        ExtentManager.logApiTest("/user/" + UserStepDefs.userPayload.getUsername(), "PUT", gson.toJson(UserStepDefs.userPayload), response.asString(), response.getStatusCode());
    }

    @Given("I have a username {string} to delete")
    public void i_have_a_username_to_delete(String username) {
        Assert.assertEquals(UserStepDefs.userPayload.getUsername(), username);
    }

    @When("I delete the user")
    public void i_delete_the_user() {
        response = UserEndpoints2.deleteUser(UserStepDefs.userPayload.getUsername(),logger);
        ExtentManager.logApiTest("/user/" + UserStepDefs.userPayload.getUsername(), "DELETE", null, response.asString(), response.getStatusCode());
    }
}


