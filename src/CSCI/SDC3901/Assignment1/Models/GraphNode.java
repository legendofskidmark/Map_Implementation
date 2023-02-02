package CSCI.SDC3901.Assignment1.Models;

public class GraphNode {

    //graph node

    private String systemName;
    private UnitConversionData conversionData;

    GraphNode() {}

    public GraphNode(String systemName, UnitConversionData conversionData) {
        this.systemName = systemName;
        this.conversionData = conversionData;
    }

    public String getSystemName() {
        return systemName;
    }

    public UnitConversionData getConversionData() {
        return conversionData;
    }
}
