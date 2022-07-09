package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private final UserDataPattern user = UserDataPattern.getRandom();
    List<String> ingredients = new ArrayList<>();


    public int randomNumber() {
        Random rand = new Random();
        int upperbound = 10;
        return rand.nextInt(upperbound);
    }

    @After
    public void tearDown() {
        UserApiPattern.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Создайте заказ с логином и действительными ингредиентами")
    public void createValidOrder() {
        String accessToken = UserApiPattern.registration(user).extract().path("accessToken");
        ingredients = OrderApiPattern.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(randomNumber()));
        var createOrder = OrderApiPattern.createOrder(orderIngredients, accessToken);

        int actualStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        int orderNumber = createOrder.extract().path("order.number");

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertNotNull(orderNumber);
    }

    @Test
    @DisplayName("Создать заказ без входа в систему")
    public void createOrderWithoutLogin() {
        ingredients = OrderApiPattern.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(randomNumber()));
        var createOrder = OrderApiPattern.createOrder(orderIngredients, "");

        int actualStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        int orderNumber = createOrder.extract().path("order.number");

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertNotNull(orderNumber);
    }

    @Test
    @DisplayName("Создайте заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        String accessToken = UserApiPattern.registration(user).extract().path("accessToken");
        Ingredients orderIngredients = new Ingredients("");
        var createOrder = OrderApiPattern.createOrder(orderIngredients, accessToken);
        String ingredientsErrorMessage = "Ingredient ids must be provided";

        int actualStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        String responseMessage = createOrder.extract().path("message");

        Assert.assertEquals(actualStatusCode, SC_BAD_REQUEST);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, ingredientsErrorMessage);
    }

    @Test
    @DisplayName("Создать заказ с недопустимым ингредиентом")
    public void createOrderWithInvalidIngredients() {
        String accessToken = UserApiPattern.registration(user).extract().path("accessToken");
        Ingredients invalidIngredients = new Ingredients("space cat fur");
        var createOrder = OrderApiPattern.createOrder(invalidIngredients, accessToken);

        int actualStatusCode = createOrder.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_INTERNAL_SERVER_ERROR);
    }

}

