package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class UserLoginTest {

    private final String EMAIL = "login@mail.ru";
    private final String PASSWORD = "12345";

    UserDataPattern login = new UserDataPattern(EMAIL, PASSWORD);
    UserDataPattern loginWithIncorrectPassword = new UserDataPattern(EMAIL, "123456");
    UserDataPattern loginWithIncorrectEmail = new UserDataPattern("login@mail.ruu", PASSWORD);

    @Test
    @DisplayName("Правильный вход пользователя с действительными учетными данными")
    public void checkCorrectLogin() {
        ValidatableResponse response = UserApiPattern.login(login);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    @Test
    @DisplayName("Вход пользователя с неверным паролем")
    public void checkLoginWithIncorrectPassword() {
        ValidatableResponse response = UserApiPattern.login(loginWithIncorrectPassword);
        response.statusCode(401).and().assertThat().body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход пользователя с неверным адресом электронной почты")
    public void checkLoginWithIncorrectEmail() {
        ValidatableResponse response = UserApiPattern.login(loginWithIncorrectEmail);
        response.statusCode(401).and().assertThat().body("message", is("email or password are incorrect"));
    }
}
