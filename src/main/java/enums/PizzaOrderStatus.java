package enums;

public enum PizzaOrderStatus {
    ORDERED("ordered"), IN_PREPARATION("in preparation"), PREPARED("prepared"), DELIVERED("delivered"), PICKED_UP("picked up");

    private String Name;

    private PizzaOrderStatus(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }
}
