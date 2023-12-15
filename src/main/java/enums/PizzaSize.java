package enums;

import globals.PizzaServiceConstants;

public enum PizzaSize {
    SMALL("smal", PizzaServiceConstants.smallPizzaMultiplier),
    MEDIUM("average", PizzaServiceConstants.mediumPizzaMultiplier),
    LARGE("big", PizzaServiceConstants.bigPizzaMultiplier);

    private String Name;
    private double priceMultiplier;

    private PizzaSize(String Name, double priceMultiplier) {
        this.Name = Name;
        this.priceMultiplier = priceMultiplier;
    }

    public String getName() {
        return Name;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}