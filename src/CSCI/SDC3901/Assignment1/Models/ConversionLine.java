package CSCI.SDC3901.Assignment1.Models;

public class ConversionLine {

    private UnitData sourceUnitData;
    private UnitData targetUnitData;

    public ConversionLine(UnitData sourceUnitData, UnitData targetUnitData) {
        this.sourceUnitData = sourceUnitData;
        this.targetUnitData = targetUnitData;
    }

    public UnitData getSourceUnitData() {
        return sourceUnitData;
    }

    public UnitData getTargetUnitData() {
        return targetUnitData;
    }
}
