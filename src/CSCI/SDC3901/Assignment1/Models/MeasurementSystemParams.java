package CSCI.SDC3901.Assignment1.Models;

import java.util.ArrayList;

public class MeasurementSystemParams {
//unit conversion lo first 2 lines
    private String systemName;
    private double minWeight;
    private ArrayList<Integer> fractionIntegers;

    public MeasurementSystemParams() {}

    public MeasurementSystemParams(String systemName, double minWeight, ArrayList<Integer> fractionIntegers) {
        this.systemName = systemName;
        this.minWeight = minWeight;
        this.fractionIntegers = fractionIntegers;
    }

    public String getSystemName() {
        return systemName;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public ArrayList<Integer> getFractionIntegers() {
        return fractionIntegers;
    }
}
