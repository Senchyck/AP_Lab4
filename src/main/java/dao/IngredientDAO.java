package dao;

import java.util.List;

import entity.Ingredient;

public interface IngredientDAO {

    List<Ingredient> findAll();
    Ingredient findById(int ingredientId);
    void save(Ingredient ingredient);
    void deleteById(int ingredientId) throws Exception;
}