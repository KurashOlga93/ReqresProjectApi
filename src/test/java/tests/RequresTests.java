package tests;

import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requres_objects.User;
import requres_objects.UsersList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequresTests {
    SoftAssert softAssert = new SoftAssert();
    public static final String BASE_URL = "https://reqres.in/api/";

    @Test()
    public void postCreateUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("leader")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "users")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("leader"))
                .statusCode(201);
    }

    @Test()
    public void getUsersListTest() {

        String body =
                given()
                .log().all()
                .when()
                .get(BASE_URL + "users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                        .extract().body().asString();

        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        softAssert.assertEquals(usersList.getData().get(0).getId(), 7);
        softAssert.assertEquals(usersList.getData().get(0).getFirstName(), "Michael");
        softAssert.assertEquals(usersList.getData().get(0).getLastName(), "Lawson");
        softAssert.assertEquals(usersList.getData().get(0).getEmail(), "michael.lawson@reqres.in");
        softAssert.assertEquals(usersList.getData().get(0).getAvatar(), "https://reqres.in/img/faces/7-image.jpg");
        softAssert.assertAll();
    }
}
