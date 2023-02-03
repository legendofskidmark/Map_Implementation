package CSCI.SDC3901.Assignment1.Models;

import java.util.ArrayList;

/**
 * The model class to indicate the UnitConversion file contents
 *
 * @author boon
 */
public class UnitConversionData {

    private MeasurementSystemParams sourceMeasurementParams;
    private MeasurementSystemParams targetMeasurementParams;
    private ArrayList<ConversionLine> sourceToTargetUnits;

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
