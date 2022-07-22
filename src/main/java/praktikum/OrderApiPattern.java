package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderApiPattern extends StellarBurgerRestAssuredPattern {
    public static final String INGREDIENTS_PATH = "api/ingredients";
    private static final String ORDER_PATH = "/api/orders/";

    @Step("Получить заказы пользователя с авторизацией")
    public ValidatableResponse getUserOrdersWithAuth(String token) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step("Получить заказы пользователя без авторизации")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step("Создать заказ со авторизацией")
    public static ValidatableResponse createOrder(Ingredients order, String token) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .body(order)
                .post(ORDER_PATH)
                .then().log().all();
    }


    @Step("Получить информацию об ингредиентах")
    public static ValidatableResponse getIngredients(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }
}

