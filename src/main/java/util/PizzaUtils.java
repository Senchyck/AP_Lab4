package util;

import org.springframework.stereotype.Component;

import entity.Ingredient;
import entity.Pizza;

@Component
public class PizzaUtils {

    public double calculateSmallPizzaPrice(Pizza pizza) {

        double pizzaPrice = 0.0;

        for (Ingredient ingr : pizza.getIngredients()) {
            pizzaPrice += ingr.getPrice();
        }

        return pizzaPrice;
    }

}
