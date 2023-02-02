package CSCI.SDC3901.Assignment1;

public class ConversionTableNode {

    private Double system1_size;
    private Double system2_size;
    private String system1_units;
    private String system2_units;


    ConversionTableNode(Double system1_size, String system1_units, Double system2_size, String system2_units) {
        this.system1_size = system1_size;
        this.system1_units = system1_units;

        this.system2_size = system2_size;
        this.system2_units = system2_units;
    }

    public Double getSystem1_size() {
        return system1_size;
    }

    public Double getSystem2_size() {
        return system2_size;
    }

    public String getSystem1_units() {
        return system1_units;
    }

    public String getSystem2_units() {
        return system2_units;
    }
}
