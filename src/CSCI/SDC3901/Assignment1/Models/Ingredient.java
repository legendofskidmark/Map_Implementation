package CSCI.SDC3901.Assignment1.Models;

/**
 * The model class representing the ingredients structure in the Recipe file
 *
 * <p>
 *     It also has metadata about the ingredient's quantity like its variance from the true conversion fact and the Integer or fraction representation of the quantity
 * </p>
 *
 * @author boon
 */
public class Ingredient {
    private double quantity;
    private String units;
    private String name;
    private String quantityRepresentation = "";
    private boolean isVarianceAllowed;
    public Ingredient(double quantity, String units, String name) {
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

    public boolean getVarianceAllowed() {
        return isVarianceAllowed;
    }

    @Override
    public String toString() {
        return quantityRepresentation + "\t" + units + "\t" + name;
    }
}
