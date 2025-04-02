package tests;

import com.google.gson.Gson;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import reqres_objects.ResourcesList;
import reqres_objects.User;
import reqres_objects.UsersList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqresTests {
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

    @Test()
    public void getSingleUserTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(2),
                        "data.email", equalTo("janet.weaver@reqres.in"),
                        "data.first_name", equalTo("Janet"),
                        "data.last_name", equalTo("Weaver"),
                        "data.avatar", equalTo("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test()
    public void getSingleUserNotFoundTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "users/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test()
    public void getListResourceTest() {
        String body =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "unknown")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        ResourcesList resourcesList = new Gson().fromJson(body, ResourcesList.class);
        softAssert.assertEquals(resourcesList.getData().get(0).getId(), 1);
        softAssert.assertEquals(resourcesList.getData().get(0).getName(), "cerulean");
        softAssert.assertEquals(resourcesList.getData().get(0).getYear(), 2000);
        softAssert.assertEquals(resourcesList.getData().get(0).getColor(), "#98B2D1");
        softAssert.assertEquals(resourcesList.getData().get(0).getPantoneValue(), "15-4020");
        softAssert.assertAll();
    }

    @Test()
    public void getSingleResourceTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "unknown/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(2),
                        "data.name", equalTo("fuchsia rose"),
                        "data.year", equalTo(2001),
                        "data.color", equalTo("#C74375"),
                        "data.pantone_value", equalTo("17-2031"));
    }

    @Test()
    public void getSingleResourceNotFoundTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "unknown/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test()
    public void putUpdateUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .put(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test()
    public void patchUpdateUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .patch(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test()
    public void deleteTest() {

        given()
                .log().all()
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .delete(BASE_URL + "users/2")
                .then()
                .log().all()
                .body(equalTo(""))
                .statusCode(204);
    }

    @Test()
    public void registerSuccessfulTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("id", equalTo(4),
                        "token", equalTo("QpwL5tke4Pnpja7X4"))
                .statusCode(200);
    }

    @Test()
    public void registerUnsuccessfulTest() {
        User user = User.builder()
                .email("sydney@fife")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("error", equalTo("Missing password"))
                .statusCode(400);
    }

    @Test()
    public void loginSuccessfulTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "login")
                .then()
                .log().all()
                .body("token", equalTo("QpwL5tke4Pnpja7X4"))
                .statusCode(200);
    }

    @Test()
    public void loginUnsuccessfulTest() {
        User user = User.builder()
                .email("peter@klaven")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "login")
                .then()
                .log().all()
                .body("error", equalTo("Missing password"))
                .statusCode(400);
    }

    @Test()
    public void getDelayedResponceTest() {
        String body =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "users?delay=3")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        softAssert.assertEquals(usersList.getData().get(0).getId(), 1);
        softAssert.assertEquals(usersList.getData().get(0).getFirstName(), "George");
        softAssert.assertEquals(usersList.getData().get(0).getLastName(), "Bluth");
        softAssert.assertEquals(usersList.getData().get(0).getEmail(), "george.bluth@reqres.in");
        softAssert.assertEquals(usersList.getData().get(0).getAvatar(), "https://reqres.in/img/faces/1-image.jpg");
        softAssert.assertAll();
    }
}