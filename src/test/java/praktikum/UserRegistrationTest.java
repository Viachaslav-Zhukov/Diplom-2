package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class UserRegistrationTest {
    private final UserDataPattern user = UserDataPattern.getRandom();
    private final String fieldInputErrorMessage = "Email, password and name are required fields";
    private final String sameUserErrorMessage = "User already exists";

    @Test
    @DisplayName("Создать пользователя с действительными учетными данными")
    public void createUserWithValidCredentials() {
        var createResponse =
                UserApiPattern.registration(new UserDataPattern(user.getEmail(), user.getPassword(), user.getName()));

        boolean isResponseSuccess = createResponse.extract().path("success");
        var actualStatusCode = createResponse.extract().statusCode();
        UserApiPattern.deleteUserByUser(user);

        Assert.assertEquals("User is not created", actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя с одинаковыми учетными данными")
    public void createUserWithSameCredentials() {
        UserApiPattern.registration(new UserDataPattern("test@test.ru", user.getPassword(), user.getName()));
        var createResponse =
                UserApiPattern.registration(new UserDataPattern("test@test.ru", user.getPassword(), user.getName()));

        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        var actualStatusCode = createResponse.extract().statusCode();

        Assert.assertEquals("User was created with empty password", actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + sameUserErrorMessage + "is not displayed", responseMessage, sameUserErrorMessage);
    }

    @Test
    @DisplayName("Создать пользователя с пустым адресом электронной почты")
    public void createUserWithEmptyEmail() {
        var createResponse =
                UserApiPattern.registration(new UserDataPattern("", user.getPassword(), user.getName()));

        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        var actualStatusCode = createResponse.extract().statusCode();

        Assert.assertEquals("User was created with empty email", actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + fieldInputErrorMessage + "is not displayed", responseMessage, fieldInputErrorMessage);
    }

    @Test
    @DisplayName("Создать пользователя с пустым паролем")
    public void createUserWithEmptyPassword() {
        var createResponse =
                UserApiPattern.registration(new UserDataPattern(user.getEmail(), "", user.getName()));

        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        int actualStatusCode = createResponse.extract().statusCode();

        Assert.assertEquals("User was created with empty password", actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + fieldInputErrorMessage + "is not displayed", responseMessage, fieldInputErrorMessage);

    }

    @Test
    @DisplayName("Создать пользователя с пустым именем")
    public void createUserWithEmptyName() {
        var createResponse =
                UserApiPattern.registration(new UserDataPattern(user.getEmail(), user.getPassword(), ""));

        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        int actualStatusCode = createResponse.extract().statusCode();

        Assert.assertEquals("User was created with empty name", actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + fieldInputErrorMessage + "is not displayed", responseMessage, fieldInputErrorMessage);
    }

}

