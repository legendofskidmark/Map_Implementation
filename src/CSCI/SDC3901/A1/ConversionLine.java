package CSCI.SDC3901.A1;

public class ConversionLine {

    private UnitData sourceUnitData;
    private UnitData targetUnitData;

    ConversionLine(UnitData sourceUnitData, UnitData targetUnitData) {
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
