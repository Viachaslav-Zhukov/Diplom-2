package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class GetUserOrdersTest {
    private final String EMAIL = "slavazukov74@gmail.com";
    private final String PASSWORD = "cbkmdfcbkmdf19";

    UserDataPattern loginForToken = new UserDataPattern(EMAIL, PASSWORD);
    OrderApiPattern orderApi;

    @Before
    public void setUp() {
        orderApi = new OrderApiPattern();
    }

    @Test
    @DisplayName("Получение  заказов при авторизации пользователя")
    public void getUserOrdersBeingAuthorized() {
        UserApiPattern userApi = new UserApiPattern();
        String token = userApi.login(loginForToken).extract().path("accessToken");
        ValidatableResponse response = orderApi.getUserOrdersWithAuth(token);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    @Test
    @DisplayName("Получение  заказов без авторизации пользователя")
    public void getUserOrdersWithoutAuthorization() {
        ValidatableResponse response = orderApi.getUserOrdersWithoutAuth();
        response.statusCode(401).and().assertThat().body("message", is("You should be authorised"));
    }
}

