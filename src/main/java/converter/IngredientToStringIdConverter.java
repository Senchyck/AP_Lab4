package converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import entity.Ingredient;


public class IngredientToStringIdConverter implements Converter<Ingredient, String> {

    @Override
    public String convert(Ingredient ingredient) {

        return String.valueOf(ingredient.getId());
    }
}