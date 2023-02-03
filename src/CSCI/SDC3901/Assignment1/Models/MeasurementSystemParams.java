package CSCI.SDC3901.Assignment1.Models;

import java.util.ArrayList;

/**
 * The model class to represent the measure system parameters
 *
 * @author boon
 */
public class MeasurementSystemParams {
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
