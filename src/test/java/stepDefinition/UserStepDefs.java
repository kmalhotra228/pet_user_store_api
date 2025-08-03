package stepDefinition;


import io.cucumber.java.en.*;
import io.restassured.response.Response;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import payloads.User;
import payloads.UserPayload;
import endpoints.UserEndpoints2;
import utilties.ExtentManager;

public class UserStepDefs {

    User userPayload;
    Response response;
    public Logger logger;

    @Given("I have user details with id {string}, username {string}, firstName {string}, lastName {string}, email {string}, password {string}, and phone {string}")
    public void i_have_user_details(String id, String username, String firstName, String lastName, String email, String password, String phone) {
        this.userPayload = UserPayload.setUserPayload(id, username, firstName, lastName, email, password, phone);
        
    }

    @When("I create the user")
    public void i_create_the_user() {
        response = UserEndpoints2.createUser(this.userPayload,logger);
        ExtentManager.logApiTest("/user", "POST", userPayload.toString(), response.asString(), response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        Assert.assertEquals((int) response.getStatusCode(), statusCode.intValue());
    }
    
    @Given("I have a username {string}")
    public void i_have_a_username(String username) {
        userPayload = new User();
        userPayload.setUsername(username);
    }

    @When("I retrieve the user details")
    public void i_retrieve_the_user_details() {
        response = UserEndpoints2.readUser(userPayload.getUsername(),logger);
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
        User updatedUserPayload = UserPayload.setUpdateUserPayload(newFirstName, newLastName);
        this.userPayload = updatedUserPayload;
    }

    @When("I update the user")
    public void i_update_the_user() {
        response = UserEndpoints2.updateUser(this.userPayload.getUsername(), this.userPayload,logger);
        ExtentManager.logApiTest("/user/" + this.userPayload.getUsername(), "PUT", this.userPayload.toString(), response.asString(), response.getStatusCode());
    }

    @Given("I have a username {string} to delete")
    public void i_have_a_username_to_delete(String username) {
        userPayload.setUsername(username);
    }

    @When("I delete the user")
    public void i_delete_the_user() {
        response = UserEndpoints2.deleteUser(this.userPayload.getUsername(),logger);
        ExtentManager.logApiTest("/user/" + this.userPayload.getUsername(), "DELETE", null, response.asString(), response.getStatusCode());
    }
}


