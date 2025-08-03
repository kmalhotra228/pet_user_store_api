package endpoints;

import static io.restassured.RestAssured.*;

import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.User;
import utilties.RetryUtil;

public class UserEndpoints2 {

    public static ResourceBundle getURL() {
        return ResourceBundle.getBundle("routes");
    }

    public static Response createUser(User payload,Logger logger) {
        String post_url = getURL().getString("post_url");

        return RetryUtil.retryRequest(() ->
            given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .post(post_url),
            5, 
            5,
            logger
        );
    }

    public static Response readUser(String username,Logger logger) {
        String get_url = getURL().getString("get_url");

        return RetryUtil.retryRequest(() ->
            given()
                .pathParam("username", username)
            .when()
                .get(get_url),
            5,
            5,
            logger
        );
    }

    public static Response updateUser(String userName, User payload,Logger logger) {
        String update_url = getURL().getString("update_url");

        return RetryUtil.retryRequest(() ->
            given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("username", userName)
                .body(payload)
            .when()
                .put(update_url),
            5,
            5,
            logger
        );
    }

    public static Response deleteUser(String username,Logger logger) {
        String delete_url = getURL().getString("delete_url");

        return RetryUtil.retryRequest(() ->
            given()
                .pathParam("username", username)
            .when()
                .delete(delete_url),
            5,
            5,
            logger
        );
    }
}
