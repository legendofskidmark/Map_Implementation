package CSCI.SDC3901.A1.Models;

import CSCI.SDC3901.A1.Models.ConversionLine;
import CSCI.SDC3901.A1.Models.MeasurementSystemParams;

import java.util.ArrayList;

public class UnitConversionData {

    private MeasurementSystemParams sourceMeasurementParams; //unit conversion 1st line
    private MeasurementSystemParams targetMeasurementParams; //unit conversion 2nd line
    private ArrayList<ConversionLine> sourceToTargetUnits; // size q size q

    public UnitConversionData(MeasurementSystemParams sourceMeasurementParams, MeasurementSystemParams targetMeasurementParams, ArrayList<ConversionLine> sourceToTargetUnits) {
        this.sourceMeasurementParams = sourceMeasurementParams;
        this.targetMeasurementParams = targetMeasurementParams;
        this.sourceToTargetUnits = sourceToTargetUnits;
    }

    public ArrayList<ConversionLine> getSourceToTargetUnits() {
        return sourceToTargetUnits;
    }

    public MeasurementSystemParams getSourceMeasurementParams() {
        return sourceMeasurementParams;
    }

    public MeasurementSystemParams getTargetMeasurementParams() {
        return targetMeasurementParams;
    }
}
