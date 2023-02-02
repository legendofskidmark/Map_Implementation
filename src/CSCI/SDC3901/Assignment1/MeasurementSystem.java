package CSCI.SDC3901.Assignment1;

import java.util.ArrayList;

public class MeasurementSystem {

    private String systemName;
    private Integer minimumWeight;
    private ArrayList<Integer> fractionInteger;

    MeasurementSystem(String systemName, Integer minimumWeight, ArrayList<Integer> fractionInteger) {
        this.fractionInteger = new ArrayList<>();
        this.systemName = systemName;
        this.minimumWeight = minimumWeight;
        this.fractionInteger = fractionInteger;
    }

    public String getSystemName() {
        return systemName;
    }
}
