package CSCI.SDC3901.A1;

public class Ingredient {
    private double quantity;
    private String units;
    private String name;
    private String quantityRepresentation = "";
    boolean isVarianceAllowed;
    Ingredient(double quantity, String units, String name) {
        this.quantity = quantity;
        this.units = units;
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public String getName() {
        return name;
    }

    public void setQuantityRepresentation(String quantityRepresentation) {
        this.quantityRepresentation = quantityRepresentation;
    }

    public void setVarianceAllowed(boolean varianceAllowed) {
        isVarianceAllowed = varianceAllowed;
    }

    @Override
    public String toString() {
        return quantityRepresentation + "\t" + units + "\t" + name;
    }
}
