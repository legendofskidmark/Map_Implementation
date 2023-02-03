package CSCI.SDC3901.Assignment1.Models;

/**
 * The node of the graph which contains the target system data
 *
 * @author boon
 */
public class GraphNode {

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
