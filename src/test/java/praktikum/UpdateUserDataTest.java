package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class UpdateUserDataTest {
    private final UserDataPattern user = UserDataPattern.getRandom();

    @Before
    public void setUp() {
        UserApiPattern.registration(user);
    }

    @After
    public void tearDown() {
        UserApiPattern.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя с авторизацией")
    public void changeUserEmail() {
        String userEmail = UserDataPattern.getRandomEmail();
        UserDataPattern user = new UserDataPattern(userEmail, this.user.getPassword(), this.user.getName());
        var changeResponse = UserApiPattern.changeUserData(UserApiPattern.getToken(this.user), user);

        boolean isResponseSuccess = changeResponse.extract().path("success");
        String changedEmail = changeResponse.extract().path("user.email");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertEquals(changedEmail, userEmail.toLowerCase());
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void changeUserPassword() {
        String newPassword = UserDataPattern.getRandomData();
        UserDataPattern newUser = new UserDataPattern(user.getEmail(), newPassword, user.getName());
        var changeResponse = UserApiPattern.changeUserData(UserApiPattern.getToken(user), newUser);

        boolean isResponseSuccess = changeResponse.extract().path("success");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void changeUserName() {
        String newUserName = UserDataPattern.getRandomData();
        UserDataPattern changedUser = new UserDataPattern(user.getEmail(), user.getPassword(), newUserName);
        var changeResponse = UserApiPattern.changeUserData(UserApiPattern.getToken(user), changedUser);

        boolean isResponseSuccess = changeResponse.extract().path("success");
        String actualUserName = changeResponse.extract().path("user.name");
        int actualStatusCode = changeResponse.extract().statusCode();


        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertEquals(actualUserName, newUserName);
    }

    @Test
    @DisplayName("Изменение пользовательских данных без авторизации")
    public void changeUserDataWithoutLogin() {
        var changeResponse = UserApiPattern.changeUserData("", user);
        String authorisedErrorMessage = "You should be authorised";

        boolean isResponseSuccess = changeResponse.extract().path("success");
        String responseMessage = changeResponse.extract().path("message");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_UNAUTHORIZED);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + authorisedErrorMessage + "is not displayed", responseMessage, authorisedErrorMessage);

    }

    @Test
    @DisplayName("Изменить адрес почты на уже использованную почту ")
    public void changeUserEmailToUsedEmail() {
        UserDataPattern firstUser = UserDataPattern.getRandom();
        UserApiPattern.registration(firstUser);
        String firstUserEmail = firstUser.getEmail();
        UserDataPattern secondUser = new UserDataPattern(firstUserEmail, user.getPassword(), user.getName());

        var changeResponse = UserApiPattern.changeUserData(UserApiPattern.getToken(user), secondUser);
        String existsEmailErrorMessage = "User with such email already exists";

        boolean isResponseSuccess = changeResponse.extract().path("success");
        String responseMessage = changeResponse.extract().path("message");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals("Message:" + existsEmailErrorMessage + "is not displayed", responseMessage, existsEmailErrorMessage);
    }
}
