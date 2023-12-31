package entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name="ingredient")
public class Ingredient implements Comparable<Ingredient> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Size(min=1, max=50, message="The name should consist of 1 to 50 characters")
    @Column(name="ingredient_name")
    private String ingredientName;

    @Min(value=0, message="The price cannot be lower than 0")
    @Column(name="price")
    private double price;

    @Min(value=0, message="Please enter an initial number of the ingredient (0-20)")
    @Max(value=20, message="Please enter an initial number of the ingredient (0-20)")
    @Column(name="stock")
    private int stock;

    public Ingredient() {}

    public Ingredient(String name, double price, int stock) {
        this.ingredientName = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public int compareTo(Ingredient other) {
        return this.id - other.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String name) {
        this.ingredientName = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }



    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
