package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserApiPattern extends StellarBurgerRestAssuredPattern {
    private static final String REGISTRATION_PATH = "api/auth/register/";
    private static final String LOGIN_PATH = "api/auth/login/";
    private static final String CHANGE_USER_DATA_PATH = "api/auth/user/";

    @Step("Регистрация пользователя")
    public static ValidatableResponse registration(UserDataPattern user) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(user)
                .post(REGISTRATION_PATH)
                .then().log().all();
    }

    @Step("Логин пользователя")
    public static ValidatableResponse login(UserDataPattern user) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(user)
                .post(LOGIN_PATH)
                .then().log().all();
    }

    @Step("Изменение пользовательских данных")
    public static ValidatableResponse changeUserData(String token, UserDataPattern user) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .body(user)
                .patch(CHANGE_USER_DATA_PATH)
                .then().log().all();
    }

    @Step("Получить токен доступа")
    public static String getToken(UserDataPattern user) {
        return login(user).extract().path("accessToken");
    }


    @Step("Удалить пользователя по токену")
    public static void deleteUserByToken(String token) {
        given()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .delete(CHANGE_USER_DATA_PATH)
                .then().log().all()
                .assertThat().statusCode(202);
    }

    @Step("Удалить пользователя по имени пользователя")
    public static void deleteUserByUser(UserDataPattern name) {
        if (getToken(name) != null) {
            deleteUserByToken(getToken(name));
        }
    }
}

