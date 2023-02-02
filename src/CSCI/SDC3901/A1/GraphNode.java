package CSCI.SDC3901.A1;

public class GraphNode {

    //graph node

    private String systemName;
    private UnitConversionData conversionData;

    GraphNode() {}

    GraphNode(String systemName, UnitConversionData conversionData) {
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
