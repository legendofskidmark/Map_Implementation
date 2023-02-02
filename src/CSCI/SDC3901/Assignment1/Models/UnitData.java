package CSCI.SDC3901.Assignment1.Models;

public class UnitData {

    private double quantity;
    private String unitName;

    public UnitData(double quantity, String unitName) {
        this.unitName = unitName;
        this.quantity = quantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
